package com.juraj.stocksbrowser.ui.home.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.juraj.stocksbrowser.ui.theme.StocksBrowserTheme

@Composable
fun SearchTextField(
    textFieldState: TextFieldValue,
    focusRequester: FocusRequester,
    onValueChange: (TextFieldValue) -> Unit,
    onBackArrowClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 16.dp)
    ) {
        TextField(
            value = textFieldState,
            maxLines = 1,
            singleLine = true,
            onValueChange = onValueChange,
            leadingIcon = {
                IconButton(onClick = onBackArrowClick) {
                    Icon(Icons.Default.ArrowBack, null)
                }
            }, trailingIcon = {
            if (textFieldState.text.isNotEmpty()) {
                IconButton(onClick = { onValueChange(TextFieldValue()) }) {
                    Icon(Icons.Default.Close, null)
                }
            }
        },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )
    }
}

@Preview(showBackground = true, widthDp = 400)
@Composable
fun SearchTextField_PreviewEmpty() {
    StocksBrowserTheme {
        SearchTextField(TextFieldValue(""), FocusRequester(), {}) { }
    }
}

@Preview(showBackground = true, widthDp = 400)
@Composable
fun SearchTextField_PreviewWithText() {
    StocksBrowserTheme {
        SearchTextField(TextFieldValue("Microsoft"), FocusRequester(), {}) { }
    }
}

@Preview(showBackground = true, widthDp = 400)
@Composable
fun SearchTextField_PreviewWithVeryLongText() {
    StocksBrowserTheme {
        SearchTextField(
            TextFieldValue("This is very very very long long long text text text 1234567890"),
            FocusRequester(),
            {}
        ) { }
    }
}
