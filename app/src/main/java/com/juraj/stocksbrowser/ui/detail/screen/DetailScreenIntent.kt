package com.juraj.stocksbrowser.ui.detail.screen

import com.juraj.stocksbrowser.usecases.RangeInterval

sealed class DetailScreenIntent {
    object NavigateHome : DetailScreenIntent()
    object ToggleFav : DetailScreenIntent()
    data class SelectRangeInterval(val rangeInterval: RangeInterval) : DetailScreenIntent()
}