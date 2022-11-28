package com.juraj.stocksbrowser.ui.common

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SimpleTopAppBar(isElevated: Boolean, content: @Composable () -> Unit) {
    Surface(
        elevation = if (isElevated) 4.dp else 0.dp,
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 56.dp),
        content = content
    )
}
