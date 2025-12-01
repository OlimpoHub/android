package com.app.arcabyolimpo.presentation.screens.home.coordinator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.app.arcabyolimpo.presentation.navigation.Screen
import com.app.arcabyolimpo.presentation.screens.beneficiary.BeneficiaryListScreen
import com.app.arcabyolimpo.presentation.screens.home.HomeScreen
import com.app.arcabyolimpo.presentation.screens.home.InventoryScreen
import com.app.arcabyolimpo.presentation.screens.workshop.WorkshopsListScreen
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.NotificationIcon
import com.app.arcabyolimpo.presentation.ui.components.molecules.FunctionalNavBar

/**
 * Displays the coordinator's home screen with a bottom functional navigation bar.
 * Switches between different sections (Home, Workshops, Orders, etc.) based on the selected tab.
 *
 * @param navController NavController used by the HomeScreen for deeper navigation flows.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Suppress("ktlint:standard:function-naming")
@Composable
fun CoordinatorHomeScreen(navController: NavHostController) {
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    var homePressedTrigger by remember { mutableIntStateOf(0) }
    var inventoryPressedTrigger by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color(0xFF040610)),
        ) {
            when (selectedTab) {
                0 -> HomeScreen(navController, homePressedTrigger)
                1 -> WorkshopsListScreen(
                    navController = navController,
                    workshopClick = { id ->
                        navController.navigate(Screen.WorkshopDetail.createRoute(id))
                    }
                )

                2 -> {
                    Column(modifier = Modifier.fillMaxSize()) {
                        TopAppBar(
                            title = {
                                Text(
                                    "Pedidos",
                                    color = Color.White,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                )
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = Color(0xFF040610),
                            )
                        )

                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "En Proceso...",
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                3 -> InventoryScreen(navController, inventoryPressedTrigger)

                4 -> BeneficiaryListScreen(
                    navController = navController,
                    onBeneficiaryClick = { beneficiaryId ->
                        navController.navigate(Screen.BeneficiaryDetail.createRoute(beneficiaryId))
                    },
                    onFilterClick = {  },
                    onNotificationClick = { }
                )
            }
        }
        FunctionalNavBar(
            selectedIndex = selectedTab,
            onItemSelected = { index ->
                if (index == 0) {
                    homePressedTrigger++
                }
                if (index == 3) {
                    inventoryPressedTrigger++
                }
                selectedTab = index
            },
        )
    }
}
