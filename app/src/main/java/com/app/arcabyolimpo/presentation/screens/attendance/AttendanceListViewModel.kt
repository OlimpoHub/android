package com.app.arcabyolimpo.presentation.screens.attendance

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.usecase.attendance.GetAttendanceByUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class AttendanceListViewModel @Inject constructor(
    private val getAttendanceByUserUseCase: GetAttendanceByUserUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val userId: String = checkNotNull(savedStateHandle["userId"]) {
        "userId parameter wasn't found in SavedStateHandle for AttendanceListViewModel"
    }

    private val _uiState = MutableStateFlow(AttendanceListUiState())
    val uiState: StateFlow<AttendanceListUiState> = _uiState.asStateFlow()

    init {
        loadAttendance()
    }

    fun loadAttendance() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null,
            )

            getAttendanceByUserUseCase(userId).collect { result ->
                when (result) {
                    is Result.Loading -> {
                    }

                    is Result.Success -> {
                        android.util.Log.d(
                            "ATTENDANCE",
                            result.data.joinToString("\n") {
                                "id=${it.idAsistencia}, tallerId=${it.idTaller}, nombre=${it.nombreTaller}"
                            }
                        )
                        _uiState.value = _uiState.value.copy(
                            attendances = result.data,
                            isLoading = false,
                            error = null,
                        )
                    }

                    is Result.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = result.exception.message ?: "Error al cargar asistencias",
                        )
                    }
                }
            }
        }
    }
}
