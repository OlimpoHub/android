package com.app.arcabyolimpo.presentation.screens.supply.supplybatchmodify

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.model.supplies.RegisterSupplyBatch
import com.app.arcabyolimpo.domain.usecase.supplies.GetAcquisitionTypesUseCase
import com.app.arcabyolimpo.domain.usecase.supplies.GetSuppliesListUseCase
import com.app.arcabyolimpo.domain.usecase.supplies.RegisterSupplyBatchUseCase
import com.app.arcabyolimpo.presentation.screens.supply.supplybatchregister.SupplyBatchRegisterUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SupplyBatchModifyViewModel
    @Inject
    constructor(
        private val getSuppliesListUseCase: GetSuppliesListUseCase,
        private val registerSupplyBatchUseCase: RegisterSupplyBatchUseCase,
        private val getAcquisitionTypesUseCase: GetAcquisitionTypesUseCase,
    ) : ViewModel() {
        // Tag used for Logcat so you can filter logs from this ViewModel while debugging
        private val TAG = "SupplyBatchModifyVM"

        private val _uiState = MutableStateFlow(SupplyBatchModifyUiState())
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

                    // Update UI state after logging
                    _uiState.update { state ->
                        when (result) {
                            is com.app.arcabyolimpo.domain.common.Result.Loading -> state.copy(isLoading = true, error = null)
                            is com.app.arcabyolimpo.domain.common.Result.Success ->
                                state.copy(
                                    suppliesList = result.data,
                                    isLoading = false,
                                    error = null,
                                )

                            is com.app.arcabyolimpo.domain.common.Result.Error ->
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

                    _uiState.update { state ->
                        when (result) {
                            is com.app.arcabyolimpo.domain.common.Result.Loading -> state.copy(isLoading = true, error = null)
                            is com.app.arcabyolimpo.domain.common.Result.Success ->
                                state.copy(
                                    acquisitionTypes = result.data,
                                    isLoading = false,
                                    error = null,
                                )

                            is com.app.arcabyolimpo.domain.common.Result.Error ->
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
            _uiState.update { it.copy(selectedSupplyId = id) }
        }

        fun onAcquisitionTypeSelected(id: String) {
            _uiState.update { it.copy(acquisitionInput = id) }
        }

        fun onQuantityChanged(value: String) {
            _uiState.update { it.copy(quantityInput = value) }
        }

        fun onExpirationDateChanged(value: String) {
            _uiState.update { it.copy(expirationDateInput = value) }
        }

        fun onBoughtDateChanged(value: String) {
            _uiState.update { it.copy(boughtDateInput = value) }
        }

        fun updateBatch() {
            val current = _uiState.value

            val supplyId = current.selectedSupplyId
            val quantity = current.quantityInput.toIntOrNull()
            val expiration = current.expirationDateInput
            val bought = current.boughtDateInput
            val acquisition = current.acquisitionInput

            // Log user input state before validation / network call
            Log.d(
                TAG,
                "registerBatch: called with supplyId=$supplyId, quantity=${current.quantityInput}, expiration=$expiration, bought=$bought",
            )

            if (supplyId.isNullOrEmpty() || quantity == null || acquisition.isNullOrEmpty()) {
                Log.w(TAG, "registerBatch: validation failed - required fields missing")
                _uiState.update { it.copy(registerError = "Seleccione un insumo, un tipo de adquisición y coloque una cantidad válida") }
                return
            }

            if (quantity < 0) {
                Log.w(TAG, "registerBatch: validation failed - negative quantity not allowed ($quantity)")
                _uiState.update {
                    it.copy(registerError = "La cantidad no puede ser negativa")
                }
                return
            }

            val batch =
                RegisterSupplyBatch(
                    supplyId = supplyId,
                    quantity = quantity,
                    expirationDate = expiration,
                    boughtDate = bought,
                    acquisition = acquisition,
                )

            viewModelScope.launch {
                // Collect the use case flow and log each stage for debugging (network, mapping, etc.)
                registerSupplyBatchUseCase(batch).collect { result ->
                    when (result) {
                        is com.app.arcabyolimpo.domain.common.Result.Loading -> Log.d(TAG, "registerBatch: Loading")
                        is com.app.arcabyolimpo.domain.common.Result.Success ->
                            Log.d(
                                TAG,
                                "registerBatch: Success - batch registered: ${result.data}",
                            )

                        is com.app.arcabyolimpo.domain.common.Result.Error ->
                            Log.w(
                                TAG,
                                "registerBatch: Error - ${result.exception.message}",
                            )
                    }

                    _uiState.update { state ->
                        when (result) {
                            is com.app.arcabyolimpo.domain.common.Result.Loading ->
                                state.copy(
                                    registerLoading = true,
                                    registerError = null,
                                    registerSuccess = false,
                                )

                            is com.app.arcabyolimpo.domain.common.Result.Success ->
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
