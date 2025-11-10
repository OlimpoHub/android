package com.app.arcabyolimpo.presentation.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.arcabyolimpo.domain.model.auth.UserRole
import com.app.arcabyolimpo.presentation.theme.Poppins
import com.app.arcabyolimpo.presentation.theme.Typography
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.LoginButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.KeyIcon
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.MailIcon
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.StandardInput
import com.app.arcabyolimpo.presentation.ui.components.atoms.logo.ArcaLogo
import com.app.arcabyolimpo.presentation.util.validateEmail
import com.app.arcabyolimpo.ui.theme.ArcaByOlimpoTheme
import com.app.arcabyolimpo.ui.theme.Background
import com.app.arcabyolimpo.ui.theme.White

@Suppress("ktlint:standard:function-naming")
@Composable
fun LoginScreen(
    onLoginSuccess: (UserRole) -> Unit,
    onRecoverPasswordClick: () -> Unit,
    onAccountActivationClick: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(state.user) {
        state.user?.let { user ->
            onLoginSuccess(user.role)
        }
    }

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Background)
                .padding(start = 32.dp, top = 0.dp, end = 32.dp, bottom = 50.dp),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.align(Alignment.Center),
        ) {
            ArcaLogo(size = 180.dp)

            Spacer(modifier = Modifier.height(50.dp))

            ArcaByOlimpoTheme(darkTheme = true, dynamicColor = false) {
                StandardInput(
                    label = "Correo Electrónico",
                    value = state.username,
                    onValueChange = { viewModel.updateUsername(it) },
                    placeholder = "E.G. ejemplo@correo.com",
                    isError = (state.username == "" || !validateEmail(state.username)) && state.error != null,
                    errorMessage = if (state.username == "") "Correo requerido" else "Correo inválido",
                    trailingIcon = { MailIcon() },
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            ArcaByOlimpoTheme(darkTheme = true, dynamicColor = false) {
                StandardInput(
                    label = "Contraseña",
                    value = state.password,
                    onValueChange = { viewModel.updatePassword(it) },
                    placeholder = "••••••••",
                    visualTransformation = PasswordVisualTransformation(),
                    isError = state.password == "" && state.error != null,
                    errorMessage = "Contraseña requerida",
                    trailingIcon = { KeyIcon() },
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Recuperar Contraseña",
                style = Typography.bodyMedium,
                textDecoration = TextDecoration.Underline,
                color = White,
                modifier = Modifier.clickable { onRecoverPasswordClick() },
            )

            Spacer(modifier = Modifier.height(36.dp))

            LoginButton(
                onClick = { viewModel.login() },
                enabled = !state.isLoading,
                modifier = Modifier.fillMaxWidth(),
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color(0xFFFFF7EB),
                        strokeWidth = 2.dp,
                    )
                } else {
                    Text(
                        text = "Iniciar Sesión",
                        color = White,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                    )
                }
            }

            state.error?.let {
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = it, color = MaterialTheme.colorScheme.error)
            }
        }

        Row(
            modifier =
                Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp),
        ) {
            Text(
                text = "Si es tu primer ingreso, ",
                style = Typography.bodyMedium,
                color = White,
            )

            Text(
                text = "activa tu cuenta",
                style = Typography.bodyMedium,
                textDecoration = TextDecoration.Underline,
                color = Color(0xFF3D59C2),
                modifier = Modifier.clickable { onAccountActivationClick() },
            )
        }
    }
}
