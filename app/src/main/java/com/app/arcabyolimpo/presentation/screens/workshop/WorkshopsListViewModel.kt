package com.app.arcabyolimpo.presentation.screens.workshop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.model.workshops.Workshop
import com.app.arcabyolimpo.domain.usecase.workshops.GetWorkshopsListUseCase
import com.app.arcabyolimpo.domain.usecase.workshops.SearchWorkshopsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

/**
 * ViewModel responsible for managing the UI state of the workshops list screen.
 *
 * This class interacts with the [GetWorkshopsListUseCase] to fetch data from the domain layer
 * and exposes a [StateFlow] of [WorkshopsListUiState] that the UI observes to render updates.
 *
 * @property getWorkshopsListUseCase Use case for retrieving the list of workshops.
 */
@HiltViewModel
class WorkshopsListViewModel @Inject constructor(
    private val getWorkshopsListUseCase: GetWorkshopsListUseCase,
    private val searchWorkshopsUseCase: SearchWorkshopsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(WorkshopsListUiState())
    val uiState: StateFlow<WorkshopsListUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private var originalWorkshopsList: List<Workshop> = emptyList()

    init {
        loadWorkshopsList()
        setupSearchDebounce()
    }

    fun onSearchQueryChange(text: String) {
        _searchQuery.value = text
        if (text.isBlank()) {
            _uiState.update { state ->
                state.copy(
                    workshopsList = originalWorkshopsList,
                    isLoading = false,
                    error = null
                )
            }
        }
    }

    fun loadWorkshopsList() {
        viewModelScope.launch {
            getWorkshopsListUseCase().collect { result ->
                _uiState.update { state ->
                    when (result) {
                        is Result.Loading ->
                            state.copy(isLoading = true, error = null)

                        is Result.Success -> {
                            originalWorkshopsList = result.data
                            state.copy(
                                workshopsList = result.data,
                                isLoading = false,
                                error = null
                            )
                        }

                        is Result.Error ->
                            state.copy(
                                error = result.exception.message,
                                isLoading = false
                            )
                    }
                }
            }
        }
    }

    private fun setupSearchDebounce() {
        viewModelScope.launch {
            _searchQuery
                .debounce(400)
                .distinctUntilChanged()
                .collect { query ->
                    if (query.isNotBlank()) {
                        performSearch(query)
                    }
                }
        }
    }

    private fun performSearch(name: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            searchWorkshopsUseCase(name).collect { result ->
                _uiState.update { state ->
                    when (result) {
                        is Result.Loading ->
                            state.copy(isLoading = true, error = null)

                        is Result.Success ->
                            state.copy(
                                workshopsList = result.data,
                                isLoading = false,
                                error = null
                            )

                        is Result.Error -> {
                            state.copy(
                                workshopsList = originalWorkshopsList,
                                error = result.exception.message,
                                isLoading = false
                            )
                        }
                    }
                }
            }
        }
    }
}