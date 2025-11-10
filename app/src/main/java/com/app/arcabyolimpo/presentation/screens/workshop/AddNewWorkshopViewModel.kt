package com.app.arcabyolimpo.presentation.screens.workshop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.usecase.workshops.PostAddNewWorkshop
import com.app.arcabyolimpo.data.remote.dto.workshops.WorkshopDto
import com.app.arcabyolimpo.data.remote.dto.workshops.WorkshopFormData
import com.app.arcabyolimpo.domain.usecase.workshops.GetWorkshopsListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

/**
 * ViewModel responsible for managing the UI state of the register workshops screen.
 *
 * This class interacts with the [postAddNewWorkshop] to post all the data in the domain layer
 * and exposes a [StateFlow] of [AddNewWorkshopUiState] that the UI observes to render updates.
 *
 * @property postAddNewWorkshop Use case for posting the new workshop.
 */

/** Mock Data - deleting in the future */
data class Training(
    val id: String,
    val name: String
)

data class User(
    val id: String,
    val name: String,
    val lastName: String,
    val email: String
)

@HiltViewModel
class AddNewWorkshopViewModel @Inject constructor(
    private val postAddNewWorkshop: PostAddNewWorkshop
) : ViewModel() {

    /** Backing property for the workshops UI state. */
    private val _uiState = MutableStateFlow(AddNewWorkshopUiState())
    val uiState: StateFlow<AddNewWorkshopUiState> = _uiState.asStateFlow()
    private val _formData = MutableStateFlow(WorkshopFormData())
    val formData: StateFlow<WorkshopFormData> = _formData.asStateFlow()
    private val _fieldErrors = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val fieldErrors: StateFlow<Map<String, Boolean>> = _fieldErrors.asStateFlow()

    /** Mock Data - deleting in the future */
    private val _trainings = MutableStateFlow<List<Training>>(emptyList())
    val trainings: StateFlow<List<Training>> = _trainings.asStateFlow()
    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users.asStateFlow()

    /** Mock Data - deleting in the future */
    fun loadTrainings() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                val mockTrainings = listOf(
                    Training(
                        id = "a6a4dc6e-29f3-4c34-bd3c-4c8c74a5a550",
                        name = "Panadería"
                    ),
                    Training(
                        id = "f05468dc-bda0-11f0-b6b8-020161fa237d",
                        name = "Repostería"
                    ),
                    Training(
                        id = "f0546abd-bda0-11f0-b6b8-020161fa237d",
                        name = "Cocina"
                    )
                )
                _trainings.value = mockTrainings
                _uiState.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    isLoading = false,
                    error = "Error al cargar capacitaciones: ${e.message}"
                ) }
            }
        }
    }

    /** Mock Data - deleting in the future */
    fun loadUsers() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                val mockUsers = listOf(
                    User(
                        id = "13fc9277-bda1-11f0-b6b8-020161fa237d",
                        name = "Juan",
                        lastName = "Pérez",
                        email = "juan.perez@email.com"
                    ),
                    User(
                        id = "4e3d1a59-2ac1-4a5e-bb77-3b238bdfc50f",
                        name = "María",
                        lastName = "González",
                        email = "maria.gonzalez@email.com"
                    ),
                    User(
                        id = "dd03051b-bcfa-11f0-b6b8-020161fa237d",
                        name = "Carlos",
                        lastName = "Rodríguez",
                        email = "carlos.rodriguez@email.com"
                    )
                )
                _users.value = mockUsers
                _uiState.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    isLoading = false,
                    error = "Error al cargar usuarios: ${e.message}"
                ) }
            }
        }
    }

    fun addNewWorkshop() {
        if (!validateForm()) return

        viewModelScope.launch {
            val workshopDto = WorkshopDto(
                id = UUID.randomUUID().toString(),
                idTraining = _formData.value.idTraining,
                name = _formData.value.name,
                startHour = _formData.value.startHour,
                finishHour = _formData.value.finishHour,
                status = 1,
                idUser = _formData.value.idUser,
                description = _formData.value.description,
                date = _formData.value.date,
                image = _formData.value.image
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
                            addNewWorkshop = result.data,
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
        val dateRegex = Regex("^\\d{4}-\\d{2}-\\d{2}$")

        if (data.name.isBlank()) errors["name"] = true
        if (data.idTraining.isBlank()) errors["idTraining"] = true
        if (data.startHour.isBlank()) errors["startHour"] = true
        if (data.finishHour.isBlank()) errors["finishHour"] = true
        if (data.date.isBlank()) errors["date"] = true
        if (data.description.isBlank()) errors["description"] = true
        if (data.idUser.isBlank()) errors["idUser"] = true

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