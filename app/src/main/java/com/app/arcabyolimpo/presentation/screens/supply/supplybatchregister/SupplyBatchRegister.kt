@file:Suppress("ktlint:standard:import-ordering")

package com.app.arcabyolimpo.presentation.screens.supply.supplybatchregister

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
// Card removed as requested; layout uses plain Column now
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import com.app.arcabyolimpo.presentation.theme.Typography
import com.app.arcabyolimpo.ui.theme.ErrorRed
import com.app.arcabyolimpo.ui.theme.PrimaryBlue
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.background
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.arcabyolimpo.domain.model.supplies.Supply
import com.app.arcabyolimpo.presentation.theme.Poppins
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.CancelButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.SaveButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.SquareAddButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.SquareMinusButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.SelectInput
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.StandardInput
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.DateInput
import com.app.arcabyolimpo.presentation.ui.components.molecules.NavBar
import com.app.arcabyolimpo.ui.theme.ArcaByOlimpoTheme
import com.app.arcabyolimpo.ui.theme.Background
import com.app.arcabyolimpo.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("ktlint:standard:function-naming")
@Composable
fun SupplyBatchRegisterScreen(
    onRegisterClick: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: SupplyBatchRegisterViewModel = hiltViewModel(),
) {
    // Use lifecycle-aware state collection (same pattern as SuppliesListScreen)
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    // Match the outer structure/style of SuppliesListScreen: Scaffold with TopAppBar
    Scaffold(
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Registrar Lotes",
                        color = White,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge,
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
        bottomBar = {
            Column {
                SupplyBatchRegisterBottomBar(
                    uiState = state,
                    onRegisterClick = {
                        viewModel.registerBatch()
                        onRegisterClick()
                    },
                    onBackClick = {
                        viewModel.clearRegisterStatus()
                        onBackClick()
                    },
                )
                NavBar()
            }
        },
    ) { padding ->
        // Box to respect the scaffold content padding and mirror SuppliesListScreen layout
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(padding)
                    .padding(8.dp),
        ) {
            // Reuse the content composable so we keep single-source-of-truth for UI
            SupplyBatchRegisterContent(
                uiState = state,
                onSelectSupply = { viewModel.onSelectSupply(it) },
                onQuantityChanged = { viewModel.onQuantityChanged(it) },
                onExpirationDateChanged = { viewModel.onExpirationDateChanged(it) },
                onBoughtDateChanged = { viewModel.onBoughtDateChanged(it) },
                onIncrementQuantity = { viewModel.onIncrementQuantity() },
                onDecrementQuantity = { viewModel.onDecrementQuantity() },
                onAcquisitionTypeSelected = { viewModel.onAcquisitionTypeSelected(it) },
            )
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun SupplyBatchRegisterBottomBar(
    uiState: SupplyBatchRegisterUiState,
    onRegisterClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(Background)
                .padding(horizontal = 28.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CancelButton(onClick = onBackClick)

        Spacer(modifier = Modifier.width(16.dp))

        if (uiState.registerLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(end = 8.dp))
        }

        SaveButton(onClick = {
            if (!uiState.registerLoading && uiState.selectedSupplyId != null) onRegisterClick()
        })
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun SupplyBatchRegisterContent(
    uiState: SupplyBatchRegisterUiState,
    onSelectSupply: (String) -> Unit,
    onQuantityChanged: (String) -> Unit,
    onExpirationDateChanged: (String) -> Unit,
    onBoughtDateChanged: (String) -> Unit,
    onIncrementQuantity: () -> Unit,
    onDecrementQuantity: () -> Unit,
    onAcquisitionTypeSelected: (String) -> Unit,
) {
    // Outer column gives spacing from the scaffold content
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Background)
                .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            // Supply selection using the shared SelectInput atom
            val supplies = uiState.suppliesList
            val selectedSupplyName = supplies.firstOrNull { it.id == uiState.selectedSupplyId }?.name ?: ""

            CompositionLocalProvider(LocalTextStyle provides Typography.bodyMedium) {
                SelectInput(
                    label = "Selecciona el insumo",
                    selectedOption = selectedSupplyName,
                    options = supplies.map { it.name },
                    onOptionSelected = { name ->
                        val supply = supplies.firstOrNull { it.name == name }
                        if (supply != null) onSelectSupply(supply.id)
                    },
                )
            }

            Row(
                modifier = Modifier.padding(0.dp),
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CompositionLocalProvider(LocalTextStyle provides Typography.bodyMedium) {
                    StandardInput(
                        label = "Cantidad",
                        value = uiState.quantityInput,
                        onValueChange = { value ->
                            val intValue = value.toIntOrNull()
                            if (intValue == null || intValue >= 0) {
                                onQuantityChanged(value)
                            }
                        },
                        modifier = Modifier.weight(0.5f),
                        placeholder = "E.G 10",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Match the text field height so the buttons visually align
                SquareAddButton(
                    modifier =
                        Modifier
                            .size(45.dp)
                            .padding(top = 9.dp, bottom = 0.dp),
                    onClick = onIncrementQuantity,
                )

                SquareMinusButton(
                    modifier =
                        Modifier
                            .size(45.dp)
                            .padding(top = 9.dp, bottom = 0.dp),
                    onClick = onDecrementQuantity,
                    enabled = uiState.quantityInput.toIntOrNull()?.let { it > 0 } ?: false,
                )
            }

            val acquisitionTypes = uiState.acquisitionTypes
            val selectedAcquisitionName = acquisitionTypes.firstOrNull { it.id == uiState.acquisitionInput }?.description ?: ""

            CompositionLocalProvider(LocalTextStyle provides Typography.bodyMedium) {
                SelectInput(
                    label = "Tipo de adquisición",
                    selectedOption = selectedAcquisitionName,
                    options = acquisitionTypes.map { it.description },
                    onOptionSelected = { description ->
                        val acq = acquisitionTypes.firstOrNull { it.description == description }
                        if (acq != null) onAcquisitionTypeSelected(acq.id)
                    },
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CompositionLocalProvider(LocalTextStyle provides Typography.bodyMedium) {
                    DateInput(
                        label = "Fecha de Compra",
                        value = uiState.boughtDateInput,
                        onValueChange = onBoughtDateChanged,
                        modifier = Modifier.weight(0.45f),
                        placeholder = "dd/mm/yyyy",
                    )
                }

                CompositionLocalProvider(LocalTextStyle provides Typography.bodyMedium) {
                    DateInput(
                        label = "Fecha de Caducidad",
                        value = uiState.expirationDateInput,
                        onValueChange = onExpirationDateChanged,
                        modifier = Modifier.weight(0.45f),
                        placeholder = "dd/mm/yyyy",
                    )
                }
            }

            if (uiState.registerError != null) {
                Text(text = "Error: ${uiState.registerError}", color = ErrorRed)
            }

            if (uiState.registerSuccess) {
                Text(text = "Lote registrado correctamente", color = PrimaryBlue)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Suppress("ktlint:standard:function-naming")
@Composable
fun SupplyBatchRegisterPreview() {
    val sampleSupplies =
        listOf(
            Supply(id = "i1", name = "Tornillos", imageUrl = "", unitMeasure = "pz", batch = emptyList()),
            Supply(id = "i2", name = "Clavos", imageUrl = "", unitMeasure = "pz", batch = emptyList()),
        )

    val sampleState =
        SupplyBatchRegisterUiState(
            suppliesList = sampleSupplies,
            isLoading = false,
            error = null,
            quantityInput = "",
            expirationDateInput = "",
            boughtDateInput = "",
            registerLoading = false,
            registerError = null,
            registerSuccess = false,
        )

    // *** MODIFICACIÓN APLICADA AQUÍ ***
    ArcaByOlimpoTheme(darkTheme = true, dynamicColor = false) {
        // Replicamos la estructura del Scaffold de la pantalla
        Scaffold(
            containerColor = Background,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Registrar Lotes",
                            color = White,
                            fontFamily = Poppins,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleLarge,
                        )
                    },
                    colors =
                        TopAppBarDefaults.topAppBarColors(
                            containerColor = Background,
                            titleContentColor = PrimaryBlue,
                        ),
                )
            },
            bottomBar = {
                Column {
                    SupplyBatchRegisterBottomBar(
                        uiState = sampleState,
                        onRegisterClick = {}, // Acciones de mock
                        onBackClick = {}, // Acciones de mock
                    )
                    NavBar()
                }
            },
        ) { padding ->
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(padding)
                        .padding(8.dp),
            ) {
                SupplyBatchRegisterContent(
                    uiState = sampleState,
                    onSelectSupply = {},
                    onQuantityChanged = {},
                    onExpirationDateChanged = {},
                    onBoughtDateChanged = {},
                    onIncrementQuantity = { },
                    onDecrementQuantity = { },
                    onAcquisitionTypeSelected = {},
                )
            }
        }
    }
}
