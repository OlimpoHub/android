package com.app.arcabyolimpo.presentation.screens.user.register

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.arcabyolimpo.data.remote.dto.user.UserDto
import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.usecase.upload.PostUploadImage
import com.app.arcabyolimpo.domain.usecase.user.GetUsersUseCase
import com.app.arcabyolimpo.domain.usecase.user.register.RegisterUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class UserRegisterViewModel @Inject constructor(
    private val registerUserUseCase: RegisterUserUseCase,
    private val getUsersUseCase: GetUsersUseCase,
    private val postUploadImage: PostUploadImage,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserRegisterUiState())
    val uiState: StateFlow<UserRegisterUiState> = _uiState.asStateFlow()

    private val _selectedImageUri = MutableStateFlow<Uri?>(null)
    val selectedImageUri: StateFlow<Uri?> = _selectedImageUri.asStateFlow()

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

    fun updateRoleId(value: String) {
        _uiState.value = _uiState.value.copy(roleId = value)
    }

    fun updateIsActive(value: Boolean) {
        _uiState.value = _uiState.value.copy(isActive = if (value) 1 else 0)
    }

    fun updateFirstName(value: String) {
        _uiState.value = _uiState.value.copy(
            firstName = value,
            firstNameError = if (value.isBlank()) "El nombre es requerido" else null
        )
    }

    fun updateLastName(value: String) {
        _uiState.value = _uiState.value.copy(
            lastName = value,
            lastNameError = if (value.isBlank()) "El apellido es requerido" else null
        )
    }

    fun updateSecondLastName(value: String) {
        _uiState.value = _uiState.value.copy(secondLastName = value)
    }

    fun updateEmail(value: String) {
        _uiState.value = _uiState.value.copy(
            email = value,
            emailError = if (!isValidEmail(value)) "Email inválido" else null
        )
    }

    fun updatePhone(value: String) {
        _uiState.value = _uiState.value.copy(
            phone = value,
            phoneError = if (value.length < 10) "Teléfono inválido" else null
        )
    }

    fun updateBirthDate(value: String) {
        _uiState.value = _uiState.value.copy(birthDate = value)
    }

    fun updateDegree(value: String) {
        _uiState.value = _uiState.value.copy(degree = value)
    }

    fun updateHasReglamentoInterno(value: Boolean) {
        _uiState.value = _uiState.value.copy(hasReglamentoInterno = value)
    }

    fun updateHasCopiaIne(value: Boolean) {
        _uiState.value = _uiState.value.copy(hasCopiaIne = value)
    }

    fun updateHasAvisoConfidencialidad(value: Boolean) {
        _uiState.value = _uiState.value.copy(hasAvisoConfidencialidad = value)
    }

    fun registerCollab() {
        if (!validateForm()) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            getUsersUseCase().collect { result ->
                when (result) {
                    is Result.Loading -> {
                    }
                    is Result.Success -> {
                        val existingUsers = result.data
                        val duplicate = existingUsers.find { existing ->
                            existing.correoElectronico.equals(_uiState.value.email, ignoreCase = true) ||
                                    (existing.nombre.equals(_uiState.value.firstName, ignoreCase = true) &&
                                            existing.apellidoPaterno.equals(_uiState.value.lastName, ignoreCase = true) &&
                                            existing.apellidoMaterno.equals(_uiState.value.secondLastName, ignoreCase = true))
                        }

                        if (duplicate != null) {
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                error = "Este usuario ya está registrado"
                            )
                        } else {
                            uploadImageAndRegister()
                        }
                    }
                    is Result.Error -> {
                        uploadImageAndRegister()
                    }
                }
            }
        }
    }

    private suspend fun uploadImageAndRegister() {
        val imageUri = _selectedImageUri.value
        var remoteImageUrl: String? = null
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
                    is Result.Loading -> {  }
                }
            }

            if (uploadError != null) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = uploadError)
                return
            }
        }

         _uiState.value = _uiState.value.copy(photoUrl = remoteImageUrl)

        proceedWithRegistration()
    }

    private suspend fun proceedWithRegistration() {
        val user = UserDto(
            idUsuario = null,
            idRol = _uiState.value.roleId,
            nombre = _uiState.value.firstName,
            apellidoPaterno = _uiState.value.lastName,
            apellidoMaterno = _uiState.value.secondLastName,
            fechaNacimiento = _uiState.value.birthDate,
            carrera = _uiState.value.degree,
            correoElectronico = _uiState.value.email,
            telefono = _uiState.value.phone,
            estatus = _uiState.value.isActive,
            reglamentoInterno = if (_uiState.value.hasReglamentoInterno) 1 else 0,
            copiaINE = if (_uiState.value.hasCopiaIne) 1 else 0,
            avisoConfidencialidad = if (_uiState.value.hasAvisoConfidencialidad) 1 else 0,
            foto = _uiState.value.photoUrl
        )

        android.util.Log.d("RegisterCollab", "Starting registration with roleId: ${_uiState.value.roleId}")

        registerUserUseCase(user).collect { result ->
            when (result) {
                is Result.Loading -> {
                }
                is Result.Success -> {
                    android.util.Log.d("RegisterCollab", "SUCCESS!")
                    val userType = if (_uiState.value.roleId == "2") "asistente" else "voluntario"
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        success = true,
                        successMessage = "Usuario registrado exitosamente como $userType",
                        error = null
                    )
                }
                is Result.Error -> {
                    android.util.Log.e("RegisterCollab", "FAILURE: ${result.exception.message}", result.exception)
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.exception.message ?: "Error al registrar"
                    )
                }
            }
        }
    }

    fun resetState() {
        _uiState.value = UserRegisterUiState()
        _selectedImageUri.value = null
    }

    fun validateForm(): Boolean {
        val state = _uiState.value

        val firstNameError = if (state.firstName.isBlank()) "El nombre es requerido" else null
        val lastNameError = if (state.lastName.isBlank()) "El apellido es requerido" else null
        val emailError = if (!isValidEmail(state.email)) "Email inválido" else null
        val phoneError = if (state.phone.length < 10) "Teléfono inválido" else null

        _uiState.value = _uiState.value.copy(
            firstNameError = firstNameError,
            lastNameError = lastNameError,
            emailError = emailError,
            phoneError = phoneError
        )

        return firstNameError == null &&
                lastNameError == null &&
                emailError == null &&
                phoneError == null
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

}