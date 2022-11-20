package com.juraj.stocksbrowser.ui.home

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.juraj.stocksbrowser.ui.theme.StocksBrowserTheme

@Composable
fun SearchTextField(textFieldValue: String, onValueChange: (String) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        TextField(value = textFieldValue,
            maxLines = 1,
            singleLine = true,
            onValueChange = onValueChange,
            leadingIcon = {
                Icon(Icons.Default.Search, null)
            }, trailingIcon = {
                if (textFieldValue.isNotEmpty()) {
                    IconButton(onClick = { onValueChange("") }) {
                        Icon(Icons.Default.Close, null)
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(), label = {
                Text("Search stocks and ETFs")
            }
        )
    }
}

@Preview(showBackground = true, widthDp = 400)
@Composable
fun SearchTextField_PreviewEmpty() {
    StocksBrowserTheme {
        SearchTextField("") { }
    }
}

@Preview(showBackground = true, widthDp = 400)
@Composable
fun SearchTextField_PreviewWithText() {
    StocksBrowserTheme {
        SearchTextField("Microsoft") { }
    }
}

@Preview(showBackground = true, widthDp = 400)
@Composable
fun SearchTextField_PreviewWithVeryLongText() {
    StocksBrowserTheme {
        SearchTextField("This is very very very long long long text text text") { }
    }
}