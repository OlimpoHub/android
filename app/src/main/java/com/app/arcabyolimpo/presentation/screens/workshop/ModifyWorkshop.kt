@file:Suppress("ktlint:standard:no-wildcard-imports")
package com.app.arcabyolimpo.presentation.screens.workshop

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.app.arcabyolimpo.presentation.navigation.Screen
import com.app.arcabyolimpo.presentation.ui.components.atoms.alerts.DecisionDialog
import com.app.arcabyolimpo.presentation.ui.components.atoms.alerts.Snackbarcustom
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.CancelButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.SaveButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.CalendarIcon
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.DescriptionInput
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.ImageUploadInput
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.SelectInput
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.StandardIconInput
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.StandardInput
import com.app.arcabyolimpo.presentation.ui.components.molecules.NavBar
import com.app.arcabyolimpo.ui.theme.ArcaByOlimpoTheme
import com.app.arcabyolimpo.ui.theme.Background
import com.app.arcabyolimpo.ui.theme.White
import kotlinx.coroutines.launch
import javax.inject.Inject



@Composable
fun modifyWorkshopScreen(
    navController: NavHostController,
    viewModel: ModifyWorkshopViewModel = hiltViewModel(),
    workshopId: String,
){
    ArcaByOlimpoTheme(darkTheme = true, dynamicColor = false) {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val isLoading by viewModel.isLoading.collectAsState()
        val formData by viewModel.formData.collectAsState()
        val users by viewModel.users.collectAsStateWithLifecycle()
        val usersLoading by viewModel.usersLoading.collectAsStateWithLifecycle()
        val usersError by viewModel.usersError.collectAsStateWithLifecycle()
        val fieldErrors by viewModel.fieldErrors.collectAsStateWithLifecycle()

        var showConfirmDialog by remember { mutableStateOf(false) }
        var expanded by remember { mutableStateOf(false) }

        val formattedDate by viewModel.formattedDate.collectAsStateWithLifecycle()

        val snackbarHostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()
        LaunchedEffect(Unit) {
            viewModel.loadWorkshop(workshopId) // Carga del taller primero
            viewModel.loadUsers() // Carga de usuarios
        }
        LaunchedEffect(formData.idUser, formData.startHour, formData.finishHour) {
            Log.d("WORKSHOP_DEBUG", "🔄 Recomposición de UI por formData:")
            Log.d("WORKSHOP_DEBUG", "  idUser (UI): ${formData.idUser}")
            Log.d("WORKSHOP_DEBUG", "  startHour (UI): ${formData.startHour}")
            Log.d("WORKSHOP_DEBUG", "  finishHour (UI): ${formData.finishHour}")
        }
        Scaffold(
            containerColor = Background,
            bottomBar = { NavBar() },
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { padding ->
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start,
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                /** Title of the forms */
                Text(
                    text = "Registrar Nuevo Taller",
                    style = MaterialTheme.typography.headlineLarge,
                    color = White,
                    modifier = Modifier.padding(bottom = 16.dp),
                    fontWeight = FontWeight.Bold,
                )

                Spacer(modifier = Modifier.height(12.dp))

                /** Name of the workshop */
                StandardInput(
                    label = "Nombre",
                    placeholder = "Ej. Panadería",
                    value = formData.name,
                    onValueChange = { viewModel.updateFormData { copy(name = it) } },
                    isError = fieldErrors["name"] == true,
                    errorMessage = null,
                )

                Spacer(modifier = Modifier.height(12.dp))

                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Cargando datos del taller...",
                            color = White,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp) // Usar 16.dp para coincidir con tu diseño original
                    ) {
                        /** Start Hour*/
                        StandardIconInput(
                            label = "Hora de entrada",
                            placeholder = "Ej. 08:00",
                            value = formData.startHour,
                            onValueChange = { viewModel.updateFormData { copy(startHour = it) } },
                            isError = fieldErrors["startHour"] == true,
                            errorMessage = null,
                            trailingIcon = {
                                CalendarIcon(tint = MaterialTheme.colorScheme.onSurface)
                            },
                            modifier = Modifier.weight(1f),
                        )


                        /** Finish Hour*/
                        StandardIconInput(
                            label = "Hora de salida",
                            placeholder = "Ej. 12:00",
                            value = formData.finishHour,
                            onValueChange = { viewModel.updateFormData { copy(finishHour = it) } },
                            isError = fieldErrors["finishHour"] == true,
                            errorMessage = null,
                            trailingIcon = {
                                CalendarIcon(tint = MaterialTheme.colorScheme.onSurface)
                            },
                            modifier = Modifier.weight(1f),
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                /** Date */
                StandardInput(
                    label = "Fecha del taller",
                    placeholder = "Ej. 2016-07-30",
                    value = if (formData.date.isNotBlank() && !fieldErrors.containsKey("date") && formattedDate.isNotBlank()) {
                        formattedDate
                    } else {
                        formData.date
                    },
                    onValueChange = { viewModel.updateFormData { copy(date = it) } },
                    isError = fieldErrors["date"] == true,
                    errorMessage = null,
                )

                Spacer(modifier = Modifier.height(12.dp))

                /** Description */
                DescriptionInput(
                    label = "Descripción",
                    placeholder = "Escribe una breve descripción del taller...",
                    value = formData.description,
                    onValueChange = { viewModel.updateFormData { copy(description = it) } },
                    isError = fieldErrors["description"] == true,
                    errorMessage = null,
                )

                Spacer(modifier = Modifier.height(12.dp))

                /** User Selection  */
                if (usersLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = "Cargando usuarios...",
                            color = White,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                } else if (usersError != null) {
                    Column {
                        Text(
                            text = "Error al cargar usuarios",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        TextButton(
                            onClick = { viewModel.loadUsers() }
                        ) {
                            Text("Reintentar")
                        }
                    }
                } else {
                    SelectInput(
                        label = "Usuario",
                        selectedOption =
                            users
                                .firstOrNull { it.idUsuario == formData.idUser }
                                ?.let { "${it.nombre} ${it.apellidoPaterno} ${it.apellidoMaterno}" }
                                ?: "Seleccionar usuario",
                        options = users.map {
                            "${it.nombre} ${it.apellidoPaterno} ${it.apellidoMaterno}"
                        },
                        onOptionSelected = { selectedText ->
                            val selectedUser = users.find {
                                "${it.nombre} ${it.apellidoPaterno} ${it.apellidoMaterno}" == selectedText
                            }

                            viewModel.updateFormData {
                                copy(
                                    idUser = selectedUser?.idUsuario ?: ""
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        isError = fieldErrors["idUser"] == true,
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                /** Video Training */
                StandardInput(
                    label = "Video de Capacitación",
                    placeholder = "https://videocapacitacion.com",
                    value = formData.videoTraining,
                    onValueChange = { viewModel.updateFormData { copy(videoTraining = it) } },
                    isError = fieldErrors["videoTraining"] == true,
                    errorMessage = null,
                )

                Spacer(modifier = Modifier.height(12.dp))

                /** Upload image */
                var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
                ImageUploadInput(
                    label = "Imagen del taller",
                    value = selectedImageUri,
                    onValueChange = { uri ->
                        selectedImageUri = uri
                        viewModel.updateFormData { copy(image = uri?.toString().orEmpty()) }
                    },
                    isError = false,
                    errorMessage = null,
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    /** Cancel */
                    CancelButton(
                        modifier =
                            Modifier
                                .size(width = 112.dp, height = 40.dp),
                        onClick = { navController.navigate(Screen.WorkshopsList.route) },
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    /** Save */
                    SaveButton(
                        onClick = {
                            if (users.isEmpty() && !usersLoading) {
                                scope.launch {
                                    snackbarHostState.showSnackbar("Debe cargar los usuarios primero")
                                }
                                viewModel.loadUsers()
                            } else {
                                showConfirmDialog = true
                            }
                        },
                        width = 112.dp,
                        height = 40.dp
                    )


                    if (showConfirmDialog) {
                        DecisionDialog(
                            onDismissRequest = {
                                showConfirmDialog = false
                                scope.launch {
                                    snackbarHostState.showSnackbar("Registro cancelado")
                                }
                            },
                            onConfirmation = {
                                showConfirmDialog = false
                                viewModel.modifyWorkshop(workshopId)
                            },
                            dialogTitle = "Confirmar registro",
                            dialogText = "¿Deseas registrar los cambios a este taller? Asegúrate de que todos los datos sean correctos.",
                            confirmText = "Confirmar",
                            dismissText = "Cancelar",
                        )
                    }

                    LaunchedEffect(uiState.isSuccess) {
                        if (uiState.isSuccess) {
                            viewModel.resetForm()
                            navController.previousBackStackEntry
                                ?.savedStateHandle
                                ?.set("snackbarMessage", "Taller modificado correctamente")
                            navController.previousBackStackEntry
                                ?.savedStateHandle
                                ?.set("snackbarSuccess", true)
                            navController.popBackStack()
                        }
                    }

                    LaunchedEffect(uiState.error) {
                        uiState.error?.let { error ->
                            navController.previousBackStackEntry
                                ?.savedStateHandle
                                ?.set("snackbarMessage", "Error: $error")
                            navController.previousBackStackEntry
                                ?.savedStateHandle
                                ?.set("snackbarSuccess", false)
                            navController.popBackStack()
                        }
                    }
                }

                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}