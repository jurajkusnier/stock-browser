package com.juraj.stocksbrowser.ui.detail.screen

import com.juraj.stocksbrowser.usecases.RangeInterval

sealed class DetailIntent {
    object NavigateHome : DetailIntent()
    object ToggleFav : DetailIntent()
    object Refresh : DetailIntent()
    data class SelectRangeInterval(val rangeInterval: RangeInterval) : DetailIntent()
}
