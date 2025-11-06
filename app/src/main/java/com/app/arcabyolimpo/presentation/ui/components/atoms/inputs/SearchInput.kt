package com.app.arcabyolimpo.presentation.ui.components.atoms.inputs

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
fun SearchInput(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "Buscar...",
    modifier: Modifier = Modifier,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholder,
                color = Color.White.copy(alpha = 0.6f),
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
        shape = RoundedCornerShape(12.dp),
        textStyle = TextStyle(color = Color.White),
        trailingIcon = trailingIcon,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFF0F142C),
            unfocusedContainerColor = Color(0xFF0F142C),
            focusedIndicatorColor = Color(0xFF3253D1),
            unfocusedIndicatorColor = Color(0xFF3253D1),
            cursorColor = Color.White,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
        ),
    )
}