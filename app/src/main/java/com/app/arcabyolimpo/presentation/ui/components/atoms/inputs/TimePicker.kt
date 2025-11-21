@file:OptIn(ExperimentalMaterial3Api::class)

package com.app.arcabyolimpo.presentation.ui.components.atoms.inputs

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.CalendarIcon

@Composable
fun TimeInput(
    label: String = "Hora",
    placeholder: String = "HH:mm",
    value: String = "",
    onValueChange: (String) -> Unit,
    isError: Boolean = false,
    errorMessage: String = "",
    modifier: Modifier = Modifier
) {
    var showTimePicker by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    StandardInput(
        label = label,
        placeholder = placeholder,
        value = value,
        onValueChange = {  },
        isError = isError,
        errorMessage = errorMessage,
        modifier = modifier
            .focusProperties { canFocus = false }
            .onFocusChanged {
                if (it.isFocused) focusManager.clearFocus()
            },
        trailingIcon = {
            IconButton(onClick = { showTimePicker = true }) {
                CalendarIcon(tint = MaterialTheme.colorScheme.onSurface)
            }
        }
    )

    if (showTimePicker) {
        TimePickerDialog(
            currentValue = value,
            onDismiss = { showTimePicker = false },
            onConfirm = { hour, minute ->
                val formatted = "%02d:%02d".format(hour, minute)
                onValueChange(formatted)
                showTimePicker = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    currentValue: String = "",
    onDismiss: () -> Unit,
    onConfirm: (Int, Int) -> Unit
) {
    val timeState = rememberTimePickerState()

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(timeState.hour, timeState.minute)
                }
            ) { Text("Aceptar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        },
        text = {
            TimePicker(state = timeState)
        }
    )
}
