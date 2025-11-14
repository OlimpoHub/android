package com.app.arcabyolimpo.presentation.screens.beneficiary

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

@Composable
fun AddNewBeneficiaryScreen(
    navController: NavHostController,
    viewModel: AddNewBeneficiaryViewModel = hiltViewModel(),
    onSuccess: (() -> Unit)? = null
) {
    ArcaByOlimpoTheme(darkTheme = true, dynamicColor = false) {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val formData by viewModel.formData.collectAsState()
        val fieldErrors by viewModel.fieldErrors.collectAsStateWithLifecycle()

        var showConfirmDialog by remember { mutableStateOf(false) }

        val snackbarHostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()

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
                    text = "Registrar Nuevo Beneficiario",
                    style = MaterialTheme.typography.headlineLarge,
                    color = White,
                    modifier = Modifier.padding(bottom = 16.dp),
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                /** Nombre */
                StandardInput(
                    label = "Nombre",
                    placeholder = "Ej. Juan",
                    value = formData.nombre,
                    onValueChange = { viewModel.updateFormData { copy(nombre = it) } },
                    isError = fieldErrors["nombre"] == true,
                    errorMessage = null
                )

                Spacer(modifier = Modifier.height(12.dp))

                /** Apellido Paterno */
                StandardInput(
                    label = "Apellido Paterno",
                    placeholder = "Ej. García",
                    value = formData.apellidoPaterno,
                    onValueChange = { viewModel.updateFormData { copy(apellidoPaterno = it) } },
                    isError = fieldErrors["apellidoPaterno"] == true,
                    errorMessage = null
                )

                Spacer(modifier = Modifier.height(12.dp))

                /** Apellido Materno */
                StandardInput(
                    label = "Apellido Materno",
                    placeholder = "Ej. López",
                    value = formData.apellidoMaterno,
                    onValueChange = { viewModel.updateFormData { copy(apellidoMaterno = it) } },
                    isError = fieldErrors["apellidoMaterno"] == true,
                    errorMessage = null
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    /** Fecha de Nacimiento */
                    StandardIconInput(
                        label = "Fecha de Nacimiento",
                        placeholder = "Ej. 2000-01-15",
                        value = formData.fechaNacimiento,
                        onValueChange = { viewModel.updateFormData { copy(fechaNacimiento = it) } },
                        isError = fieldErrors["fechaNacimiento"] == true,
                        errorMessage = null,
                        trailingIcon = {
                            CalendarIcon(tint = MaterialTheme.colorScheme.onSurface)
                        },
                        modifier = Modifier.weight(1f)
                    )

                    /** Fecha de Ingreso */
                    StandardIconInput(
                        label = "Fecha de Ingreso",
                        placeholder = "Ej. 2024-01-15",
                        value = formData.fechaIngreso,
                        onValueChange = { viewModel.updateFormData { copy(fechaIngreso = it) } },
                        isError = fieldErrors["fechaIngreso"] == true,
                        errorMessage = null,
                        trailingIcon = {
                            CalendarIcon(tint = MaterialTheme.colorScheme.onSurface)
                        },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                /** Número de Emergencia */
                StandardInput(
                    label = "Número de Emergencia",
                    placeholder = "Ej. 4421234567",
                    value = formData.numeroEmergencia,
                    onValueChange = { viewModel.updateFormData { copy(numeroEmergencia = it) } },
                    isError = fieldErrors["numeroEmergencia"] == true,
                    errorMessage = null
                )

                Spacer(modifier = Modifier.height(12.dp))

                /** Nombre de Contacto de Emergencia */
                StandardInput(
                    label = "Nombre del Contacto de Emergencia",
                    placeholder = "Ej. María García",
                    value = formData.nombreContactoEmergencia,
                    onValueChange = { viewModel.updateFormData { copy(nombreContactoEmergencia = it) } },
                    isError = fieldErrors["nombreContactoEmergencia"] == true,
                    errorMessage = null
                )

                Spacer(modifier = Modifier.height(12.dp))

                /** Relación de Contacto de Emergencia */
                StandardInput(
                    label = "Relación con Contacto de Emergencia",
                    placeholder = "Ej. Madre",
                    value = formData.relacionContactoEmergencia,
                    onValueChange = { viewModel.updateFormData { copy(relacionContactoEmergencia = it) } },
                    isError = fieldErrors["relacionContactoEmergencia"] == true,
                    errorMessage = null
                )

                Spacer(modifier = Modifier.height(12.dp))

                /** Discapacidad */
                StandardInput(
                    label = "Discapacidad",
                    placeholder = "Ej. Ninguna o especificar",
                    value = formData.discapacidad,
                    onValueChange = { viewModel.updateFormData { copy(discapacidad = it) } },
                    isError = fieldErrors["discapacidad"] == true,
                    errorMessage = null
                )

                Spacer(modifier = Modifier.height(12.dp))

                /** Descripción */
                DescriptionInput(
                    label = "Descripción",
                    placeholder = "Escribe información adicional del beneficiario...",
                    value = formData.descripcion,
                    onValueChange = { viewModel.updateFormData { copy(descripcion = it) } },
                    isError = fieldErrors["descripcion"] == true,
                    errorMessage = null
                )

                Spacer(modifier = Modifier.height(12.dp))

                /** Upload image */
                var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
                ImageUploadInput(
                    label = "Foto del beneficiario",
                    value = selectedImageUri,
                    onValueChange = { uri ->
                        selectedImageUri = uri
                        viewModel.updateFormData { copy(foto = uri?.toString().orEmpty()) }
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
                        onClick = { navController.navigate(Screen.BeneficiaryList.route) }
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
                                viewModel.addNewBeneficiary()
                            },
                            dialogTitle = "Confirmar registro",
                            dialogText = "¿Deseas registrar este beneficiario? Asegúrate de que todos los datos sean correctos.",
                            confirmText = "Confirmar",
                            dismissText = "Cancelar"
                        )
                    }

                    LaunchedEffect(uiState.isSuccess) {
                        if (uiState.isSuccess) {
                            scope.launch {
                                snackbarHostState.showSnackbar("Beneficiario registrado correctamente")
                            }

                            viewModel.resetForm()
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