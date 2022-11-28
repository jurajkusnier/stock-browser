package com.juraj.stocksbrowser.ui.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.juraj.stocksbrowser.ui.theme.StocksBrowserTheme

@Composable
fun SearchTextFieldPlaceholder(onClick: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .height(IntrinsicSize.Min)

    ) {
        TextField(
            enabled = false,
            value = "",
            maxLines = 1,
            singleLine = true,
            shape = MaterialTheme.shapes.large,
            onValueChange = {},
            leadingIcon = {
                Icon(Icons.Default.Search, null)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp, start = 16.dp, end = 16.dp),
            label = {
                Text("Search stocks and ETFs")
            },
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )
        Box(
            Modifier
                .fillMaxSize()
                .clickable(onClick = onClick)
        )
    }
}

@Preview(showBackground = true, widthDp = 400)
@Composable
fun SearchTextFieldPlaceholder_Preview() {
    StocksBrowserTheme {
        SearchTextFieldPlaceholder { }
    }
}