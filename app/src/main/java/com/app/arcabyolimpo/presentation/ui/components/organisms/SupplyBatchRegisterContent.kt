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
import com.app.arcabyolimpo.presentation.screens.supply.supplybatchmodify.SupplyBatchModifyUiState
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
/**
 * Composable content for the supply batch registration and modification form.
 *
 * This organism renders the input fields used by both the register and modify
 * screens: quantity, acquisition type selector, purchase date and expiration
 * date, and the increment/decrement controls. The composable reads per-field
 * error messages from the provided `uiState` (which can be either register
 * or modify concrete states) and passes `isError`/`errorMessage` to the
 * individual input atoms so inline validation messages are shown.
 *
 * Note: supply selection was intentionally removed from this content and the
 * currently selected supply name is shown in the screen's TopAppBar.
 *
 * @param uiState The shared UI state providing current input values, lists
 *  for selectors, loading and error flags, and per-field error messages.
 * @param onSelectSupply Callback when a supply is selected (left for
 *  backwards compatibility; supply selection UI was moved to the parent).
 * @param onQuantityChanged Callback when the quantity text changes.
 * @param onExpirationDateChanged Callback when the expiration date changes
 *  (display format is `dd/MM/yyyy`).
 * @param onBoughtDateChanged Callback when the bought/purchase date changes
 *  (display format is `dd/MM/yyyy`).
 * @param onIncrementQuantity Increment quantity by one.
 * @param onDecrementQuantity Decrement quantity by one.
 * @param onAcquisitionTypeSelected Called with the acquisition type id when
 *  the user selects an option from the acquisition selector.
 */
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
            // Resolve per-field error messages from concrete UI state types
            val supplyErrorMsg =
                when (uiState) {
                    is SupplyBatchRegisterUiState -> uiState.supplyError
                    is SupplyBatchModifyUiState -> uiState.supplyError
                    else -> null
                }

            val quantityErrorMsg =
                when (uiState) {
                    is SupplyBatchRegisterUiState -> uiState.quantityError
                    is SupplyBatchModifyUiState -> uiState.quantityError
                    else -> null
                }

            val acquisitionErrorMsg =
                when (uiState) {
                    is SupplyBatchRegisterUiState -> uiState.acquisitionError
                    is SupplyBatchModifyUiState -> uiState.acquisitionError
                    else -> null
                }

            val boughtDateErrorMsg =
                when (uiState) {
                    is SupplyBatchRegisterUiState -> uiState.boughtDateError
                    is SupplyBatchModifyUiState -> uiState.boughtDateError
                    else -> null
                }

            val expirationDateErrorMsg =
                when (uiState) {
                    is SupplyBatchRegisterUiState -> uiState.expirationDateError
                    is SupplyBatchModifyUiState -> uiState.expirationDateError
                    else -> null
                }

            // Supply selection removed from content; the screen top bar shows the selected supply name.

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
                        isError = !quantityErrorMsg.isNullOrEmpty(),
                        errorMessage = quantityErrorMsg,
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
                    isError = !acquisitionErrorMsg.isNullOrEmpty(),
                    errorMessage = acquisitionErrorMsg,
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
                        isError = !boughtDateErrorMsg.isNullOrEmpty(),
                        errorMessage = boughtDateErrorMsg ?: "",
                    )
                }

                CompositionLocalProvider(LocalTextStyle provides Typography.bodyMedium) {
                    DateInput(
                        label = "Fecha de Caducidad",
                        value = uiState.expirationDateInput,
                        onValueChange = onExpirationDateChanged,
                        modifier = Modifier.weight(0.45f),
                        placeholder = "dd/mm/yyyy",
                        isError = !expirationDateErrorMsg.isNullOrEmpty(),
                        errorMessage = expirationDateErrorMsg ?: "",
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
