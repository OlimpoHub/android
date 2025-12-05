package com.app.arcabyolimpo.presentation.screens.home.assistant

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
import androidx.navigation.NavHostController
import com.app.arcabyolimpo.presentation.navigation.Screen
import com.app.arcabyolimpo.presentation.screens.qr.scanqr.ScanQrScreen
import com.app.arcabyolimpo.presentation.screens.qr.scanresult.ScanResultScreen
import com.app.arcabyolimpo.presentation.screens.session.SessionViewModel

@Suppress("ktlint:standard:function-naming")
@Composable
fun CollaboratorHomeScreen(navController: NavHostController) {
    val sessionViewModel: SessionViewModel = hiltViewModel()

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text("Bienvenido Colaborador 👑", style = MaterialTheme.typography.headlineSmall)
        Button(onClick = { navController.navigate(Screen.ScanQr.route) }) {
            Text("Qr")
        }
        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = { sessionViewModel.logout() }) {
            Text("Cerrar sesión")
        }
        Spacer(modifier = Modifier.height(12.dp))
    }
}
