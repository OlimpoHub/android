package com.app.arcabyolimpo.presentation.screens.workshop

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.arcabyolimpo.data.remote.dto.user.UserDto
import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.usecase.workshops.PostAddNewWorkshop
import com.app.arcabyolimpo.data.remote.dto.workshops.WorkshopDto
import com.app.arcabyolimpo.data.remote.dto.workshops.WorkshopFormData
import com.app.arcabyolimpo.domain.usecase.upload.PostUploadImage
import com.app.arcabyolimpo.domain.usecase.user.GetAllUsersUseCase
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
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddNewWorkshopViewModel @Inject constructor(
    private val postAddNewWorkshop: PostAddNewWorkshop,
    private val getAllUsersUseCase: GetAllUsersUseCase,
    private val postUploadImage: PostUploadImage,
    @ApplicationContext private val context: Context
) : ViewModel() {

    /** Backing property for the workshops UI state. */
    private val _uiState = MutableStateFlow(AddNewWorkshopUiState())
    val uiState: StateFlow<AddNewWorkshopUiState> = _uiState.asStateFlow()
    private val _formData = MutableStateFlow(WorkshopFormData())
    val formData: StateFlow<WorkshopFormData> = _formData.asStateFlow()
    private val _fieldErrors = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val fieldErrors: StateFlow<Map<String, Boolean>> = _fieldErrors.asStateFlow()

    private val _selectedImageUri = MutableStateFlow<Uri?>(null)
    val selectedImageUri: StateFlow<Uri?> = _selectedImageUri.asStateFlow()
    private val _users = MutableStateFlow<List<UserDto>>(emptyList())
    val users: StateFlow<List<UserDto>> = _users.asStateFlow()

    private val _usersLoading = MutableStateFlow(false)
    val usersLoading: StateFlow<Boolean> = _usersLoading.asStateFlow()

    private val _usersError = MutableStateFlow<String?>(null)
    val usersError: StateFlow<String?> = _usersError.asStateFlow()

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
            _uiState.update { it.copy(isLoading = true, error = null, isSuccess = false) }

            val imageUri = _selectedImageUri.value
            var remoteImageUrl: String? = null
            var uploadError: String? = null

            Log.d("WorkshopDebug", "--- INICIO ADD NEW WORKSHOP ---")
            Log.d("WorkshopDebug", "Selected Image URI: $imageUri")

            if (imageUri != null) {
                val fileToUpload = getFileFromUri(context, imageUri)

                if (fileToUpload == null) {
                    uploadError = "Error al preparar la imagen para la subida."
                    Log.e("WorkshopDebug", "ERROR: getFileFromUri devolvió NULL")
                } else {
                    Log.d("WorkshopDebug", "File to upload: ${fileToUpload.absolutePath}, Size: ${fileToUpload.length()}")

                    val uploadResult = postUploadImage(fileToUpload)
                        .let { flow ->
                            flow.first { it !is Result.Loading }
                        }

                    Log.d("WorkshopDebug", "Resultado de subida (Remote): $uploadResult")

                    when (uploadResult) {
                        is Result.Success -> {
                            remoteImageUrl = uploadResult.data.url
                            fileToUpload.delete()
                            Log.d("WorkshopDebug", "Upload SUCCESS. Remote URL: $remoteImageUrl")
                        }
                        is Result.Error -> {
                            uploadError = "Error al subir la imagen: ${uploadResult.exception.message}"
                            fileToUpload.delete()
                            Log.e("WorkshopDebug", "Upload ERROR: ${uploadResult.exception.message}")
                        }
                        is Result.Loading -> { }
                    }
                }

                if (uploadError != null) {
                    _uiState.update { it.copy(isLoading = false, error = uploadError) }
                    return@launch
                }
            }

            // --- Log de los datos finales del formulario ---
            Log.d("WorkshopDebug", "--- DATOS FINALES DEL FORMULARIO ---")
            Log.d("WorkshopDebug", "Name: ${_formData.value.name}")
            Log.d("WorkshopDebug", "Start Hour: ${_formData.value.startHour}")
            Log.d("WorkshopDebug", "Date: ${_formData.value.date}")
            Log.d("WorkshopDebug", "Final Image URL (to DB): $remoteImageUrl") // ESTE ES EL CRÍTICO

            val workshopDto = WorkshopDto(
                id = UUID.randomUUID().toString(),
                name = _formData.value.name,
                startHour = _formData.value.startHour,
                finishHour = _formData.value.finishHour,
                status = 1,
                idUser = _formData.value.idUser,
                description = _formData.value.description,
                date = _formData.value.date,
                image = remoteImageUrl, // <--- Aquí ya deberías tener la URL
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
        if (data.videoTraining.isBlank()) {
            errors["videoTraining"] = true
        } else {
            if (!isValidUrl(data.videoTraining)) {
                errors["videoTraining"] = true
            }
        }

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

