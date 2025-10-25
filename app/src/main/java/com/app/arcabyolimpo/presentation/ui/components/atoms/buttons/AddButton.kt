package com.app.arcabyolimpo.presentation.ui.components.atoms.buttons

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.arcabyolimpo.presentation.theme.Poppins

/**
 *
 */
@Composable
fun AddButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = modifier.size(56.dp),
        shape = CircleShape,
        colors =
            ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFFF7EB),
            ),
        contentPadding = PaddingValues(0.dp),
    ) {
        Text(
            text = "+",
            color = Color(0xFF3655C7),
            fontFamily = Poppins,
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AddButtonPreview() {
    MaterialTheme {
        AddButton(
            modifier = Modifier.size(width = 95.dp, height = 25.dp),
            onClick = {},
        )
    }
}
