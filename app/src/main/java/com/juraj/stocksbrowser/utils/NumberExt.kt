package com.juraj.stocksbrowser.utils

import com.juraj.stocksbrowser.ui.home.screen.DeltaIndicator

fun Double.format(scale: Int) = "%.${scale}f".format(this)

fun Double.toDeltaIndicator(): DeltaIndicator {
    return when {
        this < 0.0 -> DeltaIndicator.Down
        this > 0.0 -> DeltaIndicator.Up
        else -> DeltaIndicator.NoChange
    }
}