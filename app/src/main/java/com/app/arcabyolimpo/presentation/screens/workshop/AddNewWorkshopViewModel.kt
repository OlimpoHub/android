package com.app.arcabyolimpo.presentation.screens.workshop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.usecase.workshops.PostAddNewWorkshop
import com.app.arcabyolimpo.data.remote.dto.workshops.WorkshopDto
import com.app.arcabyolimpo.data.remote.dto.workshops.WorkshopFormData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject


// Para mock data
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

    private val _uiState = MutableStateFlow(AddNewWorkshopUiState())
    val uiState: StateFlow<AddNewWorkshopUiState> = _uiState.asStateFlow()

    private val _formData = MutableStateFlow(WorkshopFormData())
    val formData: StateFlow<WorkshopFormData> = _formData.asStateFlow()

    // Estados para los dropdowns (mockdata)
    private val _trainings = MutableStateFlow<List<Training>>(emptyList())
    val trainings: StateFlow<List<Training>> = _trainings.asStateFlow()
    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users.asStateFlow()

    // Cargar capacitaciones Mockdata
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
                        id = "a6a4dc6e-29f3-4c34-bd3c-4c8c74a5a550",
                        name = "Repostería"
                    ),
                    Training(
                        id = "a6a4dc6e-29f3-4c34-bd3c-4c8c74a5a550",
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

    // Cargar usuarios Mockdata
    fun loadUsers() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                val mockUsers = listOf(
                    User(
                        id = "4e3d1a59-2ac1-4a5e-bb77-3b238bdfc50f",
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
                        id = "4e3d1a59-2ac1-4a5e-bb77-3b238bdfc50f",
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
        // Validar datos antes de enviar
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
                schedule = _formData.value.schedule,
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

    // Actualiza un campo específico del formulario
    fun updateFormData(update: WorkshopFormData.() -> WorkshopFormData) {
        _formData.update { it.update() }
    }

    // Validación del formulario
    private fun validateForm(): Boolean {
        val data = _formData.value
        return when {
            data.name.isBlank() -> {
                _uiState.update { it.copy(error = "El nombre del taller no puede estar vacío") }
                false
            }
            data.idTraining.isBlank() -> {
                _uiState.update { it.copy(error = "Debes seleccionar una capacitación") }
                false
            }
            data.startHour.isBlank() -> {
                _uiState.update { it.copy(error = "La hora de inicio es requerida") }
                false
            }
            data.finishHour.isBlank() -> {
                _uiState.update { it.copy(error = "La hora de salida es requerida") }
                false
            }
            data.date.toString().isBlank() -> {
                _uiState.update { it.copy(error = "La fecha es requerida") }
                false
            }
            data.schedule.isBlank() -> {
                _uiState.update { it.copy(error = "El horario es requerido") }
                false
            }
            data.idUser.isBlank() -> {
                _uiState.update { it.copy(error = "Debes seleccionar un usuario") }
                false
            }
            else -> {
                _uiState.update { it.copy(error = null) }
                true
            }
        }
    }
}