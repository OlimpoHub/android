package com.app.arcabyolimpo.presentation.screens.supply.supplyBatchList

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.usecase.supplies.GetSupplyBatchListUseCase
import com.app.arcabyolimpo.domain.usecase.supplies.GetSupplyBatchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the SupplyBatchListScreen.
 *
 * @property getSupplyBatchUseCase Use case to fetch supply batches.
 * @property getSupplyBatchListUseCase Use case to fetch supply details.
 * @constructor Creates an instance of [SupplyBatchListViewModel].
 * @throws Exception If an error occurs during initialization.
 * @see ViewModel
 * @see Result
 *
 */
@HiltViewModel
class SupplyBatchListViewModel
    @Inject
    constructor(
        private val getSupplyBatchUseCase: GetSupplyBatchUseCase,
        private val getSupplyBatchListUseCase: GetSupplyBatchListUseCase,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(SupplyBatchListUiState())
        val uiState: StateFlow<SupplyBatchListUiState> = _uiState.asStateFlow()

        /**
         * Loads the supply batch list for the given supply and optional expiration date.
         */
        fun getSupplyBatch(
            expirationDate: String,
            idSupply: String,
        ) {
            Log.d("SupplyBatchVM", "getSupplyBatch called expirationDate=$expirationDate idSupply=$idSupply")
            viewModelScope.launch {
                getSupplyBatchUseCase(expirationDate, idSupply).collect { result ->
                    when (result) {
                        is Result.Loading -> {
                            Log.d("SupplyBatchVM", "result=Loading")
                            _uiState.update { state -> state.copy(isLoading = true) }
                        }
                        is Result.Success -> {
                            Log.d("SupplyBatchVM", "result=Success size=${result.data.batch.size}")
                            _uiState.update { state -> state.copy(supplyBatchList = result.data, isLoading = false, error = null) }
                        }
                        is Result.Error -> {
                            Log.d("SupplyBatchVM", "result=Error message=${result.exception.message}")
                            _uiState.update { state -> state.copy(error = result.exception.message, isLoading = false) }
                        }
                    }
                }
            }
        }

        /**
         * Fetches supply details (name) for the given supply id and stores it in UI state.
         */
        fun getSupplyDetails(idSupply: String) {
            viewModelScope.launch {
                getSupplyBatchListUseCase(idSupply).collect { result ->
                    when (result) {
                        is Result.Loading -> {
                            // no-op for name fetch
                        }
                        is Result.Success -> {
                            _uiState.update { state ->
                                state.copy(supplyName = result.data.name)
                            }
                        }
                        is Result.Error -> {
                            // keep existing name or null
                        }
                    }
                }
            }
        }

        fun clearState() {
            _uiState.value = SupplyBatchListUiState()
        }
    }
