package com.app.arcabyolimpo.presentation.screens.passwordregisteration

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.arcabyolimpo.presentation.screens.tokenverification.TokenVerificationFailedScreen
import com.app.arcabyolimpo.presentation.theme.Typography
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.ReturnIcon
import com.app.arcabyolimpo.ui.theme.Background
import com.app.arcabyolimpo.ui.theme.ErrorRed
import com.app.arcabyolimpo.ui.theme.White

/**
 * Composable screen displayed when the user successfully registers a new password.
 *
 * This screen confirms that the password setup process has been completed and encourages
 * the user to log in with their new credentials. It maintains a consistent visual design
 * with the rest of the authentication flow using the app’s theme and typography.
 *
 * @param onBackClick Callback triggered when the user taps the back navigation button.
 */

@Suppress("ktlint:standard:function-naming")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordRegistrationSuccessScreen(onBackClick: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        ReturnIcon(size = 16.dp)
                    }
                },
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = Background,
                    ),
            )
        },
        containerColor = Background,
    ) { padding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 32.dp, vertical = 8.dp),
        ) {
            Text(
                text = "El Arca en Querétaro I.A.P",
                style = Typography.bodyMedium,
                color = White,
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text =
                    "¡Listo! \n" +
                        "Intenta\n" +
                        "iniciar sesión",
                style = Typography.headlineLarge,
                color = White,
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TokenVerificationFailedScreenPreview() {
    TokenVerificationFailedScreen(onBackClick = { })
}
