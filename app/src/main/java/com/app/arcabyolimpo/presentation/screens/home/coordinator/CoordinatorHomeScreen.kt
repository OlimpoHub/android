package com.app.arcabyolimpo.presentation.screens.home.coordinator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.app.arcabyolimpo.presentation.screens.home.HomeScreen
import com.app.arcabyolimpo.presentation.ui.components.molecules.FunctionalNavBar

/**
 * Displays the coordinator's home screen with a bottom functional navigation bar.
 * Switches between different sections (Home, Workshops, Orders, etc.) based on the selected tab.
 *
 * @param navController NavController used by the HomeScreen for deeper navigation flows.
 */
@Suppress("ktlint:standard:function-naming")
@Composable
fun CoordinatorHomeScreen(navController: NavHostController) {
    var selectedTab by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Box(
            modifier =
                Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(Color.LightGray),
        ) {
            when (selectedTab) {
                0 -> HomeScreen(navController)
                1 -> Text("Pantalla Talleres")
                2 -> Text("Pantalla Pedidos")
                3 -> Text("Pantalla Inventario")
                4 -> Text("Pantalla Beneficiarios")
            }
        }
        FunctionalNavBar(
            selectedIndex = selectedTab,
            onItemSelected = { selectedTab = it },
        )
    }
}
