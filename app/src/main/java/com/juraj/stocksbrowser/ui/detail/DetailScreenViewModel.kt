package com.juraj.stocksbrowser.ui.detail

import androidx.lifecycle.*
import com.juraj.stocksbrowser.navigation.NavDestinations
import com.juraj.stocksbrowser.repositories.InstrumentsRepository
import com.juraj.stocksbrowser.ui.home.toInstrumentItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class DetailScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    repository: InstrumentsRepository,
) : ViewModel() {

    private val symbol: String = NavDestinations.Details.getSymbol(savedStateHandle)
        ?: throw Exception("DetailScreenViewModel without symbol")

    val viewState = repository.getInstrument(symbol).map { instrumentEntity ->
        instrumentEntity?.let {
            DetailScreenState(it.toInstrumentItem())
        }
    }.asLiveData()

}