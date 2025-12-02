package com.app.arcabyolimpo.presentation.screens.user.updateuser

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
import com.app.arcabyolimpo.presentation.ui.components.atoms.alerts.SnackbarArca
import com.app.arcabyolimpo.presentation.ui.components.atoms.alerts.SnackbarVisualsWithError
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateUserScreen(
    viewModel: UpdateUserViewModel = hiltViewModel(),
    onDismiss: () -> Unit,
    onSuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Image state
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    // Snackbar state
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Helper function to show snackbar
    fun showSnackbar(message: String, isError: Boolean) {
        scope.launch {
            snackbarHostState.showSnackbar(
                SnackbarVisualsWithError(
                    message = message,
                    isError = isError
                )
            )
        }
    }

    // Add this after your other LaunchedEffects in UpdateUserScreen
    LaunchedEffect(uiState.photoUrl) {
        if (!uiState.photoUrl.isNullOrEmpty() && selectedImageUri == null) {
            selectedImageUri = Uri.parse(uiState.photoUrl)
        }
    }

    //State to show/hide confirmation dialog
    var showConfirmDialog by remember { mutableStateOf(false) }

    // Date picker state
    val datePickerState = rememberDatePickerState()
    var showDatePicker by remember { mutableStateOf(false) }

    // Show error snackbar
    LaunchedEffect(uiState.error) {
        uiState.error?.let { errorMessage ->
            showSnackbar(errorMessage, isError = true)
        }
    }

    // Show success snackbar
    LaunchedEffect(uiState.successMessage) {
        uiState.successMessage?.let { successMessage ->
            showSnackbar(successMessage, isError = false)
        }
    }

    // Cuando el update fue exitoso avisamos al padre y limpiamos el estado
    LaunchedEffect(uiState.success) {
        if (uiState.success) {
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
                viewModel.updateUser() // Actually update when confirmed
            },
            dialogTitle = "¿Estás seguro?",
            dialogText = "¿Deseas guardar los cambios?",
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
                        text = "Modificar Usuario",
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
                    // Image Upload - Added at the top of the form
                    ImageUploadInput(
                        label = "Foto de Perfil",
                        value = selectedImageUri,
                        onValueChange = { uri ->
                            selectedImageUri = uri
                            viewModel.updateProfileImage(uri)
                        },
                        height = 200.dp
                    )

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
                                onClick = { viewModel.updateIsActive(1) },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (uiState.isActive == 1) Color(0xFF3B82F6) else Color.Transparent,
                                    contentColor = if (uiState.isActive == 1) Color.White else Color(0xFF3B82F6)
                                ),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (uiState.isActive == 1) Color(0xFF3B82F6) else Color(0xFF3B82F6).copy(alpha = 0.5f)
                                )
                            ) {
                                Text("Activo")
                            }

                            // Inactive Button
                            OutlinedButton(
                                onClick = { viewModel.updateIsActive(0) },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (uiState.isActive == 0) Color(0xFF3B82F6) else Color.Transparent,
                                    contentColor = if (uiState.isActive == 0) Color.White else Color(0xFF3B82F6)
                                ),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (uiState.isActive == 0) Color(0xFF3B82F6) else Color(0xFF3B82F6).copy(alpha = 0.5f)
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

            // Snackbar positioned at the top of the BottomSheet
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 16.dp)
            ) { data ->
                val isError = (data.visuals as? SnackbarVisualsWithError)?.isError ?: false

                Snackbar(
                    modifier = Modifier
                        .padding(16.dp)
                        .border(
                            width = 1.dp,
                            color = if (isError) Color(0xFFEF4444) else Color(0xFF10B981),
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
                        ),
                    containerColor = if (isError) Color(0xFF7F1D1D) else Color(0xFF065F46),
                    contentColor = Color.White,
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = data.visuals.message,
                        color = Color.White
                    )
                }
            }
        }
    }
}