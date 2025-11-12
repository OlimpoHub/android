package com.app.arcabyolimpo.presentation.screens.tokenverification

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
import com.app.arcabyolimpo.presentation.screens.passwordregisteration.PasswordRegistrationViewModel
import com.app.arcabyolimpo.presentation.theme.Typography
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.ReturnIcon
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.StandardInput
import com.app.arcabyolimpo.ui.theme.Background
import com.app.arcabyolimpo.ui.theme.ErrorRed
import com.app.arcabyolimpo.ui.theme.White

/**
 * Composable screen displayed when the token verification process fails.
 *
 * This screen informs the user that the provided token is invalid and suggests retrying the process.
 * It includes a top app bar with a back navigation icon and displays error feedback
 * using appropriate typography and colors.
 *
 * @param onBackClick Lambda function triggered when the user taps the back button.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("ktlint:standard:function-naming")
@Composable
fun TokenVerificationFailedScreen(onBackClick: () -> Unit) {
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
                    "¡Token inválido! \n" +
                        "Intenta de nuevo",
                style = Typography.headlineLarge,
                color = ErrorRed,
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TokenVerificationFailedScreenPreview() {
    TokenVerificationFailedScreen(onBackClick = { })
}
