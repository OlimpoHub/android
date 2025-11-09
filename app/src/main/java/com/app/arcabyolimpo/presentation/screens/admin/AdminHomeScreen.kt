package com.app.arcabyolimpo.presentation.screens.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.arcabyolimpo.presentation.screens.session.SessionViewModel
import androidx.navigation.NavController
import com.app.arcabyolimpo.presentation.navigation.Screen

@Suppress("ktlint:standard:function-naming")
@Composable
fun CoordinatorHomeScreen(navController: NavController) {
    val sessionViewModel: SessionViewModel = hiltViewModel()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text("Bienvenido Coordinador 👑", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(20.dp))

        // Para ir a talleres (temporal, se puede quitar)
        Button(onClick = {
            navController.navigate(Screen.WorkshopsList.route)
        }) {
            Text("Talleres")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = {
            sessionViewModel.logout()
            navController.navigate(Screen.Login.route) {
                popUpTo(Screen.CoordinatorHome.route) { inclusive = true }
            }
        }) {
            Text("Cerrar sesión")
        }
    }
}

