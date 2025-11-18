package com.app.arcabyolimpo.presentation.screens.home.coordinator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.app.arcabyolimpo.presentation.navigation.Screen
import com.app.arcabyolimpo.presentation.screens.beneficiary.BeneficiaryListScreen
import com.app.arcabyolimpo.presentation.screens.home.HomeScreen
import com.app.arcabyolimpo.presentation.screens.session.SessionViewModel
import com.app.arcabyolimpo.presentation.screens.workshop.WorkshopsListScreen
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.AddButton
import com.app.arcabyolimpo.presentation.ui.components.molecules.FunctionalNavBar
import com.app.arcabyolimpo.presentation.ui.components.molecules.NavBar

@Suppress("ktlint:standard:function-naming")
@Composable
fun CoordinatorHomeScreen(navController: NavHostController) {
    var selectedTab by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        // CONTENIDO SUPERIOR SEGÚN TAB
        Box(
            modifier =
                Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(Color.LightGray),
        ) {
            when (selectedTab) {
                0 -> HomeScreen(navController)
                1 ->
                    WorkshopsListScreen(
                        navController = navController,
                        workshopClick = { id ->
                            navController.navigate(Screen.WorkshopDetail.createRoute(id))
                        },
                    )
                2 -> PedidosScreen()
                3 -> InventarioScreen()
                4 ->
                    BeneficiaryListScreen(
                        onBeneficiaryClick = { beneficiaryId ->
                            navController.navigate(Screen.BeneficiaryDetail.createRoute(beneficiaryId))
                        },
                        onFilterClick = { /* TODO: Lógica de VM */ },
                        onNotificationClick = { /* TODO: Lógica de VM */ },
                    )
            }
        }

        // NAVIGATION BAR
        FunctionalNavBar(
            selectedIndex = selectedTab,
            onItemSelected = { selectedTab = it },
        )
    }
}

@Composable fun InicioScreen() {
    Text("Pantalla Inicio")
}

@Composable fun TalleresScreen() {
    Text("Pantalla Talleres")
}

@Composable fun PedidosScreen() {
    Text("Pantalla Pedidos")
}

@Composable fun InventarioScreen() {
    Text("Pantalla Inventario")
}

@Composable fun BeneficiariosScreen() {
    Text("Pantalla Beneficiarios")
}
