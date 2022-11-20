package com.juraj.stocksbrowser.usecases

import android.util.Log
import com.juraj.stocksbrowser.api.ApiService
import com.juraj.stocksbrowser.data.InstrumentEntity
import com.juraj.stocksbrowser.repositories.InstrumentsRepository
import com.juraj.stocksbrowser.repositories.PreferencesRepository
import okhttp3.ResponseBody
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.time.LocalDate
import javax.inject.Inject

class UpdateSymbolsUseCase @Inject constructor(
    private val cacheDir: File,
    private val apiService: ApiService,
    private val instrumentsRepository: InstrumentsRepository,
    private val preferencesRepository: PreferencesRepository,
) {

    suspend operator fun invoke() {
        apiService.downloadFile("https://www.alphavantage.co/query?function=LISTING_STATUS&apikey=demo") // TODO: hardcoded link?
            .body()?.let { responseBody ->
                saveFile(responseBody)?.let { file ->
                    val reader = file.inputStream().bufferedReader()
                    reader.consumeCsvHeader()
                    reader.lineSequence().filter { it.isNotBlank() }.forEach {
                        val (symbol, name, exchange, assetType) = it.split(
                            ',',
                            ignoreCase = false,
                            limit = 4
                        )
                        instrumentsRepository.insertInstrument(
                            InstrumentEntity(
                                symbol,
                                name,
                                exchange,
                                assetType
                            )
                        )
                    }
                    preferencesRepository.setSymbolsUpdateDate(LocalDate.now())
                }
            }
    }

    private fun BufferedReader.consumeCsvHeader() = readLine()

    private fun saveFile(body: ResponseBody): File? {
        var input: InputStream? = null
        try {
            input = body.byteStream()
            val file = File.createTempFile("symbols", ".csv", cacheDir)
            val fos = FileOutputStream(file)
            fos.use { output ->
                val buffer = ByteArray(4 * 1024)
                var read: Int
                while (input.read(buffer).also { read = it } != -1) {
                    output.write(buffer, 0, read)
                }
                output.flush()
            }
            return file
        } catch (e: Exception) {
            Log.e("UpdateSymbolsUseCase", e.toString())
        } finally {
            input?.close()
        }
        return null
    }
}