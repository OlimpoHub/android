package com.app.arcabyolimpo.presentation.ui.components.molecules

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp

@Suppress("ktlint:standard:function-naming")
/**
 * Helper for the Info Rows
 */
@Composable
fun InfoRow(
    label: String,
    value: String,
) {
    Text(
        text =
            buildAnnotatedString {
                withStyle(
                    style =
                        SpanStyle(
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 18.sp,
                        ),
                ) {
                    append(label)
                    append(" ")
                }
                withStyle(
                    style =
                        SpanStyle(
                            fontWeight = FontWeight.Normal,
                            color = Color.White,
                            fontSize = 18.sp,
                        ),
                ) {
                    append(value)
                }
            },
        modifier = Modifier.fillMaxWidth(),
    )
}
