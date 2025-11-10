package com.app.arcabyolimpo.presentation.screens.beneficiary

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.app.arcabyolimpo.R
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.DeleteButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.ModifyButton
import com.app.arcabyolimpo.presentation.theme.Typography
import com.app.arcabyolimpo.presentation.ui.components.atoms.alerts.DecisionDialog
import com.app.arcabyolimpo.presentation.ui.components.atoms.status.ActiveStatus
import com.app.arcabyolimpo.presentation.ui.components.atoms.status.InactiveStatus
import com.app.arcabyolimpo.ui.theme.ArcaByOlimpoTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BeneficiaryDetail(
    beneficiaryName: String,
    beneficiaryTutorRelation: String,
    beneficiaryTutorName: String,
    beneficiaryEntryDate: String,
    beneficiaryEmergencyNumber: String,
    beneficiaryDisabilities: String,
    beneficiaryBirthdate: String,
    beneficiaryDetails: String,
    beneficiaryIsActive: Boolean,
    onBackClick: () -> Unit,
    onModifyClick: () -> Unit,
    onDeleteClick: () -> Unit // Lógica real de eliminación
) {
    // --- Logica para el snackbar ---
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Lógica de UI para mostrar/ocultar diálogo
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        DecisionDialog(
            dialogTitle = "¿Seguro que quieres eliminar a $beneficiaryName?",
            dialogText = "No podrá recuperarse una vez se haya eliminado.",
            onDismissRequest = { showDeleteDialog = false },
            onConfirmation = {
                showDeleteDialog = false
                onDeleteClick() // Llama a la lógica del ViewModel

                // Mostrar Snackbar
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = "Beneficiario eliminado correctamente"
                    )
                }
            }
        )
    }

    Scaffold(
        // Conectamos el snackbar al Scaffold
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(beneficiaryName) },
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
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()), // Para permitir scroll si no cabe
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

            // Este Row contiene las dos columnas para los detalles
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp) // Espacio entre columnas
            ) {
                // --- Columna Izquierda ---
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DetailTextRow(label = "Fecha de nacimiento:", value = beneficiaryBirthdate)
                    DetailTextRow(label = "Fecha de ingreso:", value = beneficiaryEntryDate)
                    StatusField(isActive = beneficiaryIsActive)
                }
                // --- Columna Derecha ---
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DetailTextRow(label = "Nombre de beneficiario:", value = beneficiaryName)
                    DetailTextRow(label = "Nombre de tutor:", value = beneficiaryTutorName)
                    DetailTextRow(label = "Relación del tutor:", value = beneficiaryTutorRelation)
                    DetailTextRow(label = "Número de teléfono:", value = beneficiaryEmergencyNumber)
                    DetailTextRow(label = "Discapacidades:", value = beneficiaryDisabilities)
                }
            }

            // Campo Descripcion
            Spacer(modifier = Modifier.height(16.dp))
            DetailTextRow(
                label = "Descripción:",
                value = beneficiaryDetails
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Botones de Acción
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                DeleteButton(
                    // onClick = { showDeleteDialog = true },
                    onClick = { /* TODO: Implementar dialogo */},
                    modifier = Modifier.weight(1f),
                    cornerRadius = 100.dp
                )
                ModifyButton(
                    onClick = onModifyClick,
                    modifier = Modifier.weight(1f),
                    cornerRadius = 100.dp
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
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
        BeneficiaryDetail(
            beneficiaryName = "Jiafei Daidai",
            beneficiaryTutorRelation = "Tutor",
            beneficiaryTutorName = "Juan Perez",
            beneficiaryEntryDate = "01/01/2023",
            beneficiaryEmergencyNumber = "1234567890",
            beneficiaryDisabilities = "Sí",
            beneficiaryBirthdate = "01/01/1990",
            beneficiaryDetails = "Detalles del beneficiario",
            beneficiaryIsActive = true,
            onBackClick = {},
            onModifyClick = {},
            onDeleteClick = {}
        )
    }
}

@Preview(name = "Inactivo", showBackground = true, backgroundColor = 0xFF1C1B1F)
@Composable
fun BeneficiaryDetailPreviewInactive() {
    ArcaByOlimpoTheme {
        BeneficiaryDetail(
            beneficiaryName = "Jiafei Daidai",
            beneficiaryTutorRelation = "Tutor",
            beneficiaryTutorName = "Juan Perez",
            beneficiaryEntryDate = "01/01/2023",
            beneficiaryEmergencyNumber = "1234567890",
            beneficiaryDisabilities = "Sí",
            beneficiaryBirthdate = "01/01/1990",
            beneficiaryDetails = "Detalles del beneficiario",
            beneficiaryIsActive = false,
            onBackClick = {},
            onModifyClick = {},
            onDeleteClick = {}
        )
    }
}