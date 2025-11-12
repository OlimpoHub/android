package com.app.arcabyolimpo.presentation.screens.workshop

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.app.arcabyolimpo.presentation.navigation.Screen
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.DeleteButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.ModifyButton
import com.app.arcabyolimpo.presentation.ui.components.molecules.NavBar
import com.app.arcabyolimpo.ui.theme.Background

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkshopDetailScreen(
    navController: NavHostController,
    workshopId: String,
    viewModel: WorkshopDetailViewModel = hiltViewModel()
) {
    val workshop by viewModel.workshop.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    Scaffold(
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = { Text("Detalle del taller") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Regresar")
                    }
                }
            )
        },
        bottomBar = { NavBar() }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                when {
                    isLoading -> CircularProgressIndicator()
                    errorMessage != null -> Text(text = errorMessage ?: "")
                    workshop != null -> {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Nombre: ${workshop?.nameWorkshop}")
                            Text("Capacitación: ${workshop?.idTraining}")
                            Text("Usuario: ${workshop?.idUser}")
                            Text("Fecha: ${workshop?.date}")
                            Text("Hora: ${workshop?.startHour} - ${workshop?.finishHour}")
                            Text("Descripción: ${workshop?.description}")
                        }
                    }
                    else -> Text("No se encontró el taller")
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                DeleteButton(
                    modifier = Modifier.size(width = 112.dp, height = 40.dp),
                    onClick = {
                        // Agrega la función de eliminar si quieres
                    }
                )

                Spacer(modifier = Modifier.width(16.dp))

                ModifyButton(
                    modifier = Modifier.size(width = 112.dp, height = 40.dp),
                    onClick = {
                        if (workshopId.isNotBlank()) {
                            Log.d("WorkshopDetailScreen", "Botón Modificar clickeado, workshop id: $workshopId")
                            navController.navigate(Screen.ModifyWorkshops.createRoute(workshopId))
                        } else {
                            Log.d("WorkshopDetailScreen", "ID del taller es null, no se puede navegar")
                        }
                    }
                )
            }
        }
    }
}
