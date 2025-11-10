package com.app.arcabyolimpo.presentation.screens.supply

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.arcabyolimpo.data.remote.dto.supplies.FilterSuppliesDto
import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.usecase.supplies.FilterSuppliesUseCase
import com.app.arcabyolimpo.domain.usecase.supplies.GetSuppliesListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SuppliesListViewModel
    @Inject
    constructor(
        private val getSuppliesListUseCase: GetSuppliesListUseCase,
        private val filterSuppliesUseCase: FilterSuppliesUseCase,
    ) : ViewModel() {
        /** Backing property for the supplies list UI state. */
        private val _uiState = MutableStateFlow(SuppliesListUiState())

        /** Publicly exposed immutable state observed by the UI. */
        val uiState: StateFlow<SuppliesListUiState> = _uiState.asStateFlow()

        /** Initializes the ViewModel by loading the list of supplies. */
        init {
            loadSuppliesList()
        }

        /**
         * Fetches the list of supplies from the domain layer.
         *
         * This function collects the results emitted by [GetSuppliesListUseCase]
         * and updates the [_uiState] based on whether the operation is loading, successful, or has failed.
         */
        fun loadSuppliesList() {
            viewModelScope.launch {
                getSuppliesListUseCase().collect { result ->
                    _uiState.update { state ->
                        when (result) {
                            is Result.Loading ->
                                state.copy(isLoading = true)

                            is Result.Success ->
                                state.copy(
                                    suppliesList = result.data,
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

        fun filterSupplies(filters: FilterSuppliesDto) {
            viewModelScope.launch {
                filterSuppliesUseCase(filters).collect { result ->
                    _uiState.update { state ->
                        when (result) {
                            is Result.Loading -> state.copy(isLoading = true)
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
    }
