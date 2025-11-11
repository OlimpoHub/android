package com.app.arcabyolimpo.presentation.screens.beneficiary

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.arcabyolimpo.R
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.DeleteButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.ModifyButton
import com.app.arcabyolimpo.presentation.theme.Typography
import com.app.arcabyolimpo.presentation.ui.components.atoms.alerts.DecisionDialog
import com.app.arcabyolimpo.presentation.ui.components.atoms.status.ActiveStatus
import com.app.arcabyolimpo.presentation.ui.components.atoms.status.InactiveStatus
import com.app.arcabyolimpo.domain.model.beneficiaries.Beneficiary
import com.app.arcabyolimpo.ui.theme.ArcaByOlimpoTheme
import kotlinx.coroutines.launch

@Composable
fun BeneficiaryDetailScreen(
    onBackClick: () -> Unit,
    onModifyClick: () -> Unit,
    viewModel: BeneficiaryDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.deleteSuccess, uiState.deleteError) {
        if (uiState.deleteSuccess) {
            scope.launch {
                snackbarHostState.showSnackbar("Beneficiario eliminado correctamente")
            }
            onBackClick()
        }

        val error = uiState.deleteError
        if (error != null) {
            scope.launch {
                snackbarHostState.showSnackbar(error)
            }
            viewModel.onDeletionErrorShown()
        }
    }

    BeneficiaryDetailContent(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        showDeleteDialog = showDeleteDialog,
        onBackClick = onBackClick,
        onModifyClick = {
            uiState.beneficiary?.id?.let { onModifyClick() }
        },
        onDeleteClick = viewModel::onDeleteClicked,
        onShowDialog = { showDeleteDialog = true },
        onDismissDialog = { showDeleteDialog = false }
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BeneficiaryDetailContent(
    uiState: BeneficiaryDetailUiState,
    snackbarHostState: SnackbarHostState,
    showDeleteDialog: Boolean,
    onBackClick: () -> Unit,
    onModifyClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onShowDialog: () -> Unit,
    onDismissDialog: () -> Unit
) {
    if (showDeleteDialog) {
        DecisionDialog(
            dialogTitle = "¿Seguro que quieres eliminar a ${uiState.beneficiary?.name ?: "..."}?",
            dialogText = "No podrá recuperarse una vez se haya eliminado.",
            onDismissRequest = onDismissDialog,
            onConfirmation = {
                onDismissDialog()
                onDeleteClick()
            },
            confirmText = "Confirmar",
            dismissText = "Cancelar"
        )
    }

    Scaffold(
        // Conectamos el snackbar al Scaffold
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(uiState.beneficiary?.name ?: "Cargando...") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_return_icon),
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        },
        containerColor = Color(0xFF1C1B1F) // Color de fondo oscuro
    ) { paddingValues ->
        when {
            uiState.isScreenLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            uiState.screenError != null -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = uiState.screenError, color = Color.Red)
                }
            }
            uiState.beneficiary != null -> {
                val beneficiary = uiState.beneficiary
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(horizontal = 24.dp)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Cuando hagan la chamba bien aqui agregan la imagen
                    /*
                    Image(
                        painter = painterResource(id = R.drawable.placeholder),
                        contentDescription = "Foto de $beneficiaryName",
                        modifier = Modifier
                            .size(150.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Crop
                    )
                    */

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp) // Espacio entre columnas
                    ) {
                        // --- Columna Izquierda ---
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            DetailTextRow(label = "Fecha de nacimiento:", value = beneficiary.birthdate)
                            DetailTextRow(label = "Fecha de ingreso:", value = beneficiary.entryDate)
                            StatusField(isActive = beneficiary.status == 0)
                        }

                        // --- Columna Derecha ---
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            DetailTextRow(label = "Nombre de beneficiario:", value = beneficiary.name)
                            DetailTextRow(label = "Nombre de tutor:", value = "Nombre Tutor" /* TODO */)
                            DetailTextRow(label = "Relación del tutor:", value = beneficiary.emergencyRelation)
                            DetailTextRow(label = "Número de teléfono:", value = beneficiary.emergencyNumber)
                            DetailTextRow(label = "Discapacidades:", value = beneficiary.disabilities)
                        }
                }
                    // Campo Descripcion
                    Spacer(modifier = Modifier.height(16.dp))
                    DetailTextRow(
                        label = "Descripción:",
                        value = beneficiary.details
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Botones de Acción
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        DeleteButton(
                            onClick = onShowDialog,
                            modifier = Modifier.weight(1f),
                            cornerRadius = 100.dp,
                            enabled = !uiState.isDeleting
                        )
                        ModifyButton(
                            onClick = onModifyClick,
                            modifier = Modifier.weight(1f),
                            cornerRadius = 100.dp,
                            enabled = !uiState.isDeleting
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun StatusField(isActive: Boolean) {
    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
        Text(text = "Estatus:", style = Typography.labelMedium, color = Color.Gray)

        // Lógico condicional para mostrar el átomo correcto
        if (isActive) {
            ActiveStatus(
                modifier = Modifier.padding(top = 4.dp)
            )
        } else {
            InactiveStatus(
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
fun DetailTextRow(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
        Text(text = label, style = Typography.labelMedium, color = Color.Gray)
        Text(text = value, style = Typography.bodyLarge, color = Color.White)
    }
}

@Preview(name = "Activo", showBackground = true, backgroundColor = 0xFF1C1B1F)
@Composable
fun BeneficiaryDetailPreviewActive() {
    ArcaByOlimpoTheme {
        BeneficiaryDetailContent(
            uiState = BeneficiaryDetailUiState(
                isScreenLoading = false,
                beneficiary = Beneficiary(
                    id = "1",
                    name = "Jiafei Daidai",
                    birthdate = "01/01/1990",
                    emergencyNumber = "1234567890",
                    emergencyName = "Juan",
                    emergencyRelation = "Padre",
                    details = "Detalles del beneficiario",
                    entryDate = "01/01/2023",
                    image = "",
                    disabilities = "Sí",
                    status = 1
                )
            ),
            snackbarHostState = remember { SnackbarHostState() },
            showDeleteDialog = false,
            onBackClick = {},
            onModifyClick = {},
            onDeleteClick = {},
            onShowDialog = {},
            onDismissDialog = {}
        )
    }
}

@Preview(name = "Inactivo", showBackground = true, backgroundColor = 0xFF1C1B1F)
@Composable
fun BeneficiaryDetailPreviewInactive() {
    ArcaByOlimpoTheme {
        BeneficiaryDetailContent(
            uiState = BeneficiaryDetailUiState(
                isScreenLoading = false,
                beneficiary = Beneficiary(
                    id = "1",
                    name = "Jiafei Daidai",
                    birthdate = "01/01/1990",
                    emergencyNumber = "1234567890",
                    emergencyName = "Juan",
                    emergencyRelation = "Padre",
                    details = "Detalles del beneficiario",
                    entryDate = "01/01/2023",
                    image = "",
                    disabilities = "Sí",
                    status = 0
                )
            ),
            snackbarHostState = remember { SnackbarHostState() },
            showDeleteDialog = false,
            onBackClick = {},
            onModifyClick = {},
            onDeleteClick = {},
            onShowDialog = {},
            onDismissDialog = {}
        )
    }
}