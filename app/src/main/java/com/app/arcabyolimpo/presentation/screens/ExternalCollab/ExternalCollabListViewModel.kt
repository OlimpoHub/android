package com.app.arcabyolimpo.presentation.screens.ExternalCollab

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.arcabyolimpo.domain.usecase.ExternalCollaborator.GetAllCollabsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExternalCollabListViewModel @Inject constructor(
    private val getAllCollabsUseCase: GetAllCollabsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExternalCollabListUiState())
    val uiState: StateFlow<ExternalCollabListUiState> = _uiState.asStateFlow()

    init {
        loadCollabs()
    }

    fun loadCollabs() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            getAllCollabsUseCase().fold(
                onSuccess = { collabs ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        collabs = collabs,
                        error = null
                    )
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Unknown error"
                    )
                }
            )
        }
    }
}