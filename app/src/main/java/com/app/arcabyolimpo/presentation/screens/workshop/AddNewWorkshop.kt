package com.app.arcabyolimpo.presentation.screens.workshop

import android.net.Uri
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
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.StandardInput
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.SelectInput
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.ImageUploadInput
import com.app.arcabyolimpo.presentation.ui.components.molecules.NavBar
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.SaveButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.CancelButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.CalendarIcon
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.StandardIconInput
import com.app.arcabyolimpo.ui.theme.Background
import com.app.arcabyolimpo.ui.theme.White
import com.app.arcabyolimpo.ui.theme.ArcaByOlimpoTheme
import com.app.arcabyolimpo.presentation.ui.components.atoms.alerts.DecisionDialog
import com.app.arcabyolimpo.presentation.ui.components.atoms.alerts.Snackbarcustom
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.DescriptionInput
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
 * @param workshopClick Callback triggered when a workshop item is clicked.
 * @param viewModel The [WorkshopsListViewModel] used to manage the UI state.
 */
@Composable
fun AddNewWorkshopScreen(
    navController: NavHostController,
    viewModel: AddNewWorkshopViewModel = hiltViewModel(),
    onSuccess: (() -> Unit)? = null
) {
    ArcaByOlimpoTheme(darkTheme = true, dynamicColor = false) {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val formData by viewModel.formData.collectAsState()
        val trainings by viewModel.trainings.collectAsStateWithLifecycle()
        val users by viewModel.users.collectAsStateWithLifecycle()
        val fieldErrors by viewModel.fieldErrors.collectAsStateWithLifecycle()

        var showConfirmDialog by remember { mutableStateOf(false) }

        val snackbarHostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()

        LaunchedEffect(Unit) {
            viewModel.loadTrainings()
            viewModel.loadUsers()
        }

        Scaffold(
            containerColor = Background,
            snackbarHost = {
                SnackbarHost(snackbarHostState) { data ->
                    Snackbarcustom(
                        title = data.visuals.message,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            },
            bottomBar = { NavBar() }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                /** Title of the forms */
                Text(
                    text = "Registrar Nuevo Taller",
                    style = MaterialTheme.typography.headlineLarge,
                    color = White,
                    modifier = Modifier.padding(bottom = 16.dp),
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                /** Name of the workshop */
                StandardInput(
                    label = "Nombre",
                    placeholder = "Ej. Panadería",
                    value = formData.name,
                    onValueChange = { viewModel.updateFormData { copy(name = it) } },
                    isError = fieldErrors["name"] == true,
                    errorMessage = null
                )

                Spacer(modifier = Modifier.height(12.dp))

                /** Trainings */
                SelectInput(
                    label = "Capacitación",
                    selectedOption = trainings.firstOrNull { it.id == formData.idTraining }?.name
                        ?: "",
                    options = trainings.map { it.name },
                    onOptionSelected = { selectedName ->
                        val selectedTraining = trainings.find { it.name == selectedName }
                        viewModel.updateFormData { copy(idTraining = selectedTraining?.id ?: "") }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    isError = fieldErrors["idTraining"] == true
                )

                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    /** Start Hour */
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
                        modifier = Modifier.weight(1f)
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
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                /** Date */
                StandardInput(
                    label = "Fecha del taller",
                    placeholder = "Ej. 2016-07-30",
                    value = formData.date,
                    onValueChange = { viewModel.updateFormData { copy(date = it) } },
                    isError = fieldErrors["date"] == true,
                    errorMessage = null
                )

                Spacer(modifier = Modifier.height(12.dp))

                /** Description */
                DescriptionInput(
                    label = "Descripción",
                    placeholder = "Escribe una breve descripción del taller...",
                    value = formData.description,
                    onValueChange = { viewModel.updateFormData { copy(description = it) } },
                    isError = fieldErrors["description"] == true,
                    errorMessage = null
                )

                Spacer(modifier = Modifier.height(12.dp))

                /** User */
                SelectInput(
                    label = "Usuario",
                    selectedOption = users.firstOrNull { it.id == formData.idUser }
                        ?.let { "${it.name} ${it.lastName}" } ?: "",
                    options = users.map { "${it.name} ${it.lastName}" },
                    onOptionSelected = { selectedName ->
                        val selectedUser =
                            users.find { "${it.name} ${it.lastName}" == selectedName }
                        viewModel.updateFormData { copy(idUser = selectedUser?.id ?: "") }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    isError = fieldErrors["idUser"] == true
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
                    errorMessage = null
                )

                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {

                    /** Cancel */
                    CancelButton(
                        modifier = Modifier
                            .size(width = 112.dp, height = 40.dp),
                        onClick = { navController.navigate(Screen.WorkshopsList.route) }
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    /** Save */
                    SaveButton(
                        onClick = { showConfirmDialog = true },
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
                            dismissText = "Cancelar"
                        )
                    }

                    LaunchedEffect(uiState.isSuccess) {
                        if (uiState.isSuccess) {
                            scope.launch {
                                snackbarHostState.showSnackbar("Taller registrado correctamente")
                            }
                            onSuccess?.invoke()
                        }
                    }


                    LaunchedEffect(uiState.error) {
                        if (uiState.error != null) {
                            scope.launch {
                                snackbarHostState.showSnackbar("Error: ${uiState.error}")
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
}