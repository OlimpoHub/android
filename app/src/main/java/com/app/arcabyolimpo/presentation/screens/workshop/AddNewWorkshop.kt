@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.app.arcabyolimpo.presentation.screens.workshop

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import com.app.arcabyolimpo.presentation.ui.components.atoms.alerts.DecisionDialog
import com.app.arcabyolimpo.presentation.ui.components.atoms.alerts.Snackbarcustom
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.CancelButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.SaveButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.DateInput
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.DescriptionInput
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.ImageUploadInput
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.SelectInput
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.TimeInput
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.StandardInput
import com.app.arcabyolimpo.ui.theme.ArcaByOlimpoTheme
import com.app.arcabyolimpo.ui.theme.Background
import com.app.arcabyolimpo.ui.theme.White
import kotlinx.coroutines.launch

/**
 * Composable screen that displays the registrations of new workshops.
 *
 * This screen is responsible for showing a forms to register workshops
 * retrieved from the [AddNewWorkshopViewModel]. It provides:
 * - Each field required to register a new workshop in the data base.
 * - A button to cancel the forms an return to [WorkshopsList].
 * - A button to save all the fields and send it to the data base.
 * - A navbar at the button of the screen.
 *
 * @param navController The way to go through different screens that are in the [NavGraph]
 * @param viewModel The [WorkshopsListViewModel] used to manage the UI state.
 */
@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun AddNewWorkshopScreen(
    navController: NavHostController,
    viewModel: AddNewWorkshopViewModel = hiltViewModel(),
    onSuccess: (() -> Unit)? = null,
) {
    ArcaByOlimpoTheme(darkTheme = true, dynamicColor = false) {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val formData by viewModel.formData.collectAsState()
        val users by viewModel.users.collectAsStateWithLifecycle()
        val usersLoading by viewModel.usersLoading.collectAsStateWithLifecycle()
        val usersError by viewModel.usersError.collectAsStateWithLifecycle()
        val fieldErrors by viewModel.fieldErrors.collectAsStateWithLifecycle()

        var showConfirmDialog by remember { mutableStateOf(false) }
        var expanded by remember { mutableStateOf(false) }

        val snackbarHostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()

        LaunchedEffect(Unit) {
            viewModel.loadUsers()
        }

        LaunchedEffect(usersError) {
            usersError?.let { error ->
                scope.launch {
                    snackbarHostState.showSnackbar("Error: $error")
                }
            }
        }

        Scaffold(
            containerColor = Background,
            topBar = {
                AddWorkshopTopBar(
                    onBackClick = { navController.popBackStack() },
                )
            },
            snackbarHost = {
                SnackbarHost(snackbarHostState) { data ->
                    Snackbarcustom(
                        title = data.visuals.message,
                        modifier = Modifier.padding(16.dp),
                    )
                }
            }

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
                /** Name of the workshop */
                StandardInput(
                    label = "Nombre",
                    placeholder = "Ej. Panadería",
                    value = formData.name,
                    onValueChange = { viewModel.updateFormData { copy(name = it) } },
                    isError = fieldErrors["name"] == true,
                    errorMessage = if (fieldErrors["name"] == true) "Nombre inválido" else "",
                    )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    /** Start Hour */
                    TimeInput(
                        label = "Hora de entrada",
                        placeholder = "Ej. 08:00",
                        value = formData.startHour,
                        onValueChange = { viewModel.updateFormData { copy(startHour = it) } },
                        isError = fieldErrors["startHour"] == true,
                        errorMessage = if (fieldErrors["startHour"] == true) "Hora inválida, debe ser HH:mm" else "",
                        modifier = Modifier.weight(1f)
                    )

                    /** Finish Hour */
                    TimeInput(
                        label = "Hora de salida",
                        placeholder = "Ej. 12:00",
                        value = formData.finishHour,
                        onValueChange = { viewModel.updateFormData { copy(finishHour = it) } },
                        isError = fieldErrors["finishHour"] == true,
                        errorMessage = if (fieldErrors["finishHour"] == true) "Hora inválida, debe ser HH:mm" else "",
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                /** Date */
                DateInput(
                    label = "Fecha del taller",
                    placeholder = "dd/MM/yyyy",
                    value = formData.date,
                    onValueChange = { newDate ->
                        viewModel.updateFormData { copy(date = newDate) }
                    },
                    isError = fieldErrors["date"] == true,
                    errorMessage = if (fieldErrors["date"] == true) "Fecha inválida, debe ser dd/MM/yyyy" else "",
                    )

                Spacer(modifier = Modifier.height(12.dp))

                /** Description */
                DescriptionInput(
                    label = "Descripción",
                    placeholder = "Escribe una breve descripción del taller...",
                    value = formData.description,
                    onValueChange = { viewModel.updateFormData { copy(description = it) } },
                    isError = fieldErrors["description"] == true,
                    errorMessage = if (fieldErrors["description"] == true) "Descripción requerida" else "",
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

                            viewModel.updateFormData { copy(idUser = selectedUser?.idUsuario ?: "") }
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
                    errorMessage = if (fieldErrors["videoTraining"] == true) "URL inválida, debe ser https://" else "",
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
                    errorMessage = if (fieldErrors["image"] == true) "Imagen requerida" else "",
                    isError = fieldErrors["image"] == true,
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
                        onClick = {navController.popBackStack() },
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
                                viewModel.addNewWorkshop()
                            },
                            dialogTitle = "Confirmar registro",
                            dialogText = "¿Deseas registrar este taller? Asegúrate de que todos los datos sean correctos.",
                            confirmText = "Confirmar",
                            dismissText = "Cancelar",
                        )
                    }

                    LaunchedEffect(uiState.isSuccess) {
                        if (uiState.isSuccess) {
                            viewModel.resetForm()
                            navController.previousBackStackEntry
                                ?.savedStateHandle
                                ?.set("snackbarMessage", "Taller registrado correctamente")
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddWorkshopTopBar(
    onBackClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = "Registrar Nuevo Taller",
                style = MaterialTheme.typography.titleLarge,
                color = White,
                fontWeight = FontWeight.Bold
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Regresar",
                    tint = White
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Background,
            titleContentColor = White,
            navigationIconContentColor = White
        )
    )
}



