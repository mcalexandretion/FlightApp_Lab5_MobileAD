package com.example.flightapp.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SearchInput(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        label = { Text("Введите код или название аэропорта") },
        modifier = modifier.fillMaxWidth(),
        singleLine = true
    )
}

@Preview(showBackground = true)
@Composable
fun SearchInputPreview() {
    var text by remember { mutableStateOf("SVO") }
    SearchInput(query = text, onQueryChange = { text = it })
}
