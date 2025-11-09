package com.app.arcabyolimpo.presentation.screens.passwordrecovery

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.arcabyolimpo.presentation.theme.Typography
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.SendEmailButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.KeyIcon
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.MailIcon
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.ReturnIcon
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.StandardInput
import com.app.arcabyolimpo.ui.theme.Background
import com.app.arcabyolimpo.ui.theme.ButtonBlue
import com.app.arcabyolimpo.ui.theme.ErrorRed
import com.app.arcabyolimpo.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("ktlint:standard:function-naming")
@Composable
fun PasswordRecoveryScreen(
    onBackClick: () -> Unit,
    viewModel: PasswordRecoveryViewModel = hiltViewModel(),
) {
    var email by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        ReturnIcon(size = 16.dp)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Background,
                ),
            )
        },
        containerColor = Background
    )
    {   padding ->
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
                color = White
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "¿Olvidaste \n" +
                        "tu contraseña?",
                style = Typography.headlineLarge,
                color = White
            )
            Spacer(modifier = Modifier.height(150.dp))
            StandardInput(
                label = "Ingresa tu correo electrónico",
                placeholder = "E.G. ejemplo@correo.com",
                value = email,
                onValueChange = { email = it },
                isError = uiState.error != null
                        || uiState.message == "User not found"
                        || uiState.message == "Internal server error",
                errorMessage = when (uiState.message) {
                                "User not found" -> {
                                    "Correo no registrado"
                                }
                                "Internal server error" -> {
                                    "Error al enviar correo"
                                }
                                else -> {
                                    uiState.error
                                }
                },
                trailingIcon = { MailIcon() }
            )
            Spacer(modifier = Modifier.height(37.dp))
            SendEmailButton(
                onClick = {
                    viewModel.postPasswordRecovery(email)
                },
            )

            uiState.message?.let { message ->
                if (message == "Recovery email sent") {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "¡Correo enviado exitosamente!",
                        color = ButtonBlue,
                        style = Typography.bodySmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun PasswordRecoveryScreenPreview() {
//    PasswordRecoveryScreen(onBackClick = { }, )
//}

