package com.app.arcabyolimpo.presentation.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import com.app.arcabyolimpo.presentation.screens.home.components.InventoryMainScreen
import com.app.arcabyolimpo.presentation.screens.home.components.InventoryTopBar
import com.app.arcabyolimpo.presentation.screens.productbatches.productBatchesList.ProductBatchesListScreen
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.NotificationIcon
import com.app.arcabyolimpo.ui.theme.ArcaByOlimpoTheme

/**
 * InventoryScreen manages the inventory home UI and its internal navigation.
 * @param navController NavHostController used for navigation between app screens.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Suppress("ktlint:standard:function-naming")
@Composable
fun InventoryScreen(navController: NavHostController) {
    var selectedOption by rememberSaveable { mutableStateOf<String?>(null) }

    ArcaByOlimpoTheme(darkTheme = true, dynamicColor = false) {
        Box(modifier = Modifier.fillMaxSize()) {
            Scaffold(
                containerColor = Color(0xFF040610),
                topBar = {
                    if (selectedOption == null) {
                        InventoryTopBar()
                    }
                },
            ) { paddingValues ->

                if (selectedOption == null) {
                    InventoryMainScreen(
                        paddingValues = paddingValues,
                        onSelect = { selectedOption = it },
                    )
                } else {
                    when (selectedOption) {
                        "products" ->
                            TopAppBar(
                                title = {
                                    Text(
                                        "Productos",
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
                        "product_batches" ->
                            ProductBatchesListScreen(
                                onBackClick = { selectedOption = null },
                                onDetailClick = { id ->
                                    navController.navigate(Screen.ProductBatchDetail.createRoute(id))
                                },
                                onAddClick = {
                                    navController.navigate(Screen.ProductBatchRegister.route)
                                },
                            )
                        "supplies" ->
                            TopAppBar(
                                title = {
                                    Text(
                                        "Insumos",
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
