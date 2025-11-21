package com.app.arcabyolimpo.presentation.ui.components.molecules

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.app.arcabyolimpo.presentation.ui.components.atoms.status.Poppins

/** ---------------------------------------------------------------------------------------------- *
 * TextValue -> component that receives two Strings, the first one makes it bolder and the second
 * one acts as the value.
 *
 * @param label: String -> information label
 * @param value: String -> value label or the information you want to display
 * ---------------------------------------------------------------------------------------------- */
@Composable
fun TextValue(
    label: String,
    value: String
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "$label:",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White,
            maxLines = Int.MAX_VALUE,
            overflow = TextOverflow.Visible,
            modifier = Modifier.fillMaxWidth()
        )
    }
}


