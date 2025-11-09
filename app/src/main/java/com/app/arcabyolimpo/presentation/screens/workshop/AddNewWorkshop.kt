package com.app.arcabyolimpo.presentation.screens.workshop

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.app.arcabyolimpo.presentation.navigation.Screen
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.DropdownSelector
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.StandardInput
import com.app.arcabyolimpo.presentation.ui.components.molecules.NavBar
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.SaveButton
import com.app.arcabyolimpo.ui.theme.Background
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.CancelButton
import com.app.arcabyolimpo.ui.theme.White

@Composable
fun AddNewWorkshopScreen(
    navController: NavHostController,
    viewModel: AddNewWorkshopViewModel = hiltViewModel(),
    onSuccess: (() -> Unit)? = null
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val formData by viewModel.formData.collectAsState()

    val trainings by viewModel.trainings.collectAsStateWithLifecycle()
    val users by viewModel.users.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadTrainings()
        viewModel.loadUsers()
    }

    Scaffold(
        containerColor = Background,
        bottomBar = { NavBar() }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState()) // Scroll para toda la pantalla
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Registrar Nuevo Taller",
                style = MaterialTheme.typography.headlineSmall,
                color = White,
                modifier = Modifier.padding(bottom = 16.dp),
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))
            // Nombre del taller
            StandardInput(
                label = "Nombre",
                placeholder = "Ej. Panadería",
                value = formData.name,
                onValueChange = { viewModel.updateFormData { copy(name = it) } },
                isError = uiState.error?.contains("nombre") == true,
                errorMessage = uiState.error?.takeIf { it.contains("nombre") }
            )

            Spacer(modifier = Modifier.height(12.dp))
            // Dropdown de capacitación
            DropdownSelector(
                label = "Capacitación",
                options = trainings.map { it.name to it.id },
                selectedValue = formData.idTraining,
                onOptionSelected = { selectedId ->
                    viewModel.updateFormData { copy(idTraining = selectedId) }
                },
                placeholder = "Selecciona una capacitación",
                isError = uiState.error?.contains("capacitación") == true,
                errorMessage = uiState.error?.takeIf { it.contains("capacitación") }
            )

            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Hora de entrada
                StandardInput(
                    label = "Hora de entrada",
                    placeholder = "Ej. 08:00",
                    value = formData.startHour,
                    onValueChange = { viewModel.updateFormData { copy(startHour = it) } },
                    isError = uiState.error?.contains("hora de entrada") == true,
                    errorMessage = uiState.error?.takeIf { it.contains("hora de entrada") },
                    modifier = Modifier.weight(1f)
                )

                // Hora de salida
                StandardInput(
                    label = "Hora de salida",
                    placeholder = "Ej. 12:00",
                    value = formData.finishHour,
                    onValueChange = { viewModel.updateFormData { copy(finishHour = it) } },
                    isError = uiState.error?.contains("hora de salida") == true,
                    errorMessage = uiState.error?.takeIf { it.contains("hora de salida") },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            // Fecha
            StandardInput(
                label = "Fecha del taller",
                placeholder = "Ej. 2025-11-15",
                value = formData.date,
                onValueChange = { viewModel.updateFormData { copy(date = it) } },
                isError = uiState.error?.contains("fecha") == true,
                errorMessage = uiState.error?.takeIf { it.contains("fecha") }
            )

            Spacer(modifier = Modifier.height(12.dp))
            // Horario
            StandardInput(
                label = "Horario",
                placeholder = "Ej. Lunes a viernes",
                value = formData.schedule,
                onValueChange = { viewModel.updateFormData { copy(schedule = it) } },
                isError = uiState.error?.contains("horario") == true,
                errorMessage = uiState.error?.takeIf { it.contains("horario") }
            )

            Spacer(modifier = Modifier.height(12.dp))
            // Dropdown de usuario
            DropdownSelector(
                label = "Usuario",
                options = users.map { "${it.name} ${it.lastName}" to it.id },
                selectedValue = formData.idUser,
                onOptionSelected = { selectedId ->
                    viewModel.updateFormData { copy(idUser = selectedId) }
                },
                placeholder = "Selecciona un usuario",
                isError = uiState.error?.contains("usuario") == true,
                errorMessage = uiState.error?.takeIf { it.contains("usuario") }
            )

            Spacer(modifier = Modifier.height(12.dp))
            // URL de imagen
            StandardInput(
                label = "URL de imagen",
                placeholder = "https://ejemplo.com/imagen.jpg",
                value = formData.image,
                onValueChange = { viewModel.updateFormData { copy(image = it) } },
                isError = uiState.error?.contains("url") == true,
                errorMessage = uiState.error?.takeIf { it.contains("url") }
            )

            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Cancelar
                CancelButton(
                    modifier = Modifier
                        .size(width = 112.dp, height = 40.dp),
                    onClick = { navController.navigate(Screen.WorkshopsList.route) }
                )

                Spacer(modifier = Modifier.width(16.dp))

                // Guardar
                SaveButton(
                    onClick = { viewModel.addNewWorkshop() },
                    width = 112.dp,
                    height = 40.dp
                )
            }

            // Mensajes de éxito/error
            if (uiState.isSuccess) {
                Text(
                    text = "Taller creado correctamente",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 12.dp)
                )
                LaunchedEffect(Unit) {
                    onSuccess?.invoke()
                }
            }

            if (!uiState.isSuccess && uiState.error != null) {
                Text(
                    text = uiState.error ?: "",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}
