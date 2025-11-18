package com.app.arcabyolimpo.presentation.screens.beneficiary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.arcabyolimpo.data.remote.dto.filter.FilterDto
import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.model.beneficiaries.Beneficiary
import com.app.arcabyolimpo.domain.usecase.beneficiaries.FilterBeneficiariesUseCase
import com.app.arcabyolimpo.domain.usecase.beneficiaries.GetBeneficiariesListUseCase
import com.app.arcabyolimpo.domain.usecase.beneficiaries.GetDisabilitesUseCase
import com.app.arcabyolimpo.domain.usecase.beneficiaries.SearchBeneficiariesUseCase
import com.app.arcabyolimpo.domain.usecase.supplies.FilterSuppliesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BeneficiaryListViewModel
    @Inject
    constructor(
        private val getBeneficiariesListUseCase: GetBeneficiariesListUseCase,
        private val getDisabilitiesUseCase: GetDisabilitesUseCase,
        private val filterBeneficiariesUseCase: FilterBeneficiariesUseCase,
        private val searchBeneficiariesUseCase: SearchBeneficiariesUseCase,
    ) : ViewModel() {
        // Internal state for the full list from the API
        private val _beneficiaries = MutableStateFlow<List<Beneficiary>>(emptyList())

        // Internal state for search text
        private val _searchText = MutableStateFlow("")

        // Internal state for loading and error
        private val _isLoading = MutableStateFlow(false)
        private val _error = MutableStateFlow<String?>(null)

        // ---------------------------
        // UI STATE #2 - FILTROS
        // ---------------------------
        private val _uiFiltersState = MutableStateFlow(BeneficiaryFilterUiState())
        val uiFiltersState: StateFlow<BeneficiaryFilterUiState> = _uiFiltersState.asStateFlow()

        // Public, combined UI state
        val uiState =
            combine(
                _beneficiaries,
                _searchText,
                _isLoading,
                _error,
            ) { beneficiaries, text, isLoading, error ->
                BeneficiaryListUiState(
                    isLoading = isLoading,
                    beneficiaries = beneficiaries,
                    error = error,
                    searchText = text,
                )
            }.stateIn(
                // Keep the flow alive for 5 seconds after the screen stops collecting
                scope = viewModelScope,
                started =
                    kotlinx.coroutines.flow.SharingStarted
                        .WhileSubscribed(5000),
                initialValue = BeneficiaryListUiState(),
            )

        init {
            loadDisabilities()
            // Load data when the ViewModel is created
            getBeneficiaries()
            _searchText
                .drop(1)
                .debounce(350L)
                .distinctUntilChanged()
                .onEach { text ->
                    if (text.isBlank()) {
                        getBeneficiaries()
                    } else {
                        performSearch(text)
                    }
                }.launchIn(viewModelScope)
        }

        /**
         * Fetches the list of beneficiaries from the repository via the use case.
         */
        fun getBeneficiaries() {
            getBeneficiariesListUseCase()
                .onEach { result ->
                    when (result) {
                        is Result.Loading -> {
                            _isLoading.update { true }
                            _error.update { null }
                        }

                        is Result.Success -> {
                            _isLoading.update { false }
                            _beneficiaries.update { result.data ?: emptyList() }
                        }

                        is Result.Error -> {
                            _isLoading.update { false }
                            _error.update { result.exception?.message ?: "Unknown error" }
                        }
                    }
                }.launchIn(viewModelScope)
        }

        // =====================================================================
        //                              FILTROS
        // =====================================================================

        fun loadDisabilities() {
            viewModelScope.launch {
                getDisabilitiesUseCase().collect { result ->
                    _uiFiltersState.update { state ->
                        when (result) {
                            is Result.Loading -> state.copy(isLoading = true)

                            is Result.Success ->
                                state.copy(
                                    isLoading = false,
                                    filterData = result.data,
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

        fun updateSelectedFilters(dto: FilterDto) {
            _uiFiltersState.update { it.copy(selectedFilters = dto) }
        }

        fun clearFilters() {
            _uiFiltersState.update {
                it.copy(
                    selectedFilters =
                        FilterDto(
                            filters = emptyMap(),
                            order = "ASC",
                        ),
                )
            }
        }

        fun filterBeneficiary(filters: FilterDto) {
            // Guardamos la selección en el UIState
            _uiFiltersState.update { it.copy(selectedFilters = filters) }

            viewModelScope.launch {
                filterBeneficiariesUseCase(filters).collect { result ->
                    when (result) {
                        is Result.Loading -> {
                            _uiFiltersState.update { it.copy(isLoading = true) }
                        }

                        is Result.Success -> {
                            println("Result" + result.data)
                            _uiFiltersState.update { state ->
                                state.copy(
                                    result = result.data,
                                    isLoading = false,
                                    error = null,
                                )
                            }
                        }

                        is Result.Error -> {
                            _uiFiltersState.update { state ->
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
         * Function for the search.
         * Calls the UseCase and updates the UI state accordingly.
         */
        private fun performSearch(query: String) {
            searchBeneficiariesUseCase(query)
                .onEach { result ->
                    when (result) {
                        is Result.Loading -> {
                            _isLoading.update { true }
                            _error.update { null }
                        }

                        is Result.Success -> {
                            _isLoading.update { false }
                            _beneficiaries.update { result.data }
                        }

                        is Result.Error -> {
                            _isLoading.update { false }
                            _error.update { result.exception.message ?: "Error en la busqueda" }
                        }
                    }
                }.launchIn(viewModelScope)
        }

        /**
         * Its called from the UI when text changes
         */
        fun onSearchTextChange(text: String) {
            _searchText.update { text }
        }
    }
