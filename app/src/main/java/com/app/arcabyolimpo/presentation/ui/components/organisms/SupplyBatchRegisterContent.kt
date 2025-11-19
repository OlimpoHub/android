package com.app.arcabyolimpo.presentation.ui.components.organisms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.app.arcabyolimpo.presentation.screens.supply.commonSupplyBatch.SupplyBatchUiStateBase
import com.app.arcabyolimpo.presentation.screens.supply.supplybatchregister.SupplyBatchRegisterUiState
import com.app.arcabyolimpo.presentation.theme.Typography
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.SquareAddButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.SquareMinusButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.DateInput
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.SelectInput
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.StandardInput
import com.app.arcabyolimpo.ui.theme.Background
import com.app.arcabyolimpo.ui.theme.ErrorRed
import com.app.arcabyolimpo.ui.theme.PrimaryBlue

@Suppress("ktlint:standard:function-naming")
@Composable
fun SupplyBatchRegisterContent(
    uiState: SupplyBatchUiStateBase,
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
