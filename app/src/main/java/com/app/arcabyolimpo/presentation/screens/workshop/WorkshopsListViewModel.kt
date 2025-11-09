package com.app.arcabyolimpo.presentation.screens.workshop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.usecase.supplies.GetSuppliesListUseCase
import com.app.arcabyolimpo.domain.usecase.workshops.GetWorkshopsListUseCase
import com.app.arcabyolimpo.presentation.screens.supply.SuppliesListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkshopsListViewModel@Inject
constructor(
    private val getWorkshopsListUseCase: GetWorkshopsListUseCase
): ViewModel(){
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