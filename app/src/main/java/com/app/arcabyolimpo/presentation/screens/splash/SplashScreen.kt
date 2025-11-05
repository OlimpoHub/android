package com.app.arcabyolimpo.presentation.screens.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.arcabyolimpo.domain.model.auth.UserRole
import com.app.arcabyolimpo.presentation.ui.components.atoms.logo.ArcaLogo
import com.app.arcabyolimpo.ui.theme.Background

@Suppress("ktlint:standard:function-naming")
@Composable
fun SplashScreen(
    onNavigate: (UserRole?) -> Unit,
    viewModel: SplashViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(state.isLoading, state.role) {
        if (!state.isLoading) {
            onNavigate(state.role)
        }
    }

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Background),
        contentAlignment = Alignment.Center,
    ) {
        ArcaLogo(size = 160.dp)
    }
}
