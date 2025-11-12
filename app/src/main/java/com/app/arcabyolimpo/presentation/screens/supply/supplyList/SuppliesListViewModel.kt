package com.app.arcabyolimpo.presentation.screens.supply.supplyList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.arcabyolimpo.data.remote.dto.filter.FilterDto
import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.usecase.supplies.FilterSuppliesUseCase
import com.app.arcabyolimpo.domain.usecase.supplies.GetFiltersDataUseCase
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
        private val getFiltersDataUseCase: GetFiltersDataUseCase,
    ) : ViewModel() {
        /** Backing property for the supplies list UI state. */
        private val _uiState = MutableStateFlow(SuppliesListUiState())

        /** Publicly exposed immutable state observed by the UI. */
        val uiState: StateFlow<SuppliesListUiState> = _uiState.asStateFlow()

        /** Initializes the ViewModel by loading the list of supplies. */
        init {
            loadSuppliesList()
            getSupplies()
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

        /**
         * Loads the available supply filters from the data use case.
         *
         * The resulting state is observed by the UI to show loading indicators,
         * display the filter modal, or surface errors.
         */
        fun getSupplies() {
            viewModelScope.launch {
                getFiltersDataUseCase().collect { result ->
                    _uiState.update { state ->
                        when (result) {
                            is Result.Loading ->
                                state.copy(isLoading = true)

                            is Result.Success ->
                                state.copy(
                                    filterData = result.data,
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

        /**
         * Applies the selected filters to the supplies list.
         *
         * @param filters A FilterDto object that contains the selected filters
         *                and the sort order.
         */
        fun filterSupplies(filters: FilterDto) {
            // Guardamos la selección en el UIState
            _uiState.update { it.copy(selectedFilters = filters) }

            viewModelScope.launch {
                filterSuppliesUseCase(filters).collect { result ->
                    when (result) {
                        is Result.Loading -> {
                            _uiState.update { it.copy(isLoading = true) }
                        }

                        is Result.Success -> {
                            _uiState.update { state ->
                                state.copy(
                                    suppliesList = result.data,
                                    isLoading = false,
                                    error = null,
                                )
                            }
                        }

                        is Result.Error -> {
                            _uiState.update { state ->
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

        /**
         * Resets all applied filters to their default state.
         *
         * What it does:
         * 1. Clears any selected filters by replacing them with an empty FilterDto.
         * 2. Resets the sort order back to "ASC".
         *
         * Note: This only resets the UI state's filter selection.
         *       It does NOT automatically reload the supplies list.
         */
        fun clearFilters() {
            _uiState.update {
                it.copy(
                    selectedFilters =
                        FilterDto(
                            filters = emptyMap(),
                            order = "ASC",
                        ),
                )
            }
        }
    }
