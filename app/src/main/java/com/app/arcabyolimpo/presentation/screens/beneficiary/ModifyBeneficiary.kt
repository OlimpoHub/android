@file:Suppress("ktlint:standard:no-wildcard-imports")
package com.app.arcabyolimpo.presentation.screens.beneficiary


/**
 * Composable screen that displays the registrations of updates for a beneficiary.
 *
 * This screen is responsible for showing a forms to modify beneficiaries
 *  retrieved from the [ModifyBeneficiaryViewModel]. It provides:
 * - Each field required to modify the beneficiary, with the previous data.
 * - A button to cancel the forms an return to [BeneficiaryDetail].
 * - A button to save all the fields and send it to the data base.
 *
 * @param navController The way to go through different screens that are in the [NavGraph]
 * @param viewModel The [ModifyBeneficiary] used to manage the UI state.
 * @param beneficiaryID The id of the beneficiary that is being changed
 */

import android.net.Uri
import android.util.Log
import android.util.MutableBoolean
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.app.arcabyolimpo.presentation.screens.session.SessionViewModel
import com.app.arcabyolimpo.presentation.ui.components.atoms.alerts.DecisionDialog
import com.app.arcabyolimpo.presentation.ui.components.atoms.alerts.Snackbarcustom
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.CancelButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.SaveButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.DateInput
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.DescriptionInput
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.ImageUploadInput
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.MultiSelectInput
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.StandardInput
import com.app.arcabyolimpo.ui.theme.ArcaByOlimpoTheme
import com.app.arcabyolimpo.ui.theme.Background
import com.app.arcabyolimpo.ui.theme.White
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModifyBeneficiaryScreen(
    navController: NavHostController,
    viewModel: ModifyBeneficiaryViewModel = hiltViewModel(),
    beneficiaryId: String,
    sessionViewModel: SessionViewModel = hiltViewModel(),
){
    val role by sessionViewModel.role.collectAsState()

    ArcaByOlimpoTheme(darkTheme = true, dynamicColor = false) {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val isLoading by viewModel.isLoading.collectAsState()
        val formData by viewModel.formData.collectAsState()
        val disabilities by viewModel.disabilities.collectAsStateWithLifecycle()
        val fieldErrors by viewModel.fieldErrors.collectAsStateWithLifecycle()

        var showConfirmDialog by remember { mutableStateOf(false) }
        var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

        val snackbarHostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()

        LaunchedEffect(Unit) {
            viewModel.loadBeneficiary(beneficiaryId)
            viewModel.loadDisabilities()
        }

        LaunchedEffect(formData.foto) {
            if (formData.foto.isNotEmpty() && selectedImageUri == null) {
                try {
                    selectedImageUri = Uri.parse(formData.foto)
                } catch (e: Exception) {
                    Log.e("ModifyBeneficiary", "Error parsing image URI: ${e.message}")
                }
            }
        }

        if (disabilities.isNotEmpty()) {
            val currentDisabilities = formData.disabilities
            val convertedIds = currentDisabilities.mapNotNull { nameOrId ->
                disabilities.find { it.name == nameOrId }?.id
            }

            if (convertedIds.isNotEmpty() && convertedIds != currentDisabilities) {
                viewModel.updateFormData {
                    copy(disabilities = convertedIds)
                }
            }
        }

        Scaffold(
            containerColor = Background,
            topBar = {
                ModifyBeneficiaryTopBar(
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
                // Loading
                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Cargando datos del beneficiario...",
                            color = White,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                } else {
                    // Beneficiary's name
                    StandardInput(
                        label = "Nombre",
                        placeholder = "Ej. Juan",
                        value = formData.nombre,
                        onValueChange = { viewModel.updateFormData { copy(nombre = it) } },
                        isError = fieldErrors["name"] == true,
                        errorMessage = if (fieldErrors["name"] == true) "Nombre inválido" else "",
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Beneficiary's paternal lastname
                    StandardInput(
                        label = "Apellido Paterno",
                        placeholder = "Ej. Pérez",
                        value = formData.apellidoPaterno,
                        onValueChange = { viewModel.updateFormData { copy(apellidoPaterno = it) } },
                        isError = fieldErrors["apellidoPaterno"] == true,
                        errorMessage = if (fieldErrors["apellidoPaterno"] == true) "Apellido Paterno inválido" else "",
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Beneficiary's maternal lastname
                    StandardInput(
                        label = "Apellido Materno",
                        placeholder = "Ej. López",
                        value = formData.apellidoMaterno,
                        onValueChange = { viewModel.updateFormData { copy(apellidoMaterno = it) } },
                        isError = fieldErrors["apellidoMaterno"] == true,
                        errorMessage = if (fieldErrors["apellidoMaterno"] == true) "Apellido Materno inválido" else "",
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Beneficiary's birthdate with adequate date format
                    DateInput(
                        label = "Fecha de nacimiento",
                        placeholder = "dd/MM/yyyy",
                        value = formData.fechaNacimiento,
                        onValueChange = { viewModel.updateFormData { copy(fechaNacimiento = it) } },
                        isError = fieldErrors["fechaNacimiento"] == true,
                        errorMessage = if (fieldErrors["fechaNacimiento"] == true) "Fecha de nacimiento inválida, debe ser dd/MM/yyyy" else "",
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Beneficiary's emergency contact number
                    StandardInput(
                        label = "Número de Emergencia",
                        placeholder = "Ej. 0123456789",
                        value = formData.numeroEmergencia,
                        onValueChange = { viewModel.updateFormData { copy(numeroEmergencia = it) } },
                        isError = fieldErrors["numeroEmergencia"] == true,
                        errorMessage = if (fieldErrors["numeroEmergencia"] == true) "Número de emergencia inválido" else "",
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Beneficiary's emergency's contact name
                    StandardInput(
                        label = "Nombre de Contacto de Emergencia",
                        placeholder = "Ej. Juan Pérez",
                        value = formData.nombreContactoEmergencia,
                        onValueChange = { viewModel.updateFormData { copy(nombreContactoEmergencia = it) } },
                        isError = fieldErrors["nombreContactoEmergencia"] == true,
                        errorMessage = if (fieldErrors["nombreContactoEmergencia"] == true) "Nombre de contacto de emergencia inválido" else "",
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Beneficiary's relation with their emergency contact
                    StandardInput(
                        label = "Relacion de Contacto de Emergencia",
                        placeholder = "Ej. Padre",
                        value = formData.relacionContactoEmergencia,
                        onValueChange = { viewModel.updateFormData { copy(relacionContactoEmergencia = it) } },
                        isError = fieldErrors["relacionContactoEmergencia"] == true,
                        errorMessage = if (fieldErrors["relacionContactoEmergencia"] == true) "Relacion de contacto de emergencia inválida" else "",
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Beneficiary's description
                    DescriptionInput(
                        label = "Descripción",
                        placeholder = "Escribe una breve descripción del beneficiario",
                        value = formData.descripcion,
                        onValueChange = { viewModel.updateFormData { copy(descripcion = it) } },
                        isError = fieldErrors["descripcion"] == true,
                        errorMessage = if (fieldErrors["descripcion"] == true) "Descripción requerida" else "",
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Beneficiary's date of admission with adequate date format
                    DateInput(
                        label = "Fecha de ingreso",
                        placeholder = "dd/MM/yyyy",
                        value = formData.fechaIngreso,
                        onValueChange = { viewModel.updateFormData { copy(fechaIngreso = it) } },
                        isError = fieldErrors["fechaIngreso"] == true,
                        errorMessage = if (fieldErrors["fechaIngreso"] == true) "Fecha de ingreso inválida, debe ser dd/MM/yyyy" else "",
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Beneficiary's disability
                    MultiSelectInput(
                        label = "Discapacidad",
                        selectedOptions = formData.disabilities.mapNotNull { disabilityId ->
                            disabilities.find { it.id == disabilityId || it.name == disabilityId }?.name
                        },
                        options = disabilities.map { it.name },
                        onOptionsSelected = { selectedNames ->
                            val selectedIds = selectedNames.mapNotNull { name ->
                                disabilities.find { it.name == name }?.id
                            }
                            viewModel.updateFormData {
                                copy(disabilities = selectedIds)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        isError = fieldErrors["discapacidad"] == true,
                        errorMessage = if (fieldErrors["discapacidad"] == true) "Discapacidad requerida" else "",
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Image upload
                    ImageUploadInput(
                        label = "Foto del beneficiario",
                        value = selectedImageUri,
                        onValueChange = { uri ->
                            selectedImageUri = uri
                            viewModel.setSelectedImageUri(uri)
                                        },
                        isError = fieldErrors["foto"] == true,
                        errorMessage = if (fieldErrors["foto"] == true) "Foto requerida" else "",
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    if (role == "COORDINADOR") {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Active Button
                            OutlinedButton(
                                onClick = { viewModel.updateFormData { copy(estatus = 1) } },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (formData.estatus == 1) Color(0xFF3B82F6) else Color.Transparent,
                                    contentColor = if (formData.estatus == 1) Color.White else Color(
                                        0xFF3B82F6
                                    )
                                ),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (formData.estatus == 1) Color(0xFF3B82F6) else Color(
                                        0xFF3B82F6
                                    ).copy(
                                        alpha = 0.5f
                                    )
                                )
                            ) {
                                Text("Activo")
                            }

                            // Inactive Button
                            OutlinedButton(
                                onClick = { viewModel.updateFormData { copy(estatus = 0) } },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (formData.estatus == 0) Color(0xFF3B82F6) else Color.Transparent,
                                    contentColor = if (formData.estatus == 0) Color.White else Color(
                                        0xFF3B82F6
                                    )
                                ),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (formData.estatus == 0) Color(0xFF3B82F6) else Color(
                                        0xFF3B82F6
                                    ).copy(
                                        alpha = 0.5f
                                    )
                                )
                            ) {
                                Text("Inactivo")
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        // Cancel
                        CancelButton(
                            modifier =
                                Modifier
                                    .size(width = 112.dp, height = 40.dp),
                            onClick = { navController.popBackStack() },
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        // Save
                        SaveButton(
                            onClick = {
                                if (disabilities.isEmpty()) {
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Lista de discapacidades vacía")
                                    }
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
                                        snackbarHostState.showSnackbar("Modificación cancelada")
                                    }
                                },
                                onConfirmation = {
                                    showConfirmDialog = false
                                    viewModel.modifyBeneficiary(beneficiaryId)
                                },
                                dialogTitle = "Confirmar modificación",
                                dialogText = "¿Deseas guardar los cambios en este a los datos delbeneficiario? Asegúrate de que todos los datos sean correctos.",
                                confirmText = "Confirmar",
                                dismissText = "Cancelar",
                            )
                        }

                        LaunchedEffect(uiState.isSuccess) {
                            if (uiState.isSuccess) {
                                viewModel.resetForm()
                                navController.previousBackStackEntry
                                    ?.savedStateHandle
                                    ?.set("snackbarMessage", "Beneficiario modificado correctamente")
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModifyBeneficiaryTopBar(
    onBackClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = "Modificar Beneficiario",
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