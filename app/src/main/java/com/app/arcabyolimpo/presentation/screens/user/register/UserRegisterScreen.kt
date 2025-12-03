package com.app.arcabyolimpo.presentation.screens.user.register

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.SaveButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.CancelButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.ExitIcon
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.ModalInput
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.ImageUploadInput
import androidx.compose.foundation.clickable
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.CalendarIcon
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.rememberCoroutineScope
import com.app.arcabyolimpo.presentation.ui.components.atoms.alerts.DecisionDialog
import com.app.arcabyolimpo.presentation.ui.components.atoms.alerts.SnackbarVisualsWithError
import com.app.arcabyolimpo.presentation.ui.components.atoms.alerts.Snackbarcustom
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserRegisterScreen(
    viewModel: UserRegisterViewModel = hiltViewModel(),
    onDismiss: () -> Unit,
    onSuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    // COLLECTED STATE FROM VIEWMODEL (for image preview)
    val selectedImageUri by viewModel.selectedImageUri.collectAsState()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val isActiveBoolean = uiState.isActive == 1

    // Snackbar state
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(uiState.success, uiState.error) {
        if (uiState.success) {
            uiState.successMessage?.let { message ->
                snackbarHostState.showSnackbar(message)
            }
        } else if (uiState.error != null) {
            uiState.error?.let { errorMessage ->
                // Usamos el mensaje de error directamente
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = errorMessage,
                        withDismissAction = true,
                        duration = SnackbarDuration.Long
                    )
                }
            }
        }
    }

    //State to show/hide confirmation dialog
    var showConfirmDialog by remember { mutableStateOf(false) }

    // Date picker state
    val datePickerState = rememberDatePickerState()
    var showDatePicker by remember { mutableStateOf(false) }

    // Show success message
    LaunchedEffect(uiState.success) {
        if (uiState.success) {
            kotlinx.coroutines.delay(3000)
            onSuccess()
            viewModel.resetState()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.resetState()
        }
    }

    //Show confirmation dialog
    if (showConfirmDialog) {
        DecisionDialog(
            onDismissRequest = { showConfirmDialog = false },
            onConfirmation = {
                showConfirmDialog = false
                viewModel.registerCollab()
            },
            dialogTitle = "¿Estás seguro?",
            dialogText = "¿Deseas registrar este usuario?",
            confirmText = "Confirmar",
            dismissText = "Cancelar"
        )
    }

    // Date Picker Dialog
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val date = java.time.Instant.ofEpochMilli(millis)
                                .atZone(java.time.ZoneId.systemDefault())
                                .toLocalDate()
                            viewModel.updateBirthDate(date.toString())
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("OK", color = Color(0xFF3B82F6))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancelar", color = Color.Gray)
                }
            },
            colors = DatePickerDefaults.colors(
                containerColor = Color(0xFF1A1F2E)
            )
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    containerColor = Color(0xFF1A1F2E),
                    titleContentColor = Color.White,
                    headlineContentColor = Color.White,
                    weekdayContentColor = Color.White,
                    subheadContentColor = Color.White,
                    yearContentColor = Color.White,
                    currentYearContentColor = Color(0xFF3B82F6),
                    selectedYearContentColor = Color.White,
                    selectedYearContainerColor = Color(0xFF3B82F6),
                    dayContentColor = Color.White,
                    selectedDayContentColor = Color.White,
                    selectedDayContainerColor = Color(0xFF3B82F6),
                    todayContentColor = Color(0xFF3B82F6),
                    todayDateBorderColor = Color(0xFF3B82F6)
                )
            )
        }
    }

    ModalBottomSheet(
        onDismissRequest = {
            viewModel.resetState()
            onDismiss()
        },
        sheetState = sheetState,
        containerColor = Color(0xFF040610),
        contentColor = Color.White,
        dragHandle = null
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Registrar Usuario",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    IconButton(onClick = {
                        viewModel.resetState()
                        onDismiss()
                    }) {
                        ExitIcon(tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Form content
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = false)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Image Upload
                    ImageUploadInput(
                        label = "Foto de Perfil",
                        value = selectedImageUri,
                        onValueChange = { uri ->
                            viewModel.setSelectedImageUri(uri)
                        },
                        height = 200.dp
                    )

                    // Role Selection
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Tipo de Usuario *",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Asistente Button (Role ID = 2)
                            OutlinedButton(
                                onClick = { viewModel.updateRoleId("2") },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (uiState.roleId == "2") Color(0xFF3B82F6) else Color.Transparent,
                                    contentColor = if (uiState.roleId == "2") Color.White else Color(0xFF3B82F6)
                                ),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (uiState.roleId == "2") Color(0xFF3B82F6) else Color(0xFF3B82F6).copy(alpha = 0.5f)
                                )
                            ) {
                                Text("Asistente")
                            }

                            // Voluntario Button (Role ID = 3)
                            OutlinedButton(
                                onClick = { viewModel.updateRoleId("3") },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (uiState.roleId == "3") Color(0xFF3B82F6) else Color.Transparent,
                                    contentColor = if (uiState.roleId == "3") Color.White else Color(0xFF3B82F6)
                                ),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (uiState.roleId == "3") Color(0xFF3B82F6) else Color(0xFF3B82F6).copy(alpha = 0.5f)
                                )
                            ) {
                                Text("Voluntario")
                            }
                        }
                    }

                    ModalInput(
                        label = "Nombre *",
                        value = uiState.firstName,
                        onValueChange = { viewModel.updateFirstName(it) },
                        placeholder = "Ingresa el nombre",
                        isError = uiState.firstNameError != null,
                        errorMessage = uiState.firstNameError
                    )

                    ModalInput(
                        label = "Apellido Paterno *",
                        value = uiState.lastName,
                        onValueChange = { viewModel.updateLastName(it) },
                        placeholder = "Ingresa el apellido paterno",
                        isError = uiState.lastNameError != null,
                        errorMessage = uiState.lastNameError
                    )

                    ModalInput(
                        label = "Apellido Materno",
                        value = uiState.secondLastName,
                        onValueChange = { viewModel.updateSecondLastName(it) },
                        placeholder = "Ingresa el apellido materno"
                    )

                    ModalInput(
                        label = "Correo Electrónico *",
                        value = uiState.email,
                        onValueChange = { viewModel.updateEmail(it) },
                        placeholder = "ejemplo@correo.com",
                        isError = uiState.emailError != null,
                        errorMessage = uiState.emailError
                    )

                    ModalInput(
                        label = "Teléfono *",
                        value = uiState.phone,
                        onValueChange = { viewModel.updatePhone(it) },
                        placeholder = "1234567890",
                        isError = uiState.phoneError != null,
                        errorMessage = uiState.phoneError
                    )

                    // Date of Birth with Calendar Picker
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showDatePicker = true }
                    ) {
                        ModalInput(
                            label = "Fecha de Nacimiento *",
                            value = uiState.birthDate,
                            onValueChange = { viewModel.updateBirthDate(it) },
                            placeholder = "YYYY-MM-DD",
                        )

                        // Calendar icon overlay
                        IconButton(
                            onClick = { showDatePicker = true },
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .padding(end = 8.dp, top = 32.dp)
                        ) {
                            CalendarIcon(tint = Color(0xFF3B82F6))
                        }
                    }

                    ModalInput(
                        label = "Carrera",
                        value = uiState.degree,
                        onValueChange = { viewModel.updateDegree(it) },
                        placeholder = "Ingeniería en..."
                    )

                    // Document Checkboxes
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Documentos",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.updateHasReglamentoInterno(!uiState.hasReglamentoInterno) }
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = uiState.hasReglamentoInterno,
                                onCheckedChange = { viewModel.updateHasReglamentoInterno(it) },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = Color(0xFF3B82F6),
                                    uncheckedColor = Color(0xFF3B82F6).copy(alpha = 0.5f),
                                    checkmarkColor = Color.White
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Reglamento Interno",
                                color = Color.White,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.updateHasCopiaIne(!uiState.hasCopiaIne) }
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = uiState.hasCopiaIne,
                                onCheckedChange = { viewModel.updateHasCopiaIne(it) },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = Color(0xFF3B82F6),
                                    uncheckedColor = Color(0xFF3B82F6).copy(alpha = 0.5f),
                                    checkmarkColor = Color.White
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Copia de INE",
                                color = Color.White,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.updateHasAvisoConfidencialidad(!uiState.hasAvisoConfidencialidad) }
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = uiState.hasAvisoConfidencialidad,
                                onCheckedChange = { viewModel.updateHasAvisoConfidencialidad(it) },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = Color(0xFF3B82F6),
                                    uncheckedColor = Color(0xFF3B82F6).copy(alpha = 0.5f),
                                    checkmarkColor = Color.White
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Aviso de Confidencialidad",
                                color = Color.White,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }

                    // Status Toggle
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Estatus",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Active Button
                            OutlinedButton(
                                onClick = { viewModel.updateIsActive(true) },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (isActiveBoolean) Color(0xFF3B82F6) else Color.Transparent,
                                    contentColor = if (isActiveBoolean) Color.White else Color(0xFF3B82F6)
                                ),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isActiveBoolean) Color(0xFF3B82F6) else Color(0xFF3B82F6).copy(alpha = 0.5f)
                                )
                            ) {
                                Text("Activo")
                            }

                            // Inactive Button
                            OutlinedButton(
                                onClick = { viewModel.updateIsActive(false) },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (!isActiveBoolean) Color(0xFF3B82F6) else Color.Transparent,
                                    contentColor = if (!isActiveBoolean) Color.White else Color(0xFF3B82F6)
                                ),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (!isActiveBoolean) Color(0xFF3B82F6) else Color(0xFF3B82F6).copy(alpha = 0.5f)
                                )
                            ) {
                                Text("Inactivo")
                            }
                        }
                    }

                }

                Spacer(modifier = Modifier.height(24.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        CancelButton(
                            onClick = {
                                viewModel.resetState()
                                onDismiss()
                            }
                        )
                    }

                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        SaveButton(
                            onClick = {
                                if (viewModel.validateForm()) {
                                    showConfirmDialog = true
                                }
                            }

                        )
                    }
                }

                if (uiState.isLoading) {
                    Spacer(modifier = Modifier.height(16.dp))
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(top = 16.dp)
            ) { data ->
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Snackbarcustom(
                        title = data.visuals.message,
                        modifier =
                            Modifier
                                .fillMaxWidth(0.85f),
                        ifSucces = uiState.success,
                    )
                }
            }
        }
    }
}