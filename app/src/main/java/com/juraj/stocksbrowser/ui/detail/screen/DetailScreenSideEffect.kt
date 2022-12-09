package com.juraj.stocksbrowser.ui.detail.screen

sealed class DetailScreenSideEffect {
    object NavigateHome : DetailScreenSideEffect()
    object NetworkError : DetailScreenSideEffect()
}
