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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.arcabyolimpo.presentation.theme.Typography
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.SetPasswordButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.KeyIcon
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.ReturnIcon
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.StandardInput
import com.app.arcabyolimpo.ui.theme.Background
import com.app.arcabyolimpo.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("ktlint:standard:function-naming")
@Composable
fun PasswordRegistrationScreen(
    email: String?,
    onBackClick: () -> Unit,
    onPasswordRegistrationSucessClick: () -> Unit,
    viewModel: PasswordRegistrationViewModel = hiltViewModel(),
) {
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.response?.status == true) {
        LaunchedEffect(Unit) {
            onPasswordRegistrationSucessClick()
        }
    }

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
                    "Crea una \n" +
                        "contraseña para \n" +
                        "activar tu cuenta",
                style = Typography.headlineLarge,
                color = White,
            )
            Spacer(modifier = Modifier.height(80.dp))
            StandardInput(
                label = "Ingresa una nueva contraseña",
                value = password,
                onValueChange = { password = it },
                isError = uiState.error != null
                        || passwordError != null,
                errorMessage = when {
                                    passwordError != null -> passwordError
                                    else -> uiState.error
                                },
                trailingIcon = { KeyIcon() }
            )
            Spacer(modifier = Modifier.height(37.dp))
            StandardInput(
                label = "Confirma tu nueva contraseña",
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                isError = uiState.error != null
                        || confirmPasswordError != null,
                errorMessage = when {
                    confirmPasswordError != null -> confirmPasswordError
                    else -> uiState.error
                } ,
                trailingIcon = { KeyIcon() },
                visualTransformation = PasswordVisualTransformation(),
            )
            Spacer(modifier = Modifier.height(37.dp))
            SetPasswordButton(
                onClick = {
                    when {
                        password.isEmpty() -> {
                            passwordError = "Debes ingresar una nueva contraseña"
                        }
                        password.isNotEmpty() && password != confirmPassword -> {
                            passwordError = null
                            confirmPasswordError = "Las contraseñas no coinciden"
                        }
                        else -> {
                            passwordError = null
                            confirmPasswordError = null
                            if (!email.isNullOrEmpty()) {
                                viewModel.postPasswordRegistration(email, password)
                            }
                        }
                    }
                },
            )
        }
    }
}

// @Preview(showBackground = true, showSystemUi = true)
// @Composable
// fun PasswordRegistrationScreenPreview() {
//    PasswordRegistrationScreen(onBackClick = { })
// }
