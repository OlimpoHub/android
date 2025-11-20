package com.app.arcabyolimpo.presentation.screens.supply.supplybatchmodify

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.model.supplies.RegisterSupplyBatch
import com.app.arcabyolimpo.domain.usecase.supplies.GetAcquisitionTypesUseCase
import com.app.arcabyolimpo.domain.usecase.supplies.GetSuppliesListUseCase
import com.app.arcabyolimpo.domain.usecase.supplies.ModifySupplyBatchUseCase
import com.app.arcabyolimpo.domain.usecase.supplies.GetSupplyBatchOneUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import java.time.Instant
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@HiltViewModel
class SupplyBatchModifyViewModel
    @Inject
    constructor(
        private val getSuppliesListUseCase: GetSuppliesListUseCase,
        private val modifySupplyBatchUseCase: ModifySupplyBatchUseCase,
        private val getAcquisitionTypesUseCase: GetAcquisitionTypesUseCase,
        private val getSupplyBatchOneUseCase: GetSupplyBatchOneUseCase,
    ) : ViewModel() {
        // Tag used for Logcat so you can filter logs from this ViewModel while debugging
        private val TAG = "SupplyBatchModifyVM"

        private val _uiState = MutableStateFlow(SupplyBatchModifyUiState())
        val uiState: StateFlow<SupplyBatchModifyUiState> = _uiState.asStateFlow()

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
                        is com.app.arcabyolimpo.domain.common.Result.Loading -> Log.d(TAG, "loadSuppliesList: Loading")
                        is com.app.arcabyolimpo.domain.common.Result.Success ->
                            Log.d(
                                TAG,
                                "loadSuppliesList: Success - received ${result.data.size} supplies",
                            )

                        is com.app.arcabyolimpo.domain.common.Result.Error ->
                            Log.w(
                                TAG,
                                "loadSuppliesList: Error - ${result.exception.message}",
                            )
                    }

                    // Update UI state after logging: compute explicit newState and assign
                    val newSuppliesState =
                        when (result) {
                            is Result.Loading -> _uiState.value.copy(isLoading = true, error = null)
                            is Result.Success ->
                                _uiState.value.copy(
                                    suppliesList = result.data,
                                    isLoading = false,
                                    error = null,
                                )

                            is Result.Error ->
                                _uiState.value.copy(
                                    isLoading = false,
                                    error = result.exception.message,
                                )
                        }

                    _uiState.value = newSuppliesState
                }
            }
        }

        fun loadAcquisitionTypes() {
            viewModelScope.launch {
                getAcquisitionTypesUseCase().collect { result ->
                    when (result) {
                        is com.app.arcabyolimpo.domain.common.Result.Loading -> Log.d(TAG, "loadAcquisitionTypes: Loading")
                        is com.app.arcabyolimpo.domain.common.Result.Success ->
                            Log.d(
                                TAG,
                                "loadAcquisitionTypes: Success - received ${result.data.size} acquisition types",
                            )

                        is com.app.arcabyolimpo.domain.common.Result.Error ->
                            Log.w(
                                TAG,
                                "loadAcquisitionTypes: Error - ${result.exception.message}",
                            )
                    }

                    val newAcqState =
                        when (result) {
                            is Result.Loading -> _uiState.value.copy(isLoading = true, error = null)
                            is Result.Success ->
                                _uiState.value.copy(
                                    acquisitionTypes = result.data,
                                    isLoading = false,
                                    error = null,
                                )

                            is Result.Error ->
                                _uiState.value.copy(
                                    isLoading = false,
                                    error = result.exception.message,
                                )
                        }

                    _uiState.value = newAcqState
                }
            }
        }

        fun onSelectSupply(id: String) {
            _uiState.update { it.copy(selectedSupplyId = id, supplyError = null, registerError = null) }
        }

        fun onSelectBatch(id: String) {
            _uiState.update { it.copy(selectedBatchId = id) }

            // Load batch details from repository and populate fields for modification
            viewModelScope.launch {
                getSupplyBatchOneUseCase(id).collect { result ->
                    when (result) {
                        is Result.Loading -> Log.d(TAG, "onSelectBatch: Loading batch $id")
                        is Result.Success -> Log.d(TAG, "onSelectBatch: Success - got batch for id $id: ${result.data}")
                        is Result.Error -> Log.w(TAG, "onSelectBatch: Error - ${result.exception.message}")
                    }

                    val newState = when (result) {
                        is Result.Loading -> _uiState.value.copy(isLoading = true, registerError = null)
                        is Result.Success -> _uiState.value.copy(
                            selectedSupplyId = result.data.supplyId,
                            quantityInput = result.data.quantity.toString(),
                            expirationDateInput = formatToDisplayDate(result.data.expirationDate),
                            boughtDateInput = formatToDisplayDate(result.data.boughtDate),
                            acquisitionInput = result.data.acquisition,
                            isLoading = false,
                            registerError = null,
                        )
                        is Result.Error -> _uiState.value.copy(
                            isLoading = false,
                            registerError = result.exception.message,
                        )
                        else -> _uiState.value
                    }

                    _uiState.value = newState
                }
            }
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

        // Validate inputs for modify action
        private fun validateInputsForModify(): Boolean {
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

        // Shared date validator (tries common formats)
        private fun isValidDate(dateStr: String): Boolean {
            if (dateStr.isBlank()) return false
            try {
                val fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                LocalDate.parse(dateStr, fmt)
                return true
            } catch (_: Exception) {
            }
            try {
                LocalDate.parse(dateStr)
                return true
            } catch (_: Exception) {
            }
            try {
                OffsetDateTime.parse(dateStr)
                return true
            } catch (_: Exception) {
            }
            try {
                Instant.parse(dateStr)
                return true
            } catch (_: Exception) {
            }
            return false
        }

        fun updateBatch() {
            val current = _uiState.value

            val supplyId = current.selectedSupplyId
            val batchId = current.selectedBatchId
            val quantity = current.quantityInput.toIntOrNull()
            val expiration = current.expirationDateInput
            val bought = current.boughtDateInput
            val acquisition = current.acquisitionInput

            // Log user input state and run validation
            Log.d(
                TAG,
                "updateBatch: called with batchId=$batchId, supplyId=$supplyId, quantity=${current.quantityInput}, expiration=$expiration, bought=$bought",
            )

            if (batchId.isNullOrEmpty()) {
                Log.w(TAG, "updateBatch: missing batchId")
                _uiState.update { it.copy(registerError = "Lote no válido seleccionado") }
                return
            }

            if (!validateInputsForModify()) {
                Log.w(TAG, "updateBatch: validation failed - see uiState field errors")
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
                val targetBatchId = batchId ?: ""
                modifySupplyBatchUseCase(targetBatchId, batch).collect { result ->
                    when (result) {
                        is Result.Loading -> Log.d(TAG, "modifyBatch: Loading")
                        is Result.Success ->
                            Log.d(
                                TAG,
                                "modifyBatch: Success - batch modified: ${result.data}",
                            )

                        is Result.Error ->
                            Log.w(
                                TAG,
                                "modifyBatch: Error - ${result.exception.message}",
                            )
                    }
                    val newRegisterState =
                        when (result) {
                            is Result.Loading ->
                                _uiState.value.copy(
                                    registerLoading = true,
                                    registerError = null,
                                    registerSuccess = false,
                                )

                            is Result.Success ->
                                _uiState.value.copy(
                                    registerLoading = false,
                                    registerSuccess = true,
                                    registerError = null,
                                )

                            is Result.Error ->
                                _uiState.value.copy(
                                    registerLoading = false,
                                    registerError = result.exception.message,
                                    registerSuccess = false,
                                )
                        }

                    _uiState.value = newRegisterState
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

        /**
         * Convert various backend/ISO date representations into the UI date format `dd/MM/yyyy`.
         * Returns empty string when input is null/empty or cannot be parsed.
         */
        private fun formatToDisplayDate(dateStr: String?): String {
            if (dateStr.isNullOrBlank()) return ""

            val outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

            try {
                // Try parsing RFC3339 / ISO instant with timezone (e.g. 2025-11-26T06:00:00.000Z)
                val odt = OffsetDateTime.parse(dateStr)
                val local = odt.toLocalDate()
                return local.format(outputFormatter)
            } catch (_: Exception) {
            }

            try {
                // Try parsing plain ISO local date (e.g. 2025-11-26)
                val ld = LocalDate.parse(dateStr)
                return ld.format(outputFormatter)
            } catch (_: Exception) {
            }

            try {
                // Try parsing Instant-like strings
                val instant = Instant.parse(dateStr)
                val local = instant.atZone(ZoneId.systemDefault()).toLocalDate()
                return local.format(outputFormatter)
            } catch (_: Exception) {
            }

            // If the backend already returned dd/MM/yyyy, return it; otherwise return empty to avoid invalid SQL
            return if (dateStr.contains("/")) dateStr else ""
        }
    }
