package com.app.arcabyolimpo.presentation.screens.workshop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.arcabyolimpo.data.mapper.workshops.toWorkshopDto
import com.app.arcabyolimpo.data.remote.dto.workshops.WorkshopFormData
import com.app.arcabyolimpo.domain.model.workshops.Workshop
import com.app.arcabyolimpo.domain.repository.workshops.WorkshopRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ModifyWorkshopViewModel @Inject constructor(
    private val repository: WorkshopRepository
) : ViewModel() {

    private val _trainings = MutableStateFlow<List<Training>>(emptyList())
    val trainings = _trainings.asStateFlow()

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users = _users.asStateFlow()

    private val _fieldErrors = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val fieldErrors = _fieldErrors.asStateFlow()

    private val _uiState = MutableStateFlow(ModifyWorkshopsUiState())
    val uiState: StateFlow<ModifyWorkshopsUiState> = _uiState.asStateFlow()

    private val _formData = MutableStateFlow(WorkshopFormData())
    val formData: StateFlow<WorkshopFormData> = _formData.asStateFlow()

    fun loadWorkshop(workshopId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val workshop = repository.getWorkshopsById(workshopId)
                workshop?.let { fillFormWithWorkshopData(it) }
                _uiState.value = _uiState.value.copy(
                    modifyWorkshop = workshop,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    fun modifyWorkshop() {
        if (!validateForm()) return

        viewModelScope.launch {
            try {
                val dto = _formData.value.toWorkshopDto()
                repository.modifyWorkshops(dto)
                _uiState.update { it.copy(isSuccess = true, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun loadTrainings() {
        _trainings.value = listOf(
            Training("a6a4dc6e-29f3-4c34-bd3c-4c8c74a5a550", "Panadería"),
            Training("f05468dc-bda0-11f0-b6b8-020161fa237d", "Repostería"),
            Training("f0546abd-bda0-11f0-b6b8-020161fa237d", "Cocina")
        )
    }

    fun loadUsers() {
        _users.value = listOf(
            User("13fc9277-bda1-11f0-b6b8-020161fa237d", "Juan", "Pérez", ""),
            User("4e3d1a59-2ac1-4a5e-bb77-3b238bdfc50f", "María", "González", ""),
            User("dd03051b-bcfa-11f0-b6b8-020161fa237d", "Carlos", "Rodríguez", "")
        )
    }

    fun updateFormData(update: WorkshopFormData.() -> WorkshopFormData) {
        _formData.update { it.update() }
    }

    fun fillFormWithWorkshopData(workshop: Workshop) {
        println("Llenando formData con: $workshop")
        _formData.value = formData.value.copy(
            name = workshop.nameWorkshop ?: "",
            idTraining = workshop.idTraining ?: "",
            startHour = workshop.startHour ?: "",
            finishHour = workshop.finishHour ?: "",
            date = workshop.date ?: "",
            description = workshop.description ?: "",
            idUser = workshop.idUser ?: "",
            image = workshop.url ?: ""
        )
    }


    private fun validateForm(): Boolean {
        val data = _formData.value
        val errors = mutableMapOf<String, Boolean>()
        if (data.name.isBlank()) errors["name"] = true
        if (data.idTraining.isBlank()) errors["idTraining"] = true
        if (data.idUser.isBlank()) errors["idUser"] = true
        if (data.startHour.isBlank()) errors["startHour"] = true
        if (data.finishHour.isBlank()) errors["finishHour"] = true
        if (data.date.isBlank()) errors["date"] = true
        if (data.description.isBlank()) errors["description"] = true
        _fieldErrors.value = errors
        return errors.isEmpty()
    }
}
