package com.app.arcabyolimpo.presentation.screens.ExternalCollab.RegisterExternalCollab

import androidx.compose.foundation.clickable
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
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.CancelButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.SaveButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.CalendarIcon
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.ExitIcon
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.ModalInput

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExternalCollabRegisterScreen(
    viewModel: ExternalCollabRegisterViewModel = hiltViewModel(),
    onDismiss: () -> Unit,
    onSuccess: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Date picker state
    val datePickerState = rememberDatePickerState()
    var showDatePicker by remember { mutableStateOf(false) }

    // Show success message
    LaunchedEffect(uiState.success) {
        if (uiState.success) {
            kotlinx.coroutines.delay(1500)
            viewModel.resetState()
            onSuccess()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.resetState()
        }
    }

    // Date Picker Dialog
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val date =
                                java.time.Instant
                                    .ofEpochMilli(millis)
                                    .atZone(java.time.ZoneId.systemDefault())
                                    .toLocalDate()
                            viewModel.updateBirthDate(date.toString())
                        }
                        showDatePicker = false
                    },
                ) {
                    Text("OK", color = Color(0xFF3B82F6))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancelar", color = Color.Gray)
                }
            },
            colors =
                DatePickerDefaults.colors(
                    containerColor = Color(0xFF1A1F2E),
                ),
        ) {
            DatePicker(
                state = datePickerState,
                colors =
                    DatePickerDefaults.colors(
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
                        todayDateBorderColor = Color(0xFF3B82F6),
                    ),
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
        dragHandle = null,
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Registrar Colaborador",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
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
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = false)
                        .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                ModalInput(
                    label = "Nombre *",
                    value = uiState.firstName,
                    onValueChange = { viewModel.updateFirstName(it) },
                    placeholder = "Ingresa el nombre",
                    isError = uiState.firstNameError != null,
                    errorMessage = uiState.firstNameError,
                )

                ModalInput(
                    label = "Apellido Paterno *",
                    value = uiState.lastName,
                    onValueChange = { viewModel.updateLastName(it) },
                    placeholder = "Ingresa el apellido paterno",
                    isError = uiState.lastNameError != null,
                    errorMessage = uiState.lastNameError,
                )

                ModalInput(
                    label = "Apellido Materno",
                    value = uiState.secondLastName,
                    onValueChange = { viewModel.updateSecondLastName(it) },
                    placeholder = "Ingresa el apellido materno",
                )

                ModalInput(
                    label = "Correo Electrónico *",
                    value = uiState.email,
                    onValueChange = { viewModel.updateEmail(it) },
                    placeholder = "ejemplo@correo.com",
                    isError = uiState.emailError != null,
                    errorMessage = uiState.emailError,
                )

                ModalInput(
                    label = "Teléfono *",
                    value = uiState.phone,
                    onValueChange = { viewModel.updatePhone(it) },
                    placeholder = "1234567890",
                    isError = uiState.phoneError != null,
                    errorMessage = uiState.phoneError,
                )

                // Date of Birth with Calendar Picker
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .clickable { showDatePicker = true },
                ) {
                    ModalInput(
                        label = "Fecha de Nacimiento",
                        value = uiState.birthDate,
                        onValueChange = { viewModel.updateBirthDate(it) },
                        placeholder = "YYYY-MM-DD",
                    )

                    // Calendar icon overlay
                    IconButton(
                        onClick = { showDatePicker = true },
                        modifier =
                            Modifier
                                .align(Alignment.CenterEnd)
                                .padding(end = 8.dp, top = 32.dp),
                    ) {
                        CalendarIcon(tint = Color(0xFF3B82F6))
                    }
                }

                ModalInput(
                    label = "Carrera",
                    value = uiState.degree,
                    onValueChange = { viewModel.updateDegree(it) },
                    placeholder = "Ingeniería en...",
                )

                // Status Toggle - Updated to Blue
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Estatus",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 8.dp),
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        // Active Button - Blue
                        OutlinedButton(
                            onClick = { viewModel.updateIsActive(true) },
                            modifier = Modifier.weight(1f),
                            colors =
                                ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (uiState.isActive) Color(0xFF3B82F6) else Color.Transparent,
                                    contentColor = if (uiState.isActive) Color.White else Color(0xFF3B82F6),
                                ),
                            border =
                                androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (uiState.isActive) Color(0xFF3B82F6) else Color(0xFF3B82F6).copy(alpha = 0.5f),
                                ),
                        ) {
                            Text("Activo")
                        }

                        // Inactive Button - Blue
                        OutlinedButton(
                            onClick = { viewModel.updateIsActive(false) },
                            modifier = Modifier.weight(1f),
                            colors =
                                ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (!uiState.isActive) Color(0xFF3B82F6) else Color.Transparent,
                                    contentColor = if (!uiState.isActive) Color.White else Color(0xFF3B82F6),
                                ),
                            border =
                                androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (!uiState.isActive) Color(0xFF3B82F6) else Color(0xFF3B82F6).copy(alpha = 0.5f),
                                ),
                        ) {
                            Text("Inactivo")
                        }
                    }
                }

                // Success message
                if (uiState.successMessage != null) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors =
                            CardDefaults.cardColors(
                                containerColor = Color(0xFF22C55E).copy(alpha = 0.2f),
                            ),
                    ) {
                        Text(
                            text = uiState.successMessage ?: "",
                            color = Color(0xFF22C55E),
                            modifier = Modifier.padding(16.dp),
                            fontWeight = FontWeight.Medium,
                        )
                    }
                }

                // Error message
                if (uiState.error != null) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors =
                            CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.2f),
                            ),
                    ) {
                        Text(
                            text = uiState.error ?: "",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(16.dp),
                            fontWeight = FontWeight.Medium,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center,
                ) {
                    CancelButton(
                        onClick = {
                            viewModel.resetState()
                            onDismiss()
                        },
                    )
                }

                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center,
                ) {
                    SaveButton(
                        onClick = { viewModel.registerCollab() },
                    )
                }
            }

            if (uiState.isLoading) {
                Spacer(modifier = Modifier.height(16.dp))
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    color = Color.White,
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
