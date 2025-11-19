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

@Suppress("ktlint:standard:function-naming")
@Composable
fun NumberStepper(
    label: String,
    placeholder: String = "000",
    value: String,
    isError: Boolean = false,
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
