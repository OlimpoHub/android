package com.app.arcabyolimpo.presentation.screens.supply.supplybatchregister

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.model.supplies.RegisterSupplyBatch
import com.app.arcabyolimpo.domain.usecase.supplies.GetAcquisitionTypesUseCase
import com.app.arcabyolimpo.domain.usecase.supplies.GetSuppliesListUseCase
import com.app.arcabyolimpo.domain.usecase.supplies.RegisterSupplyBatchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Supply Batch Register screen.
 *
 * Debugging tips:
 * - Use Logcat to filter logs from this ViewModel by tag:
 *     adb logcat -s SupplyBatchRegisterVM
 *   or in Android Studio's Logcat set the filter to `SupplyBatchRegisterVM`.
 * - The ViewModel logs when it's created, when it invokes the getSupplies use case,
 *   and the results (Loading/Success/Error) including the number of supplies returned.
 * - When registering a batch, input values and use case results are also logged.
 */
@HiltViewModel
class SupplyBatchRegisterViewModel
    @Inject
    constructor(
        private val getSuppliesListUseCase: GetSuppliesListUseCase,
        private val registerSupplyBatchUseCase: RegisterSupplyBatchUseCase,
        private val getAcquisitionTypesUseCase: GetAcquisitionTypesUseCase,
    ) : ViewModel() {
        // Tag used for Logcat so you can filter logs from this ViewModel while debugging
        private val TAG = "SupplyBatchRegisterVM"

        private val _uiState = MutableStateFlow(SupplyBatchRegisterUiState())
        val uiState: StateFlow<SupplyBatchRegisterUiState> = _uiState.asStateFlow()

        init {
            // Log when ViewModel is created (helps confirm DI/Hilt and screen wiring)
            Log.d(TAG, "init: SupplyBatchRegisterViewModel created - starting loadSuppliesList()")
            loadSuppliesList()
            loadAcquisitionTypes()
        }

        private val MAX_QUANTITY = 999999

        fun onIncrementQuantity() {
            val currentValue = _uiState.value.quantityInput.toIntOrNull() ?: 0
            val newValue = (currentValue + 1).coerceAtMost(MAX_QUANTITY)

            Log.d(TAG, "onIncrementQuantity: $newValue")
            _uiState.update {
                it.copy(quantityInput = newValue.toString())
            }
        }

        fun onDecrementQuantity() {
            val currentValue = _uiState.value.quantityInput.toIntOrNull() ?: 0
            val newValue = (currentValue - 1).coerceAtLeast(0)

            Log.d(TAG, "onDecrementQuantity: $newValue")
            _uiState.update {
                it.copy(quantityInput = newValue.toString())
            }
        }

        // Load supplies list using the same pattern as SuppliesListViewModel: expose isLoading / error
        fun loadSuppliesList() {
            viewModelScope.launch {
                // Helpful log to show that the load call was triggered from the UI
                Log.d(TAG, "loadSuppliesList: invoking getSuppliesListUseCase()")

                getSuppliesListUseCase().collect { result ->
                    // Log the raw result so you can see Loading/Success/Error in Logcat
                    when (result) {
                        is Result.Loading -> Log.d(TAG, "loadSuppliesList: Loading")
                        is Result.Success ->
                            Log.d(
                                TAG,
                                "loadSuppliesList: Success - received ${result.data.size} supplies",
                            )

                        is Result.Error ->
                            Log.w(
                                TAG,
                                "loadSuppliesList: Error - ${result.exception.message}",
                            )
                    }

                    // Update UI state after logging
                    _uiState.update { state ->
                        when (result) {
                            is Result.Loading -> state.copy(isLoading = true, error = null)
                            is Result.Success ->
                                state.copy(
                                    suppliesList = result.data,
                                    isLoading = false,
                                    error = null,
                                )

                            is Result.Error ->
                                state.copy(
                                    isLoading = false,
                                    error = result.exception.message,
                                )
                        }
                    }
                }
            }
        }

        fun loadAcquisitionTypes() {
            viewModelScope.launch {
                getAcquisitionTypesUseCase().collect { result ->
                    when (result) {
                        is Result.Loading -> Log.d(TAG, "loadAcquisitionTypes: Loading")
                        is Result.Success ->
                            Log.d(
                                TAG,
                                "loadAcquisitionTypes: Success - received ${result.data.size} acquisition types",
                            )

                        is Result.Error ->
                            Log.w(
                                TAG,
                                "loadAcquisitionTypes: Error - ${result.exception.message}",
                            )
                    }

                    _uiState.update { state ->
                        when (result) {
                            is Result.Loading -> state.copy(isLoading = true, error = null)
                            is Result.Success ->
                                state.copy(
                                    acquisitionTypes = result.data,
                                    isLoading = false,
                                    error = null,
                                )

                            is Result.Error ->
                                state.copy(
                                    isLoading = false,
                                    error = result.exception.message,
                                )
                        }
                    }
                }
            }
        }

        fun onSelectSupply(id: String) {
            _uiState.update { it.copy(selectedSupplyId = id, supplyError = null, registerError = null) }
        }

        fun onAcquisitionTypeSelected(id: String) {
            _uiState.update { it.copy(acquisitionInput = id, acquisitionError = null, registerError = null) }
        }

        fun onQuantityChanged(value: String) {
            _uiState.update { it.copy(quantityInput = value, quantityError = null, registerError = null) }
        }

        fun onExpirationDateChanged(value: String) {
            _uiState.update { it.copy(expirationDateInput = value, expirationDateError = null, registerError = null) }
        }

        fun onBoughtDateChanged(value: String) {
            _uiState.update { it.copy(boughtDateInput = value, boughtDateError = null, registerError = null) }
        }

        private fun validateInputsForRegister(): Boolean {
            val state = _uiState.value
            var valid = true
            val maxLen = 50

            var supplyErr: String? = null
            var qtyErr: String? = null
            var expErr: String? = null
            var boughtErr: String? = null
            var acqErr: String? = null

            if (state.selectedSupplyId.isNullOrEmpty()) {
                supplyErr = "Seleccione un insumo"
                valid = false
            }

            val qty = state.quantityInput.toIntOrNull()
            if (state.quantityInput.isBlank() || qty == null) {
                qtyErr = "Ingrese una cantidad válida"
                valid = false
            } else if (qty <= 0) {
                qtyErr = "La cantidad debe ser mayor a cero"
                valid = false
            }

            if (state.expirationDateInput.isBlank()) {
                expErr = "Ingrese una fecha de caducidad"
                valid = false
            } else if (state.expirationDateInput.length > maxLen) {
                expErr = "Máximo $maxLen caracteres"
                valid = false
            } else if (!isValidDate(state.expirationDateInput)) {
                expErr = "Formato de fecha inválido"
                valid = false
            }

            if (state.boughtDateInput.isBlank()) {
                boughtErr = "Ingrese la fecha de adquisición"
                valid = false
            } else if (state.boughtDateInput.length > maxLen) {
                boughtErr = "Máximo $maxLen caracteres"
                valid = false
            } else if (!isValidDate(state.boughtDateInput)) {
                boughtErr = "Formato de fecha inválido"
                valid = false
            }

            if (state.acquisitionInput.isBlank()) {
                acqErr = "Seleccione un tipo de adquisición"
                valid = false
            } else if (state.acquisitionInput.length > maxLen) {
                acqErr = "Máximo $maxLen caracteres"
                valid = false
            }

            _uiState.update { it.copy(
                supplyError = supplyErr,
                quantityError = qtyErr,
                expirationDateError = expErr,
                boughtDateError = boughtErr,
                acquisitionError = acqErr,
                registerError = if (!valid) "Corrija los campos obligatorios" else null,
            ) }

            return valid
        }

        // Try to parse common date representations used in the app/backend
        private fun isValidDate(dateStr: String): Boolean {
            if (dateStr.isBlank()) return false
            try {
                // dd/MM/yyyy
                val fmt = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")
                java.time.LocalDate.parse(dateStr, fmt)
                return true
            } catch (_: Exception) {
            }
            try {
                // yyyy-MM-dd or ISO local date
                java.time.LocalDate.parse(dateStr)
                return true
            } catch (_: Exception) {
            }
            try {
                // OffsetDateTime (ISO)
                java.time.OffsetDateTime.parse(dateStr)
                return true
            } catch (_: Exception) {
            }
            try {
                java.time.Instant.parse(dateStr)
                return true
            } catch (_: Exception) {
            }
            return false
        }

        fun registerBatch() {
            val current = _uiState.value

            val supplyId = current.selectedSupplyId
            val quantity = current.quantityInput.toIntOrNull()
            val expiration = current.expirationDateInput
            val bought = current.boughtDateInput
            val acquisition = current.acquisitionInput

            // Run form validation (per-field errors will be set on _uiState)
            Log.d(
                TAG,
                "registerBatch: called with supplyId=$supplyId, quantity=${current.quantityInput}, expiration=$expiration, bought=$bought",
            )

            if (!validateInputsForRegister()) {
                Log.w(TAG, "registerBatch: validation failed - see uiState field errors")
                return
            }

            val batch =
                RegisterSupplyBatch(
                    supplyId = supplyId!!,
                    quantity = quantity!!,
                    expirationDate = expiration,
                    acquisition = acquisition,
                    boughtDate = bought,
                )

            viewModelScope.launch {
                // Collect the use case flow and log each stage for debugging (network, mapping, etc.)
                registerSupplyBatchUseCase(batch).collect { result ->
                    when (result) {
                        is Result.Loading -> Log.d(TAG, "registerBatch: Loading")
                        is Result.Success ->
                            Log.d(
                                TAG,
                                "registerBatch: Success - batch registered: ${result.data}",
                            )

                        is Result.Error ->
                            Log.w(
                                TAG,
                                "registerBatch: Error - ${result.exception.message}",
                            )
                    }

                    _uiState.update { state ->
                        when (result) {
                            is Result.Loading ->
                                state.copy(
                                    registerLoading = true,
                                    registerError = null,
                                    registerSuccess = false,
                                )

                            is Result.Success ->
                                state.copy(
                                    registerLoading = false,
                                    registerSuccess = true,
                                    registerError = null,
                                )

                            is Result.Error ->
                                state.copy(
                                    registerLoading = false,
                                    registerError = result.exception.message,
                                    registerSuccess = false,
                                )
                        }
                    }
                }
            }
        }

        fun clearRegisterStatus() {
            Log.d(TAG, "clearRegisterStatus: clearing register flags")
            _uiState.update {
                it.copy(
                    registerLoading = false,
                    registerError = null,
                    registerSuccess = false,
                )
            }
        }
    }
