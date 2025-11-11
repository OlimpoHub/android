@file:Suppress("ktlint:standard:import-ordering")

package com.app.arcabyolimpo.presentation.screens.supplybatchregister

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.arcabyolimpo.domain.model.supplies.Supply
import com.app.arcabyolimpo.presentation.screens.supplybatchregister.SupplyBatchRegisterUiState
import com.app.arcabyolimpo.presentation.screens.supplybatchregister.SupplyBatchRegisterViewModel
import com.app.arcabyolimpo.presentation.theme.Poppins
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.CancelButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.SaveButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.SquareAddButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.SquareMinusButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.CalendarIcon
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.NotificationIcon
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.SelectInput
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.StandardInput
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.text.font.FontWeight
import com.app.arcabyolimpo.ui.theme.ArcaByOlimpoTheme
import com.app.arcabyolimpo.ui.theme.Background
import com.app.arcabyolimpo.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("ktlint:standard:function-naming")
@Composable
fun SupplyBatchRegisterScreen(
    onRegisterClick: () -> Unit,
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
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Background),
            )
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
                onRegister = { viewModel.registerBatch() },
                onClear = { viewModel.clearRegisterStatus() },
            )
        }
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
    onRegister: () -> Unit,
    onClear: () -> Unit,
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
                        onValueChange = onQuantityChanged,
                        modifier = Modifier.weight(0.5f),
                        placeholder = "E.G 10",
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Match the text field height so the buttons visually align
                SquareAddButton(
                    modifier =
                        Modifier
                            .size(45.dp)
                            .padding(top = 9.dp, bottom = 0.dp),
                    onClick = {
                        val current = uiState.quantityInput.toIntOrNull() ?: 0
                        onQuantityChanged((current + 1).toString())
                    },
                )

                SquareMinusButton(
                    modifier =
                        Modifier
                            .size(45.dp)
                            .padding(top = 9.dp, bottom = 0.dp),
                    onClick = {
                        val current = uiState.quantityInput.toIntOrNull() ?: 0
                        val next = if (current > 0) current - 1 else 0
                        onQuantityChanged(next.toString())
                    },
                )
            }

            // Tipo de adquisición using SelectInput (visual/local for now)
            val acquisitionOptions = listOf("Compra", "Donación", "Transferencia")
            var selectedAcq by remember { mutableStateOf(acquisitionOptions[0]) }

            CompositionLocalProvider(LocalTextStyle provides Typography.bodyMedium) {
                SelectInput(
                    label = "Tipo de adquisición",
                    selectedOption = selectedAcq,
                    options = acquisitionOptions,
                    onOptionSelected = { selectedAcq = it },
                )
            }

            // Dates row (fecha de compra / fecha de caducidad)
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CompositionLocalProvider(LocalTextStyle provides Typography.bodyMedium) {
                    StandardInput(
                        label = "Fecha de Compra",
                        value = uiState.boughtDateInput,
                        onValueChange = onBoughtDateChanged,
                        modifier = Modifier.weight(0.45f).height(80.dp),
                        placeholder = "DD/MM/YYYY",
                        trailingIcon = { CalendarIcon(modifier = Modifier.size(20.dp), tint = Color.White, size = 20.dp) },
                    )
                }

                CompositionLocalProvider(LocalTextStyle provides Typography.bodyMedium) {
                    StandardInput(
                        label = "Fecha de Caducidad",
                        value = uiState.expirationDateInput,
                        onValueChange = onExpirationDateChanged,
                        modifier = Modifier.weight(0.45f).height(80.dp),
                        placeholder = "DD/MM/YYYY",
                        trailingIcon = { CalendarIcon(modifier = Modifier.size(20.dp), tint = Color.White, size = 20.dp) },
                    )
                }
            }

            // Actions: centered row with Cancel and Save; mirrors screenshot spacing
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Spacer(modifier = Modifier.weight(1f))

                CancelButton(onClick = onClear)

                if (uiState.registerLoading) {
                    CircularProgressIndicator(modifier = Modifier.padding(end = 8.dp))
                }

                SaveButton(onClick = {
                    if (!uiState.registerLoading && uiState.selectedSupplyId != null) onRegister()
                })
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
            selectedSupplyId = "i1",
            quantityInput = "10",
            expirationDateInput = "2026-11-03",
            boughtDateInput = "2025-11-01",
            registerLoading = false,
            registerError = null,
            registerSuccess = false,
        )

    // Use darkTheme=true in preview so the app's Background color (dark palette) is visible
    ArcaByOlimpoTheme(darkTheme = true, dynamicColor = false) {
        SupplyBatchRegisterContent(
            uiState = sampleState,
            onSelectSupply = {},
            onQuantityChanged = {},
            onExpirationDateChanged = {},
            onBoughtDateChanged = {},
            onRegister = {},
            onClear = {},
        )
    }
}
