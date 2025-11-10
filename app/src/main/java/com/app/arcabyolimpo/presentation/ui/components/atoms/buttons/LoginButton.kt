package com.app.arcabyolimpo.presentation.ui.components.atoms.buttons

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.arcabyolimpo.presentation.theme.Poppins
import com.app.arcabyolimpo.ui.theme.ButtonBlue
import com.app.arcabyolimpo.ui.theme.White

@Suppress("ktlint:standard:function-naming")
/**
 * LoginButton: blue rounded button used to login in the app.
 *
 * @param onClick: () -> Unit -> function to execute when the button is clicked
 * @param modifier: Modifier = Modifier -> modifier for custom styling
 * @param enabled: Boolean = true -> whether the button is enabled
 * @param cornerRadius: Dp = 8.dp -> how much rounded the corners are
 * @param width: Dp? = null -> button width
 * @param height: Dp? = null -> button height
 * @param content: @Composable RowScope.() -> Unit -> content of the button
 */

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
                modifier.fillMaxWidth()
            },
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(containerColor = ButtonBlue),
        shape = RoundedCornerShape(cornerRadius),
        contentPadding = PaddingValues(vertical = 15.dp),
        content = content,
    )
}

@Suppress("ktlint:standard:function-naming")
@Composable
@Preview(showBackground = true)
fun PreviewLoginButton() {
    LoginButton(onClick = {}) {
        Text(
            text = "Iniciar Sesión",
            color = White,
            fontFamily = Poppins,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
        )
    }
}
