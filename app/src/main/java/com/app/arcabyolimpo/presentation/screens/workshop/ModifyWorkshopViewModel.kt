package com.app.arcabyolimpo.presentation.screens.workshop

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.arcabyolimpo.data.remote.dto.user.UserDto
import com.app.arcabyolimpo.data.remote.dto.workshops.WorkshopDto
import com.app.arcabyolimpo.data.remote.dto.workshops.WorkshopFormData
import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.model.workshops.Workshop
import com.app.arcabyolimpo.domain.repository.workshops.WorkshopRepository
import com.app.arcabyolimpo.domain.usecase.user.GetAllUsersUseCase
import com.app.arcabyolimpo.domain.usecase.workshops.PostModifyWorkshop
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ModifyWorkshopViewModel @Inject constructor(
    private val postModifyWorkshop: PostModifyWorkshop,
    private val getAllUsersUseCase: GetAllUsersUseCase,
    private val repository: WorkshopRepository,
) : ViewModel(){

    private val _uiState = MutableStateFlow(ModifyWorkshopUiState())
    val uiState: StateFlow<ModifyWorkshopUiState> = _uiState.asStateFlow()
    private val _formData = MutableStateFlow(WorkshopFormData())
    val formData: StateFlow<WorkshopFormData> = _formData.asStateFlow()
    private val _fieldErrors = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val fieldErrors: StateFlow<Map<String, Boolean>> = _fieldErrors.asStateFlow()

    private val _users = MutableStateFlow<List<UserDto>>(emptyList())
    val users: StateFlow<List<UserDto>> = _users.asStateFlow()

    private val _formattedDate = MutableStateFlow("")
    val formattedDate: StateFlow<String> = _formattedDate.asStateFlow()

    private val _usersLoading = MutableStateFlow(false)
    val usersLoading: StateFlow<Boolean> = _usersLoading.asStateFlow()

    private val _usersError = MutableStateFlow<String?>(null)
    val usersError: StateFlow<String?> = _usersError.asStateFlow()

    private val _workLoadError = MutableStateFlow<String?>(null)
    val workLoadError: StateFlow<String?> = _workLoadError.asStateFlow()

    private val _workshop = MutableStateFlow<Workshop?>(null)
    val workshop: StateFlow<Workshop?> = _workshop.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()


    fun loadUsers() {
        viewModelScope.launch {
            _usersLoading.value = true
            _usersError.value = null

            try {
                val result = getAllUsersUseCase()
                when (result) {
                    is com.app.arcabyolimpo.domain.common.Result.Success -> {
                        _users.value = result.data
                        _usersError.value = null
                    }
                    is com.app.arcabyolimpo.domain.common.Result.Error -> {
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
    fun loadWorkshop(idWorkshop: String) {
        if (idWorkshop.isBlank()) {
            _workLoadError.value = "ID del taller no válido:" + idWorkshop
            _isLoading.value = false
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val workshopData = repository.getWorkshopsById(idWorkshop)
                _workshop.value = workshopData
                Log.d("WORKSHOP_DEBUG", "Hora Inicio (Cruda): ${workshopData?.startHour}")
                if (workshopData != null) {
                    val cleanDate = formatWorkshopDate(workshopData.date)
                    _formData.update {
                        it.copy(
                            name = workshopData.nameWorkshop ?: "",
                            startHour = formatHour(workshopData.startHour) ?: "",
                            finishHour = formatHour(workshopData.finishHour) ?: "",
                            date = cleanDate ?: "",
                            description = workshopData.description ?: "",
                            idUser = workshopData.idUser ?: "",
                            image = workshopData.url ?: "",
                            videoTraining = workshopData.videoTraining ?: ""
                        )
                    }
                    _formattedDate.value = formatWorkshopDate(workshopData.date)
                    Log.d("WORKSHOP_DEBUG", "  startHour: ${_formData.value.startHour}")
                } else {
                    _workLoadError.value = "No se encontró el taller"
                }
            } catch (e: Exception) {
                _workLoadError.value = "Error al cargar el taller: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    private fun formatWorkshopDate(dateString: String?): String {
        if (dateString.isNullOrEmpty()) {
            return ""
        }

        return try {
            val instant = Instant.parse(dateString)
            val zonedDateTime = instant.atZone(ZoneId.systemDefault())

            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            zonedDateTime.format(formatter)
        } catch (e: Exception) {

            if (dateString.matches(Regex("^\\d{4}-\\d{2}-\\d{2}"))) {
                dateString
            } else {
                Log.e("WORKSHOP_DEBUG", "Error al formatear o validar fecha: $dateString", e)
                ""
            }
        }
    }


    fun modifyWorkshop(idWorkshop: String) {
        if (!validateForm()) return

        viewModelScope.launch {
            val workshopDto = WorkshopDto(
                id = idWorkshop,
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
            postModifyWorkshop(workshopDto).collect { result ->
                _uiState.update { state ->
                    when (result) {
                        is Result.Loading -> state.copy(
                            isLoading = true,
                            error = null,
                            isSuccess = false
                        )

                        is Result.Success -> state.copy(
                            modifiedWorkshop = workshopDto,
                            isLoading = false,
                            error = null,
                            isSuccess = true
                        )

                        is Result.Error -> state.copy(
                            isLoading = false,
                            error = result.exception.message ?: "Error al modificar el workshop",
                            isSuccess = false
                        )
                    }
                }
            }
        }
    }
    private fun formatHour(hourString: String?): String {
        return hourString?.takeIf { it.length >= 5 }?.substring(0, 5) ?: ""
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