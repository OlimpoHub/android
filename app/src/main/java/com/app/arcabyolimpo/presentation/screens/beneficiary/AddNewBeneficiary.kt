package com.app.arcabyolimpo.presentation.screens.beneficiary

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
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.StandardInput
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.SelectInput
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.ImageUploadInput
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.DateInput
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.SaveButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.CancelButton
import com.app.arcabyolimpo.ui.theme.Background
import com.app.arcabyolimpo.ui.theme.White
import com.app.arcabyolimpo.ui.theme.ArcaByOlimpoTheme
import com.app.arcabyolimpo.presentation.ui.components.atoms.alerts.DecisionDialog
import com.app.arcabyolimpo.presentation.ui.components.atoms.alerts.Snackbarcustom
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.DescriptionInput
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewBeneficiaryScreen(
    navController: NavHostController,
    viewModel: AddNewBeneficiaryViewModel = hiltViewModel(),
    onSuccess: (() -> Unit)? = null
) {
    ArcaByOlimpoTheme(darkTheme = true, dynamicColor = false) {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val formData by viewModel.formData.collectAsState()
        val disabilities by viewModel.disabilities.collectAsStateWithLifecycle()
        val fieldErrors by viewModel.fieldErrors.collectAsStateWithLifecycle()

        var showConfirmDialog by remember { mutableStateOf(false) }

        val snackbarHostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()

        // Calcular fechas límite
        val maxDateMillis = remember { Calendar.getInstance(TimeZone.getTimeZone("UTC")).timeInMillis }
        val minBirthDateMillis = remember {
            Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
                add(Calendar.YEAR, -100) // Máximo 100 años atrás
            }.timeInMillis
        }

        LaunchedEffect(Unit) {
            viewModel.loadDisabilities()
        }

        Scaffold(
            containerColor = Background,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Registrar Beneficiario",
                            style = MaterialTheme.typography.headlineLarge,
                            color = White,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Regresar",
                                tint = White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Background
                    )
                )
            },
            snackbarHost = {
                SnackbarHost(snackbarHostState) { data ->
                    Snackbarcustom(
                        title = data.visuals.message,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            },
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

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    /** Upload image - Lado izquierdo */
                    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
                    ImageUploadInput(
                        label = "",
                        value = selectedImageUri,
                        onValueChange = { uri ->
                            selectedImageUri = uri
                            viewModel.updateFormData { copy(foto = uri?.toString().orEmpty()) }
                        },
                        isError = false,
                        errorMessage = null,
                        modifier = Modifier.weight(0.4f)
                    )

                    /** Columna derecha con los primeros campos */
                    Column(
                        modifier = Modifier.weight(0.6f),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        /** Nombre del beneficiario */
                        StandardInput(
                            label = "Nombre del beneficiario:",
                            placeholder = "",
                            value = formData.nombre,
                            onValueChange = { viewModel.updateFormData { copy(nombre = it) } },
                            isError = fieldErrors["nombre"] != null,
                            errorMessage = fieldErrors["nombre"]
                        )

                        /** Apellido Paterno */
                        StandardInput(
                            label = "Apellido Paterno:",
                            placeholder = "",
                            value = formData.apellidoPaterno,
                            onValueChange = { viewModel.updateFormData { copy(apellidoPaterno = it) } },
                            isError = fieldErrors["apellidoPaterno"] != null,
                            errorMessage = fieldErrors["apellidoPaterno"]
                        )

                        /** Apellido Materno */
                        StandardInput(
                            label = "Apellido Materno:",
                            placeholder = "",
                            value = formData.apellidoMaterno,
                            onValueChange = { viewModel.updateFormData { copy(apellidoMaterno = it) } },
                            isError = fieldErrors["apellidoMaterno"] != null,
                            errorMessage = fieldErrors["apellidoMaterno"]
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    /** Fecha de Nacimiento - CON DATEINPUT */
                    DateInput(
                        label = "Fecha de Nacimiento:",
                        placeholder = "YYYY-MM-DD",
                        value = formData.fechaNacimiento,
                        onValueChange = { viewModel.updateFormData { copy(fechaNacimiento = it) } },
                        isError = fieldErrors["fechaNacimiento"] != null,
                        errorMessage = if (fieldErrors["fechaNacimiento"] != null) "La fecha es un campo obligatorio" else "",
                        modifier = Modifier.weight(1f),
                        minDateUtcMillis = minBirthDateMillis,
                        maxDateUtcMillis = maxDateMillis
                    )

                    /** Relación del tutor */
                    StandardInput(
                        label = "Relacion del tutor:",
                        placeholder = "",
                        value = formData.relacionContactoEmergencia,
                        onValueChange = { viewModel.updateFormData { copy(relacionContactoEmergencia = it) } },
                        isError = fieldErrors["relacionContactoEmergencia"] != null,
                        errorMessage = fieldErrors["relacionContactoEmergencia"],
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    /** Ingreso (Fecha de Ingreso) - CON DATEINPUT */
                    DateInput(
                        label = "Ingreso:",
                        placeholder = "YYYY-MM-DD",
                        value = formData.fechaIngreso,
                        onValueChange = { viewModel.updateFormData { copy(fechaIngreso = it) } },
                        isError = fieldErrors["fechaIngreso"] != null,
                        errorMessage = if (fieldErrors["fechaIngreso"] != null) "La fecha es un campo obligatorio" else "",
                        modifier = Modifier.weight(1f),
                        maxDateUtcMillis = maxDateMillis
                    )

                    /** Número de teléfono */
                    StandardInput(
                        label = "Numero de telefono:",
                        placeholder = "",
                        value = formData.numeroEmergencia,
                        onValueChange = { viewModel.updateFormData { copy(numeroEmergencia = it) } },
                        isError = fieldErrors["numeroEmergencia"] != null,
                        errorMessage = fieldErrors["numeroEmergencia"],
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                /** Nombre de Contacto de Emergencia */
                StandardInput(
                    label = "Nombre del Contacto de Emergencia:",
                    placeholder = "",
                    value = formData.nombreContactoEmergencia,
                    onValueChange = { viewModel.updateFormData { copy(nombreContactoEmergencia = it) } },
                    isError = fieldErrors["nombreContactoEmergencia"] != null,
                    errorMessage = fieldErrors["nombreContactoEmergencia"],
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                /** Discapacidades - Selector desde base de datos */
                SelectInput(
                    label = "Discapacidades:",
                    selectedOption = disabilities.firstOrNull { formData.disabilities.contains(it.id) }?.name
                        ?: "Seleccionar",
                    options = disabilities.map { it.name },
                    onOptionSelected = { selectedNombre ->
                        val selectedDisability = disabilities.find { it.name == selectedNombre }
                        viewModel.updateFormData {
                            copy(disabilities = listOfNotNull(selectedDisability?.id))
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    isError = fieldErrors["discapacidad"] != null,
                    errorMessage = fieldErrors["discapacidad"]
                )

                Spacer(modifier = Modifier.height(12.dp))

                /** Descripción */
                DescriptionInput(
                    label = "Descripción:",
                    placeholder = "",
                    value = formData.descripcion,
                    onValueChange = { viewModel.updateFormData { copy(descripcion = it) } },
                    isError = fieldErrors["descripcion"] != null,
                    errorMessage = fieldErrors["descripcion"],
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    /** Cancel */
                    CancelButton(
                        modifier = Modifier.size(width = 112.dp, height = 40.dp),
                        onClick = { navController.popBackStack() }
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    /** Save - Guardar */
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
                                delay(100)
                            }
                            viewModel.resetForm()
                            navController.previousBackStackEntry
                                ?.savedStateHandle
                                ?.set("success_message", "Beneficiario creado correctamente")

                            navController.popBackStack()
                        }
                    }

                    LaunchedEffect(uiState.error) {
                        if (uiState.error != null) {
                            scope.launch {
                                delay(100)
                            }
                            navController.previousBackStackEntry
                                ?.savedStateHandle
                                ?.set("error_message", uiState.error)

                            navController.popBackStack()
                        }
                    }
                }

                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}