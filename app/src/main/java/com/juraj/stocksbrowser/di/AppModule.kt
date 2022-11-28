package com.juraj.stocksbrowser.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.juraj.stocksbrowser.api.ApiService
import com.juraj.stocksbrowser.api.NasdaqApiService
import com.juraj.stocksbrowser.api.YahooApiService
import com.juraj.stocksbrowser.data.AppDatabase
import com.juraj.stocksbrowser.data.InstrumentsDao
import com.juraj.stocksbrowser.data.StocksDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.io.File
import javax.inject.Qualifier
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideJson(): Json {
        return Json {
            ignoreUnknownKeys = true
        }
    }

    @Provides
    fun provideYahooApiService(
        json: Json,
        httpClient: OkHttpClient
    ): YahooApiService {
        return Retrofit.Builder()
            .baseUrl(YAHOO_BASE_URL)
            .client(httpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(YahooApiService::class.java)
    }

    @Provides
    fun provideNasdaqApiService(
        json: Json,
        httpClient: OkHttpClient
    ): NasdaqApiService {
        return Retrofit.Builder()
            .baseUrl(NASDAQ_BASE_URL)
            .client(httpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(NasdaqApiService::class.java)
    }

    @Provides
    fun provideStockApiService(
        json: Json,
        @BaseUrl baseUrl: String
    ): ApiService {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    fun provideHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.HEADERS)
        }

        return OkHttpClient
            .Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->
                val requestWithUserAgent = chain.request()
                    .newBuilder()
                    .header(
                        "User-Agent",
                        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:107.0) Gecko/20100101 Firefox/107.0"
                    )
                    .build()
                chain.proceed(requestWithUserAgent)
            }
            .build()
    }

    @Provides
    fun provideRoomDatabase(@ApplicationContext applicationContext: Context): AppDatabase =
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, DATABSE_NAME
        ).build()

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext applicationContext: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            migrations = listOf(SharedPreferencesMigration(applicationContext, USER_PREFERENCES)),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { applicationContext.preferencesDataStoreFile(USER_PREFERENCES) }
        )
    }

    @Provides
    fun provideInstrumentsDao(appDatabase: AppDatabase): InstrumentsDao =
        appDatabase.instrumentsDao()

    @Provides
    fun provideStocksDao(appDatabase: AppDatabase): StocksDao =
        appDatabase.stocksDao()

    @Provides
    fun provideCacheDir(@ApplicationContext applicationContext: Context): File =
        applicationContext.cacheDir

    @Provides
    @BaseUrl
    fun provideBaseUrl(): String = BASE_URL

    companion object {
        private const val BASE_URL = "http://10.0.2.2:8080"
        private const val NASDAQ_BASE_URL = "https://api.nasdaq.com"
        private const val YAHOO_BASE_URL = "https://query1.finance.yahoo.com"
        private const val USER_PREFERENCES = "settings"
        private const val DATABSE_NAME = "main-database"
    }
}


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BaseUrl