package com.app.arcabyolimpo.presentation.ui.components.molecules

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.arcabyolimpo.presentation.theme.Poppins
import com.app.arcabyolimpo.ui.theme.White

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
    value: String,
) {
    Row {
        Text(
            text = "$label:",
            color = White,
            fontFamily = Poppins,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = value,
            color = White,
            fontFamily = Poppins,
            fontWeight = FontWeight.Normal,
            fontSize = 15.sp,
        )
    }
}
