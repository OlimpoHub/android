package com.app.arcabyolimpo.presentation.screens.splash

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.arcabyolimpo.domain.model.auth.UserRole

@Suppress("ktlint:standard:function-naming")
@Composable
fun SplashScreen(
    onNavigate: (UserRole?) -> Unit,
    viewModel: SplashViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsState()

    // Cuando el state ya no esté cargando, navegamos (role != null -> Home según rol; else -> Login)
    LaunchedEffect(state.isLoading, state.role) {
        if (!state.isLoading) {
            // Delay pequeño opcional si quieres mostrar la splash un momento:
            // kotlinx.coroutines.delay(300)
            onNavigate(state.role)
        }
    }

    // UI simple de splash
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(text = "MyApp", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(12.dp))
        if (state.isLoading) {
            CircularProgressIndicator()
        } else {
            // Si hay error o no-session, mostramos un texto breve (opcional)
            state.error?.let { Text(text = it, color = MaterialTheme.colorScheme.error) }
        }
    }
}
