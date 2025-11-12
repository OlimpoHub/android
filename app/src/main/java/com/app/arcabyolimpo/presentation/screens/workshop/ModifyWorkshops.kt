package com.app.arcabyolimpo.presentation.screens.workshop

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.app.arcabyolimpo.domain.model.workshops.Workshop
import com.app.arcabyolimpo.presentation.navigation.Screen
import com.app.arcabyolimpo.presentation.ui.components.atoms.alerts.DecisionDialog
import com.app.arcabyolimpo.presentation.ui.components.atoms.alerts.Snackbarcustom
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.*
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.*
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.CalendarIcon
import com.app.arcabyolimpo.presentation.ui.components.molecules.NavBar
import com.app.arcabyolimpo.ui.theme.Background
import com.app.arcabyolimpo.ui.theme.White
import kotlinx.coroutines.launch

@Composable
fun ModifyWorkshopScreen(
    workshopId: String,
    navController: NavHostController,
    viewModel: ModifyWorkshopViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val formData by viewModel.formData.collectAsState()
    val trainings by viewModel.trainings.collectAsStateWithLifecycle()
    val users by viewModel.users.collectAsStateWithLifecycle()
    val fieldErrors by viewModel.fieldErrors.collectAsStateWithLifecycle()

    var showConfirmDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(workshopId) {
        viewModel.loadWorkshop(workshopId)
    }

    if (uiState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    Scaffold(
        containerColor = Background,
        snackbarHost = { SnackbarHost(snackbarHostState) { data ->
            Snackbarcustom(title = data.visuals.message, modifier = Modifier.padding(16.dp))
        } },
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

            Text(
                text = "Modificar Taller",
                style = MaterialTheme.typography.headlineLarge,
                color = White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            /** Nombre */
            StandardInput(
                label = "Nombre",
                placeholder = "Ej. Panadería",
                value = formData.name.orEmpty(),
                onValueChange = { viewModel.updateFormData { copy(name = it) } },
                isError = fieldErrors["name"] == true
            )

            Spacer(modifier = Modifier.height(12.dp))

            /** Capacitación */
            SelectInput(
                label = "Capacitación",
                selectedOption = trainings.firstOrNull { it.id == formData.idTraining }?.name.orEmpty(),
                options = trainings.map { it.name },
                onOptionSelected = { selectedName ->
                    val selectedTraining = trainings.find { it.name == selectedName }
                    viewModel.updateFormData { copy(idTraining = selectedTraining?.id.orEmpty()) }
                },
                modifier = Modifier.fillMaxWidth(),
                isError = fieldErrors["idTraining"] == true
            )

            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                /** Hora de entrada */
                StandardIconInput(
                    label = "Hora de entrada",
                    placeholder = "Ej. 08:00",
                    value = formData.startHour.orEmpty(),
                    onValueChange = { viewModel.updateFormData { copy(startHour = it) } },
                    isError = fieldErrors["startHour"] == true,
                    trailingIcon = { CalendarIcon(tint = MaterialTheme.colorScheme.onSurface) },
                    modifier = Modifier.weight(1f)
                )

                /** Hora de salida */
                StandardIconInput(
                    label = "Hora de salida",
                    placeholder = "Ej. 12:00",
                    value = formData.finishHour.orEmpty(),
                    onValueChange = { viewModel.updateFormData { copy(finishHour = it) } },
                    isError = fieldErrors["finishHour"] == true,
                    trailingIcon = { CalendarIcon(tint = MaterialTheme.colorScheme.onSurface) },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            /** Fecha */
            StandardInput(
                label = "Fecha del taller",
                placeholder = "Ej. 2016-07-30",
                value = formData.date.orEmpty(),
                onValueChange = { viewModel.updateFormData { copy(date = it) } },
                isError = fieldErrors["date"] == true
            )

            Spacer(modifier = Modifier.height(12.dp))

            /** Descripción */
            DescriptionInput(
                label = "Descripción",
                placeholder = "Escribe una breve descripción del taller...",
                value = formData.description.orEmpty(),
                onValueChange = { viewModel.updateFormData { copy(description = it) } },
                isError = fieldErrors["description"] == true
            )

            Spacer(modifier = Modifier.height(12.dp))

            /** Usuario */
            SelectInput(
                label = "Usuario",
                selectedOption = users.firstOrNull { it.id == formData.idUser }
                    ?.let { "${it.name} ${it.lastName}" }.orEmpty(),
                options = users.map { "${it.name} ${it.lastName}" },
                onOptionSelected = { selectedName ->
                    val selectedUser = users.find { "${it.name} ${it.lastName}" == selectedName }
                    viewModel.updateFormData { copy(idUser = selectedUser?.id.orEmpty()) }
                },
                modifier = Modifier.fillMaxWidth(),
                isError = fieldErrors["idUser"] == true
            )

            Spacer(modifier = Modifier.height(12.dp))

            /** Imagen */
            var selectedImageUri by remember(formData.image) {
                mutableStateOf<Uri?>(formData.image?.takeIf { it.isNotEmpty() }?.let { Uri.parse(it) })
            }
            ImageUploadInput(
                label = "Imagen del taller",
                value = selectedImageUri,
                onValueChange = { uri ->
                    selectedImageUri = uri
                    viewModel.updateFormData { copy(image = uri?.toString().orEmpty()) }
                },
                isError = false
            )

            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CancelButton(
                    modifier = Modifier.size(width = 112.dp, height = 40.dp),
                    onClick = { navController.navigate(Screen.WorkshopsList.route) }
                )

                Spacer(modifier = Modifier.width(16.dp))

                SaveButton(
                    onClick = { showConfirmDialog = true },
                    width = 112.dp,
                    height = 40.dp
                )

                if (showConfirmDialog) {
                    DecisionDialog(
                        onDismissRequest = { showConfirmDialog = false },
                        onConfirmation = {
                            showConfirmDialog = false
                            viewModel.modifyWorkshop()
                        },
                        dialogTitle = "Confirmar modificación",
                        dialogText = "¿Deseas guardar los cambios en este taller?",
                        confirmText = "Guardar",
                        dismissText = "Cancelar"
                    )
                }

                LaunchedEffect(uiState.isSuccess) {
                    if (uiState.isSuccess) {
                        scope.launch {
                            snackbarHostState.showSnackbar("Taller modificado correctamente")
                        }
                        navController.popBackStack()
                    }
                }

                LaunchedEffect(uiState.error) {
                    uiState.error?.let {
                        scope.launch { snackbarHostState.showSnackbar("Error: $it") }
                    }
                }
            }

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}
