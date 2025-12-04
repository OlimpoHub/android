package com.app.arcabyolimpo.presentation.ui.components.molecules

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.SquareAddButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.SquareMinusButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.StandardInput

/**
 * A Composable function that creates a number stepper UI component.
 * It consists of a text input field flanked by increment and decrement buttons.
 *
 * @param label The text to be displayed as the label for the input field.
 * @param placeholder The placeholder text to be shown when the input field is empty. Defaults to "000".
 * @param value The current value to be displayed in the input field.
 * @param isError A boolean flag to indicate if the current value is in an error state. Defaults to false.
 * @param errorMessage The error message to display when isError is true. Defaults to an empty string.
 * @param onValueChange A callback function that is invoked when the value of the input field changes.
 * @param onIncrement A callback function that is invoked when the increment button is clicked.
 * @param onDecrement A callback function that is invoked when the decrement button is clicked.
 */
@Suppress("ktlint:standard:function-naming")
@Composable
fun NumberStepper(
    label: String,
    placeholder: String = "000",
    value: String,
    isError: Boolean = false,
    errorMessage: String = "",
    onValueChange: (String) -> Unit,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        StandardInput(
            label = label,
            modifier = Modifier.weight(1f),
            isError = isError,
            errorMessage = errorMessage,
            onValueChange = onValueChange,
            value = value,
            placeholder = placeholder,
        )
        SquareMinusButton(
            onClick = onDecrement,
            modifier =
                Modifier
                    .offset(y = 12.dp),
        )
        SquareAddButton(
            onClick = onIncrement,
            modifier =
                Modifier
                    .offset(y = 12.dp),
        )
    }
}
