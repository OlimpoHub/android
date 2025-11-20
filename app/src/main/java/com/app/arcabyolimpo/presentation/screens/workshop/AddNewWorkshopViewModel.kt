package com.app.arcabyolimpo.presentation.screens.workshop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.arcabyolimpo.data.remote.dto.user.UserDto
import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.usecase.workshops.PostAddNewWorkshop
import com.app.arcabyolimpo.data.remote.dto.workshops.WorkshopDto
import com.app.arcabyolimpo.data.remote.dto.workshops.WorkshopFormData
import com.app.arcabyolimpo.domain.usecase.user.GetAllUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddNewWorkshopViewModel @Inject constructor(
    private val postAddNewWorkshop: PostAddNewWorkshop,
    private val getAllUsersUseCase: GetAllUsersUseCase
) : ViewModel() {

    /** Backing property for the workshops UI state. */
    private val _uiState = MutableStateFlow(AddNewWorkshopUiState())
    val uiState: StateFlow<AddNewWorkshopUiState> = _uiState.asStateFlow()
    private val _formData = MutableStateFlow(WorkshopFormData())
    val formData: StateFlow<WorkshopFormData> = _formData.asStateFlow()
    private val _fieldErrors = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val fieldErrors: StateFlow<Map<String, Boolean>> = _fieldErrors.asStateFlow()


    private val _users = MutableStateFlow<List<UserDto>>(emptyList())
    val users: StateFlow<List<UserDto>> = _users.asStateFlow()

    private val _usersLoading = MutableStateFlow(false)
    val usersLoading: StateFlow<Boolean> = _usersLoading.asStateFlow()

    private val _usersError = MutableStateFlow<String?>(null)
    val usersError: StateFlow<String?> = _usersError.asStateFlow()

    fun loadUsers() {
        viewModelScope.launch {
            _usersLoading.value = true
            _usersError.value = null

            try {
                val result = getAllUsersUseCase()
                when (result) {
                    is Result.Success -> {
                        _users.value = result.data
                        _usersError.value = null
                    }
                    is Result.Error -> {
                        _usersError.value = "Error al cargar usuarios: ${result.exception.message}"
                        _users.value = emptyList()
                    }
                    is Result.Loading -> {

                    }
                }
            } catch (e: Exception) {
                _usersError.value = "Error al cargar usuarios: ${e.message}"
                _users.value = emptyList()
            } finally {
                _usersLoading.value = false
            }
        }
    }

    fun addNewWorkshop() {
        if (!validateForm()) return

        viewModelScope.launch {
            val workshopDto = WorkshopDto(
                id = UUID.randomUUID().toString(),
                name = _formData.value.name,
                startHour = _formData.value.startHour,
                finishHour = _formData.value.finishHour,
                status = 1,
                idUser = _formData.value.idUser,
                description = _formData.value.description,
                date = _formData.value.date,
                image = _formData.value.image,
                videoTraining = _formData.value.videoTraining
            )

            postAddNewWorkshop(workshopDto).collect { result ->
                _uiState.update { state ->
                    when (result) {
                        is Result.Loading -> state.copy(
                            isLoading = true,
                            error = null,
                            isSuccess = false
                        )

                        is Result.Success -> state.copy(
                            addNewWorkshop = workshopDto,
                            isLoading = false,
                            error = null,
                            isSuccess = true
                        )

                        is Result.Error -> state.copy(
                            isLoading = false,
                            error = result.exception.message ?: "Error al crear el workshop",
                            isSuccess = false
                        )
                    }
                }
            }
        }
    }

    fun updateFormData(update: WorkshopFormData.() -> WorkshopFormData) {
        _formData.update { it.update() }
        clearFieldErrors()
    }

    fun resetForm() {
        _formData.value = WorkshopFormData()
        _fieldErrors.value = emptyMap()
        _uiState.update { it.copy(isSuccess = false, error = null) }
    }

    private fun clearFieldErrors() {
        _fieldErrors.value = emptyMap()
    }

    private fun validateForm(): Boolean {
        val data = _formData.value
        val errors = mutableMapOf<String, Boolean>()
        val hourRegex = Regex("^([01]?\\d|2[0-3]):[0-5]\\d$")
        val dateRegex = Regex("^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/\\d{4}$")

        if (data.name.isBlank()) errors["name"] = true
        if (data.startHour.isBlank()) errors["startHour"] = true
        if (data.finishHour.isBlank()) errors["finishHour"] = true
        if (data.date.isBlank()) errors["date"] = true
        if (data.description.isBlank()) errors["description"] = true
        if (data.idUser.isBlank()) errors["idUser"] = true
        if (data.videoTraining.isBlank()) errors["videoTraining"] = true

        if (data.startHour.isNotBlank() && !hourRegex.matches(data.startHour)) {
            errors["startHour"] = true
        }
        if (data.finishHour.isNotBlank() && !hourRegex.matches(data.finishHour)) {
            errors["finishHour"] = true
        }
        if (data.date.isNotBlank() && !dateRegex.matches(data.date)) {
            errors["date"] = true
        }
        _fieldErrors.value = errors
        return errors.isEmpty()
    }
}

