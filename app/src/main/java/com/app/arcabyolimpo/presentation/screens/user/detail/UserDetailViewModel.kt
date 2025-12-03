package com.app.arcabyolimpo.presentation.screens.user.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.usecase.user.GetUserByIdUseCase
import com.app.arcabyolimpo.domain.usecase.user.delete.DeleteUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDetailViewModel @Inject constructor(
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val userId: String = checkNotNull(savedStateHandle["userId"]){
        "userId parameter wasn't found in SavedStateHandle"
    }

    private val _uiState = MutableStateFlow(UserDetailUiState())
    val uiState: StateFlow<UserDetailUiState> = _uiState.asStateFlow()

    init {
        loadCollabDetail()
    }

    fun loadCollabDetail() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            getUserByIdUseCase(userId).collect { result ->
                when (result) {
                    is Result.Loading -> {
                    }
                    is Result.Success -> {
                       _uiState.value = _uiState.value.copy(
                            collab = result.data,
                            isLoading = false
                        )
                    }
                    is Result.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = result.exception.message ?: "Unknown error occurred"
                        )
                    }
                }
            }
        }
    }

    fun deleteCollabById(idString: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(deleteLoading = true, deleteError = null, deleted = false)

            deleteUserUseCase(idString).collect { result ->
                when (result) {
                    is Result.Loading -> {
                    }
                    is Result.Success -> {
                        _uiState.value = _uiState.value.copy(
                            deleteLoading = false,
                            deleted = true
                        )
                    }
                    is Result.Error -> {
                        _uiState.value = _uiState.value.copy(
                            deleteLoading = false,
                            deleteError = result.exception.message ?: "Error al eliminar"
                        )
                    }
                }
            }
        }
    }

    fun resetDeleteState() {
        _uiState.value = _uiState.value.copy(deleteLoading = false, deleteError = null, deleted = false)
    }
}