package com.juraj.stocksbrowser

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.juraj.stocksbrowser.ui.theme.StocksBrowserTheme

@Composable
fun SearchTextField(doSearch: (String) -> Unit) {
    var textFieldValue by remember { mutableStateOf("") }

    Row(verticalAlignment = Alignment.CenterVertically) {
        TextField(value = textFieldValue, onValueChange = {
            textFieldValue = it
        }, trailingIcon = {
            IconButton(onClick = {
                doSearch(textFieldValue)
            }) {
                Icon(Icons.Default.Search, null)
            }
        }, modifier = Modifier.fillMaxWidth(), label = {
            Text("Search term")
        })
    }
}

@Preview(showBackground = true, widthDp = 400)
@Composable
fun SearchTextField_Preview() {
    StocksBrowserTheme {
        SearchTextField { }
    }
}