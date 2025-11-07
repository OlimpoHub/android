package com.app.arcabyolimpo.presentation.ui.components.atoms.buttons

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
 * LoginButton: blue rounded button used to login in the app.
 *
 * @param onClick: () -> Unit -> function to execute when the button is clicked
 * @param cornerRadius: Dp = 8.dp -> how much rounded the corners are
 * @param width: Dp = 88.dp -> button width
 * @param height: Dp = 32.dp -> button height
 */

@Suppress("ktlint:standard:function-naming")
@Composable
fun LoginButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    cornerRadius: Dp = 8.dp,
    width: Dp? = null,
    height: Dp? = null,
    content: @Composable RowScope.() -> Unit,
) {
    Button(
        onClick = onClick,
        modifier =
            if (width != null && height != null) {
                modifier.size(width, height)
            } else {
                modifier
            },
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2844AE)),
        shape = RoundedCornerShape(cornerRadius),
        contentPadding = PaddingValues(horizontal = 7.dp, vertical = 6.dp),
        content = content,
    )
}
