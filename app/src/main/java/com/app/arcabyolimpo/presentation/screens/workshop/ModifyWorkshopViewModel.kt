package com.app.arcabyolimpo.presentation.screens.workshop

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.arcabyolimpo.data.remote.dto.user.UserDto
import com.app.arcabyolimpo.data.remote.dto.workshops.WorkshopDto
import com.app.arcabyolimpo.data.remote.dto.workshops.WorkshopFormData
import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.model.workshops.Workshop
import com.app.arcabyolimpo.domain.repository.workshops.WorkshopRepository
import com.app.arcabyolimpo.domain.usecase.upload.PostUploadImage
import com.app.arcabyolimpo.domain.usecase.user.GetAllUsersUseCase
import com.app.arcabyolimpo.domain.usecase.workshops.GetWorkshopsListUseCase
import com.app.arcabyolimpo.domain.usecase.workshops.PostModifyWorkshop
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

/**
 * ViewModel responsible for managing the UI state of the Modify Workshop screen.
 *
 * This class handles the entire workflow needed to modify an existing workshop, including:
 * - Fetching the workshop data to be edited.
 * - Retrieving the list of available users who can be assigned as instructors.
 * - Uploading a new image for the workshop when required.
 * - Sending the updated workshop information to the backend.
 *
 * It exposes a [StateFlow] of [ModifyWorkshopUiState] that the UI observes to reactively display
 * loading changes, errors, success states, and workshop information.
 *
 * This ViewModel interacts with multiple use cases and repositories from the domain and data layers:
 *
 * @property postModifyWorkshop Use case responsible for submitting the updated workshop data to the server.
 * @property getAllUsersUseCase Use case that retrieves the list of active users (e.g., instructors).
 * @property repository Repository used to fetch existing workshop details from the backend.
 * @property postUploadImage Use case that handles uploading a new workshop image to the server.
 * @property context Application context used for accessing resources or file utilities.
 */

@HiltViewModel
class ModifyWorkshopViewModel @Inject constructor(
    private val postModifyWorkshop: PostModifyWorkshop,
    private val getAllUsersUseCase: GetAllUsersUseCase,
    private val repository: WorkshopRepository,
    private val postUploadImage: PostUploadImage,
    @ApplicationContext private val context: Context
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

    private val _selectedImageUri = MutableStateFlow<Uri?>(null)
    val selectedImageUri: StateFlow<Uri?> = _selectedImageUri.asStateFlow()
    private val regexValidation = Regex("^[a-zA-Z0-9 áéíóúÁÉÍÓÚñÑ]*$")
    private val urlTypingRegex = Regex("^[a-zA-Z0-9:/.?=&_\\-]*$")

    fun validateInput(text: String, regex: Regex, maxLength: Int): Boolean
    {
        if (text.isEmpty()) {return true}
        return text.length <= maxLength && regex.matches(text)
    }


    fun setSelectedImageUri(uri: Uri?) {
        _selectedImageUri.value = uri
    }

    fun getFileFromUri(context: Context, uri: Uri): File? {
        return try {
            val contentResolver = context.contentResolver
            val tempFile = File(context.cacheDir, "upload_temp_${System.currentTimeMillis()}")
            contentResolver.openInputStream(uri)?.use { inputStream ->
                FileOutputStream(tempFile).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            tempFile
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
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

                Log.d("WORKSHOP_DEBUG", "Workshop: ${workshopData}")
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
                            image = workshopData.url ?: ""
                        )
                    }
                    _formattedDate.value = formatWorkshopDate(workshopData.date)
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

            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            zonedDateTime.format(formatter)
        } catch (e: Exception) {

            if (dateString.matches(Regex("^\\d{4}-\\d{2}-\\d{2}"))) {
                val parts = dateString.split("-")
                "${parts[2]}/${parts[1]}/${parts[0]}"
            } else {
                Log.e("WORKSHOP_DEBUG", "Error al formatear o validar fecha: $dateString", e)
                ""
            }
        }
    }


    fun modifyWorkshop(idWorkshop: String) {
        if (!validateForm()) return
        Log.d("DEBUG_SAVE", "Intentando guardar -> Nombre: ${_formData.value.name}, Desc: ${_formData.value.description}")
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, isSuccess = false) }

            val imageUri = _selectedImageUri.value
            var remoteImageUrl: String? = _formData.value.image
            var uploadError: String? = null

            if (imageUri != null) {
                val fileToUpload = getFileFromUri(context, imageUri)

                if (fileToUpload == null) {
                    uploadError = "Error al preparar la imagen para la subida."
                } else {
                    val uploadResult = postUploadImage(fileToUpload)
                        .let { flow ->
                            flow.first { it !is Result.Loading }
                        }

                    when (uploadResult) {
                        is Result.Success -> {
                            remoteImageUrl = uploadResult.data.url
                            fileToUpload.delete()
                        }
                        is Result.Error -> {
                            uploadError = "Error al subir la imagen: ${uploadResult.exception.message}"
                            fileToUpload.delete()
                        }
                        is Result.Loading -> { }
                    }
                }

                if (uploadError != null) {
                    _uiState.update { it.copy(isLoading = false, error = uploadError) }
                    return@launch
                }
            }

            val workshopDto = WorkshopDto(
                id = idWorkshop,
                name = _formData.value.name,
                startHour = _formData.value.startHour,
                finishHour = _formData.value.finishHour,
                status = 1,
                idUser = _formData.value.idUser,
                description = _formData.value.description,
                date = _formData.value.date,
                image = remoteImageUrl
            )
            Log.d("WORKSHOP_DEBUG", "Fecha: ${_formData?.value?.date}")
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
    /** Function that updates the form data, making sure that it follows the regex and dosen't
     * exceed the char number */

    fun updateFormData(update: WorkshopFormData.() -> WorkshopFormData) {
        val currentState = _formData.value
        val newState = currentState.update()
        if (currentState.name != newState.name) {
            if (!validateInput(newState.name,regexValidation, 50)) return
        }
        if (currentState.description != newState.description) {
            if (!validateInput(newState.name,regexValidation, 400)) return
        }
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

    private fun isValidUrl(url: String?): Boolean {
        if (url.isNullOrBlank()) return false

        val regex = Regex(
            pattern = "^(https?://)([\\w.-]+)\\.([a-z\\.]{2,6})([/\\w .-]*)*/?$",
            options = setOf(RegexOption.IGNORE_CASE)
        )

        return regex.matches(url)
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