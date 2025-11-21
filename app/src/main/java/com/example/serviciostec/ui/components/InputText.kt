package com.example.serviciostec.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun InputText(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isError: Boolean = false,
    readOnly: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        isError = isError,
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        readOnly = readOnly,
        enabled = !readOnly
    )
}