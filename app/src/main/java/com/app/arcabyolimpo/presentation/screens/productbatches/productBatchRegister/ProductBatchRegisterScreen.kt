package com.app.arcabyolimpo.presentation.screens.productbatches.productBatchRegister

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.arcabyolimpo.presentation.theme.Poppins
import com.app.arcabyolimpo.presentation.ui.components.atoms.alerts.DecisionDialog
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.CancelButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.ModifyButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.SaveButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.SquareAddButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.SquareMinusButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.DateInput
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.ImageUploadInput
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.SelectInput
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.StandardInput
import com.app.arcabyolimpo.presentation.ui.components.molecules.NavBar
import com.app.arcabyolimpo.presentation.ui.components.molecules.NumberStepper
import com.app.arcabyolimpo.ui.theme.Background
import com.app.arcabyolimpo.ui.theme.White
import kotlinx.coroutines.launch

/** ProductBatchRegisterScreen: Composable screen for registering a new product batch.
 *
 * @param onBackClick () -> Unit -> callback for back navigation
 * @param onCreated () -> Unit -> callback when the product batch is successfully created
 * @param viewModel ProductBatchRegisterViewModel = hiltViewModel() -> ViewModel for managing UI state
 */
@OptIn(ExperimentalMaterial3Api::class)
@Suppress("ktlint:standard:function-naming")
@Composable
fun ProductBatchRegisterScreen(
    onBackClick: () -> Unit,
    onCreated: () -> Unit,
    viewModel: ProductBatchRegisterViewModel = hiltViewModel(),
) {
    val state = viewModel.uiState

    LaunchedEffect(Unit) {
        viewModel.loadData()
    }

    val selectedProductIndex = state.productIds.indexOf(state.idProducto)
    val selectedProductName =
        if (selectedProductIndex != -1) {
            state.names.getOrNull(selectedProductIndex) ?: ""
        } else {
            ""
        }

    var showConfirmDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Registrar Lote",
                        color = White,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineLarge,
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.Default.ArrowBack,
                            "Back",
                            tint = White,
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Background),
            )
        },
    ) { padding ->
        Column(
            modifier =
                Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.padding(vertical = 40.dp)) {}

            SelectInput(
                label = "Selecciona el producto",
                placeholder = "Producto",
                selectedOption = selectedProductName,
                options = state.names,
                isError = state.isIdProductoError,
                errorMessage = if (state.isIdProductoError) "Selecciona un producto" else "",
                onOptionSelected = { selectedName ->
                    val index = state.names.indexOf(selectedName)
                    if (index != -1) {
                        val id = state.productIds[index]
                        viewModel.onFieldChange("idProducto", id)
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
            )

            StandardInput(
                label = "Precio de venta",
                value = state.precioVenta,
                onValueChange = {
                    viewModel.onFieldChange("precioVenta", it)
                },
                placeholder = "0.00",
                keyboardType = KeyboardType.Decimal,
                isError = state.isPrecioVentaError,
                errorMessage = if (state.isPrecioVentaError) "Ingresa un precio de venta" else "",
            )

            NumberStepper(
                label = "Cantidad producida",
                value = state.cantidadProducida,
                isError = state.isCantidadProducidaError,
                errorMessage = if (state.isCantidadProducidaError) "Ingresa una cantidad mayor a 0" else "",
                onValueChange = {
                    viewModel.onFieldChange("cantidadProducida", it)
                },
                onIncrement = {
                    val newVal = (state.cantidadProducida.toIntOrNull() ?: 0) + 1
                    viewModel.onFieldChange("cantidadProducida", newVal.toString())
                },
                onDecrement = {
                    val current = state.cantidadProducida.toIntOrNull() ?: 0
                    if (current > 0) {
                        viewModel.onFieldChange("cantidadProducida", (current - 1).toString())
                    }
                },
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                DateInput(
                    label = "Fecha de Elaboración",
                    value = state.fechaRealizacion,
                    isError = state.isFechaRealizacionError,
                    errorMessage = if (state.isFechaRealizacionError) "Selecciona una fecha" else "",
                    onValueChange = {
                        viewModel.onFieldChange("fechaRealizacion", it)
                    },
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .weight(1f),
                )
                DateInput(
                    label = "Fecha de Caducidad",
                    value = state.fechaCaducidad,
                    isError = state.isFechaCaducidadError,
                    errorMessage = if (state.isFechaCaducidadError) "Fecha no válida" else "",
                    onValueChange = {
                        viewModel.onFieldChange("fechaCaducidad", it)
                    },
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .weight(1f),
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Column(
                    modifier =
                        Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    CancelButton(
                        onClick = onBackClick,
                    )
                }
                Column(
                    modifier =
                        Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    SaveButton(
                        onClick = { showConfirmDialog = true }
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
                                viewModel.validateAndRegister(onSuccess = onCreated)
                            },
                            dialogTitle = "Confirmar registro",
                            dialogText = "¿Deseas registrar este lote de producto? Asegúrate de que todos los datos sean correctos.",
                            confirmText = "Confirmar",
                            dismissText = "Cancelar",
                        )
                    }
                }
            }
        }
    }
}
