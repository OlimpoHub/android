package com.app.arcabyolimpo.presentation.screens.workshop

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.DropdownSelector
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.StandardInput
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.StandardIconInput
import com.app.arcabyolimpo.ui.theme.Background
import java.sql.Time

@Composable
fun AddNewWorkshopScreen(
    viewModel: AddNewWorkshopViewModel = hiltViewModel(),
    onSuccess: (() -> Unit)? = null
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val formData by viewModel.formData.collectAsState()

    // Estados para los dropdowns
    val trainings by viewModel.trainings.collectAsStateWithLifecycle()
    val users by viewModel.users.collectAsStateWithLifecycle()

    // Cargar datos cuando se inicia la pantalla
    LaunchedEffect(Unit) {
        viewModel.loadTrainings()
        viewModel.loadUsers()
    }

    Scaffold(
        containerColor = Background
    ){ padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Registrar Nuevo Taller",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Nombre del taller
            StandardInput(
                label = "Nombre",
                placeholder = "Ej. Panaderia",
                value = formData.name,
                onValueChange = { viewModel.updateFormData { copy(name = it) } },
                isError = uiState.error?.contains("nombre") == true,
                errorMessage = uiState.error?.takeIf { it.contains("nombre") }
            )

            // Dropdown para ID de capacitación
            DropdownSelector(
                label = "Capacitación",
                options = users.map { "${it.name} ${it.lastName}" to it.id },
                selectedValue = formData.idTraining,
                onOptionSelected = { selectedId ->
                    viewModel.updateFormData { copy(idUser = selectedId) }
                },
                placeholder = "Selecciona una capacitación",
                isError = uiState.error?.contains("capatiación") == true,
                errorMessage = uiState.error?.takeIf { it.contains("capacitación") }
            )

            // Hora de inicio
            StandardInput(
                label = "Hora de inicio",
                placeholder = "Ej. 08:00",
                value = formData.startHour.toString().substring(0,5),
                onValueChange = { input ->
                    val timeValue = try {
                        Time.valueOf("$input:00")
                    } catch (e: IllegalArgumentException) {
                        Time(0)
                    }
                    viewModel.updateFormData { copy(startHour = timeValue) }
                },
            )

            // Hora de salida
            StandardInput(
                label = "Hora de salida",
                placeholder = "Ej. 12:00",
                value = formData.finishHour.toString().substring(0,5),
                onValueChange = { input ->
                    val timeValue = try {
                        Time.valueOf("$input:00")
                    } catch (e: IllegalArgumentException) {
                        Time(0)
                    }
                    viewModel.updateFormData { copy(finishHour = timeValue) } // Corregí el error aquí
                },
            )

            // Fecha
            StandardInput(
                label = "Fecha del taller",
                placeholder = "Ej. 2025-11-15",
                value = formData.date.toString(),
                onValueChange = { input ->
                    val dateValue = try {
                        java.sql.Date.valueOf(input)
                    } catch (e: IllegalArgumentException) {
                        java.sql.Date(System.currentTimeMillis())
                    }
                    viewModel.updateFormData { copy(date = dateValue) }
                },
            )

            // Horario
            StandardInput(
                label = "Horario",
                placeholder = "Ej. Lunes a viernes",
                value = formData.schedule,
                onValueChange = { viewModel.updateFormData { copy(schedule = it) } },
            )

            // Dropdown para ID del usuario
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

            // Imagen (opcional)
            StandardIconInput(
                label = "URL de imagen",
                placeholder = "https://ejemplo.com/imagen.jpg",
                value = formData.image,
                onValueChange = { viewModel.updateFormData { copy(image = it) } },
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.addNewWorkshop() },
                enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Crear taller")
                }
            }

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

            if (uiState.error != null && !uiState.isSuccess) {
                Text(
                    text = uiState.error ?: "",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}