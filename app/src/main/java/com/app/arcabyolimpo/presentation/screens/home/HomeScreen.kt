package com.app.arcabyolimpo.presentation.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.app.arcabyolimpo.presentation.navigation.Screen
import com.app.arcabyolimpo.presentation.screens.home.components.MainMenu
import com.app.arcabyolimpo.presentation.screens.home.components.TopBarContent
import com.app.arcabyolimpo.presentation.screens.qr.workshopselection.QrWorkshopsListScreen
import com.app.arcabyolimpo.presentation.screens.user.UserListScreen
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.NotificationIcon
import com.app.arcabyolimpo.ui.theme.ArcaByOlimpoTheme

/**
 * Displays the Home screen, showing the main menu or a selected section’s content.
 * Uses `selectedOption` to switch views without navigation and keeps its state with rememberSaveable.
 *
 * @param navController Navigation controller used only for deeper user-related screens.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Suppress("ktlint:standard:function-naming")
@Composable
fun HomeScreen(navController: NavHostController) {
    var selectedOption by rememberSaveable { mutableStateOf<String?>(null) }

    ArcaByOlimpoTheme(darkTheme = true, dynamicColor = false) {
        Box(modifier = Modifier.fillMaxSize()) {
            Scaffold(
                containerColor = Color(0xFF040610),
                topBar = {
                    if (selectedOption == null) {
                        TopBarContent()
                    }
                },
            ) { paddingValues ->

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    if (selectedOption == null) {
                        MainMenu(
                            paddingValues = paddingValues,
                            onSelect = { selectedOption = it },
                        )
                    } else {
                        when (selectedOption) {
                            "qr" ->
                                QrWorkshopsListScreen(
                                    onBackClick = { selectedOption = null },
                                    workshopClick = { id, name ->
                                        navController.navigate(
                                            Screen.CreateQr.createRoute(
                                                id,
                                                name
                                            )
                                        )
                                    },
                                )

                            "users" ->
                                UserListScreen(
                                    onCollabClick = { id ->
                                        navController.navigate(Screen.UserDetail.createRoute(id))
                                    },
                                    onAddClick = {
                                        navController.navigate(Screen.UserRegister.route)
                                    },
                                    onBack = { selectedOption = null },
                                )

                            "training" ->
                                TopAppBar(
                                    title = {
                                        Text(
                                            "Capacitaciones",
                                            color = Color.White,
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight.Bold,
                                        )
                                    },
                                    navigationIcon = {
                                        IconButton(onClick = { selectedOption = null }) {
                                            Icon(
                                                imageVector = Icons.Default.ArrowBack,
                                                contentDescription = "Regresar",
                                                tint = Color.White,
                                            )
                                        }
                                    },
                                    colors =
                                        TopAppBarDefaults.topAppBarColors(
                                            containerColor = Color(0xFF040610),
                                        ),
                                    actions = {
                                        IconButton(onClick = { }) {
                                            NotificationIcon()
                                        }
                                    },
                                )

                            "analysis" ->
                                TopAppBar(
                                    title = {
                                        Text(
                                            "Análisis",
                                            color = Color.White,
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight.Bold,
                                        )
                                    },
                                    navigationIcon = {
                                        IconButton(onClick = { selectedOption = null }) {
                                            Icon(
                                                imageVector = Icons.Default.ArrowBack,
                                                contentDescription = "Regresar",
                                                tint = Color.White,
                                            )
                                        }
                                    },
                                    colors =
                                        TopAppBarDefaults.topAppBarColors(
                                            containerColor = Color(0xFF040610),
                                        ),
                                    actions = {
                                        IconButton(onClick = { }) {
                                            NotificationIcon()
                                        }
                                    },
                                )
                        }
                    }
                }
            }
        }
    }
}
