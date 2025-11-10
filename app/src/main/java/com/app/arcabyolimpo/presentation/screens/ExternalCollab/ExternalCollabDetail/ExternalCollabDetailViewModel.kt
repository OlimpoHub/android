package com.app.arcabyolimpo.presentation.screens.ExternalCollab.ExternalCollabDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.arcabyolimpo.domain.model.ExternalCollaborator.ExternalCollab
import com.app.arcabyolimpo.domain.repository.ExternalCollabRepository.ExternalCollabRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ExternalCollabDetailUiState(
    val collab: ExternalCollab? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ExternalCollabDetailViewModel @Inject constructor(
    private val repository: ExternalCollabRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val collabId: String = checkNotNull(savedStateHandle["collabId"])

    private val _uiState = MutableStateFlow(ExternalCollabDetailUiState())
    val uiState: StateFlow<ExternalCollabDetailUiState> = _uiState.asStateFlow()

    init {
        loadCollabDetail()
    }

    fun loadCollabDetail() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            repository.getCollabById(collabId)
                .onSuccess { collab ->
                    _uiState.value = _uiState.value.copy(
                        collab = collab,
                        isLoading = false
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Unknown error occurred"
                    )
                }
        }
    }
}