package com.app.arcabyolimpo.presentation.screens.beneficiary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.arcabyolimpo.domain.model.beneficiaries.Beneficiary
import com.app.arcabyolimpo.domain.usecase.beneficiaries.GetBeneficiariesListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.usecase.beneficiaries.SearchBeneficiariesUseCase
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

import javax.inject.Inject


@HiltViewModel
class BeneficiaryListViewModel @Inject constructor(
    private val getBeneficiariesListUseCase: GetBeneficiariesListUseCase,
    private val searchBeneficiariesUseCase: SearchBeneficiariesUseCase
) : ViewModel() {

    // Internal state for the full list from the API
    private val _beneficiaries = MutableStateFlow<List<Beneficiary>>(emptyList())

    // Internal state for search text
    private val _searchText = MutableStateFlow("")

    //Internal state for loading and error
    private val _isLoading = MutableStateFlow(false)
    private val _error = MutableStateFlow<String?>(null)

    // Public, combined UI state
    val uiState = combine(
        _beneficiaries,
        _searchText,
        _isLoading,
        _error
    ) { beneficiaries, text, isLoading, error ->
        BeneficiaryListUiState(
            isLoading = isLoading,
            beneficiaries = beneficiaries,
            error = error,
            searchText = text
        )
    }.stateIn(
        // Keep the flow alive for 5 seconds after the screen stops collecting
        scope = viewModelScope,
        started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
        initialValue = BeneficiaryListUiState()
    )

    init {
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
            }
            .launchIn(viewModelScope)
    }

    /**
     * Fetches the list of beneficiaries from the repository via the use case.
     */
    fun getBeneficiaries() {
        getBeneficiariesListUseCase().onEach { result ->
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

    /**
     * Function for the search.
     * Calls the UseCase and updates the UI state accordingly.
     */
    private fun performSearch(query: String) {
        searchBeneficiariesUseCase(query).onEach { result ->
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