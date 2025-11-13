package com.app.arcabyolimpo.presentation.screens.workshop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.usecase.workshops.GetWorkshopsListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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
class WorkshopsListViewModel@Inject
constructor(
    private val getWorkshopsListUseCase: GetWorkshopsListUseCase
): ViewModel(){

    /** Backing property for the workshops list UI state. */
    private val _uiState = MutableStateFlow(WorkshopsListUiState())
    val uiState: StateFlow<WorkshopsListUiState> = _uiState.asStateFlow()

    init{
        loadWorkshopsList()
    }
    fun loadWorkshopsList(){
        viewModelScope.launch {
            getWorkshopsListUseCase().collect{ result ->
                _uiState.update { state ->
                    when (result) {
                        is Result.Loading ->
                            state.copy(
                                isLoading = true,
                            )
                        is Result.Success ->
                            state.copy(
                                workshopsList = result.data,
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