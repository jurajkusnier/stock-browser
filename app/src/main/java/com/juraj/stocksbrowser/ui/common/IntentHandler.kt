package com.juraj.stocksbrowser.ui.common

interface IntentHandler<INTENT : Any> {
    fun handleIntent(intent: INTENT)
}
