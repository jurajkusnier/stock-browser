package com.juraj.stocksbrowser.ui.common

import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun CoroutineScope.showErrorSnackBar(
    snackbarHostState: SnackbarHostState,
    onClick: () -> Unit
) {
    launch {
        snackbarHostState.currentSnackbarData?.dismiss()

        val result = snackbarHostState.showSnackbar(
            message = "Something went wrong",
            actionLabel = "Try Again!",
            duration = SnackbarDuration.Indefinite
        )

        when (result) {
            SnackbarResult.Dismissed -> {}
            SnackbarResult.ActionPerformed -> onClick()
        }
    }
}
