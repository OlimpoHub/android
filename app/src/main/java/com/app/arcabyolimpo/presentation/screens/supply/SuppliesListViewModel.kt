package com.app.arcabyolimpo.presentation.screens.supply

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(SuppliesListUiState())
        val uiState: StateFlow<SuppliesListUiState> = _uiState.asStateFlow()

        init {
            loadSuppliesList()
        }

        fun loadSuppliesList() {
            viewModelScope.launch {
                getSuppliesListUseCase().collect { result ->

                    class SuppliesListViewModel
                        @Inject
                        constructor(
                            private val getSuppliesListUseCase: GetSuppliesListUseCase,
                            private val filterSuppliesUseCase: FilterSuppliesUseCase,
                        ) : ViewModel() {
                            private val _uiState = MutableStateFlow(SuppliesListUiState())
                            val uiState: StateFlow<SuppliesListUiState> = _uiState.asStateFlow()

                            init {
                                loadSuppliesList()
                            }

                            fun loadSuppliesList() {
                                viewModelScope.launch {
                                    getSuppliesListUseCase().collect { result ->
                                        _uiState.update { state ->
                                            when (result) {
                                                is Result.Loading ->
                                                    state.copy(
                                                        isLoading = true,
                                                    )

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
                        }
                }
            }
        }
    }
