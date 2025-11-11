package com.app.arcabyolimpo.presentation.screens.supply.supplyDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.usecase.supplies.GetSupplyBatchListUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class SuppliesDetailViewModel
    @Inject
    constructor(
        private val getSupplyBatchListUseCase: GetSupplyBatchListUseCase,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(SuppliesDetailUiState())
        val uiState: StateFlow<SuppliesDetailUiState> = _uiState.asStateFlow()

        fun getSupply(id: String) {
            viewModelScope.launch {
                getSupplyBatchListUseCase(id).collect { result ->
                    _uiState.update { state ->
                        when (result) {
                            is Result.Loading ->
                                state.copy(
                                    isLoading = true,
                                )
                            is Result.Success ->
                                state.copy(
                                    supplyBatchList = result.data,
                                    isLoading = false,
                                    error = null,
                                )
                            is Result.Error ->
                                state.copy(
                                    error = result.exception.message,
                                    isLoading = false,
                                )
                        }
                    }
                }
            }
        }
    }
