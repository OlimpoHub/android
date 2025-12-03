package com.app.arcabyolimpo.presentation.screens.capacitations

/**
 * ViewModel responsible for managing the UI state of the Disability Detail screen.
 *
 * @property GetDisabilityUseCase Use case for retrieving the existing detail of disability.
 */

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.usecase.disabilities.DeleteDisabilityUseCase
import com.app.arcabyolimpo.domain.usecase.disabilities.GetDisabilityUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DisabilityDetailViewModel
@Inject
constructor(
    private val getDisabilityUseCase: GetDisabilityUseCase,
    private val savedStateHandle: SavedStateHandle,
    private val deleteDisabilityUseCase: DeleteDisabilityUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(DisabilityDetailUiState())
    val uiState: StateFlow<DisabilityDetailUiState> = _uiState.asStateFlow()

    private var disabilityId: String? = null

    init {
        disabilityId = savedStateHandle.get<String>("disabilityId")
        if (disabilityId != null) {
            loadDisability(disabilityId!!)
        } else {
            _uiState.update { it.copy(screenError = "Discapacidad no encontrada") }
        }
    }

    fun loadDisability(id: String) {
        viewModelScope.launch {
            getDisabilityUseCase(id).collect { result ->
                _uiState.update { state ->
                    when (result) {
                        is com.app.arcabyolimpo.domain.common.Result.Loading ->
                            state.copy(isScreenLoading = true)
                        is com.app.arcabyolimpo.domain.common.Result.Success ->
                            state.copy(
                                isScreenLoading = false,
                                disability = result.data,
                                screenError = null,
                            )
                        is com.app.arcabyolimpo.domain.common.Result.Error ->
                            state.copy(
                                isScreenLoading = false,
                                screenError = result.exception.message,
                            )
                    }
                }
            }
        }
    }

    fun onDeleteClicked() {
        if (disabilityId == null) return

        viewModelScope.launch {
            deleteDisabilityUseCase(disabilityId!!).collect { result ->
                _uiState.update { state ->
                    when (result) {
                        is com.app.arcabyolimpo.domain.common.Result.Loading ->
                            state.copy(isDeleting = true)
                        is com.app.arcabyolimpo.domain.common.Result.Success ->
                            state.copy(
                                isDeleting = false,
                                deleteSuccess = true,
                                deleteError = null,
                            )
                        is Result.Error ->
                            state.copy(
                                isDeleting = false,
                                deleteError = result.exception.message,
                            )
                    }
                }
            }
        }
    }

    fun onDeletionErrorShown() {
        _uiState.update { it.copy(deleteError = null) }
    }
}