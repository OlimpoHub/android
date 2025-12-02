package com.app.arcabyolimpo.presentation.screens.user.updateuser

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.arcabyolimpo.data.remote.dto.user.UserDto
import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.usecase.user.GetUserByIdUseCase
import com.app.arcabyolimpo.domain.usecase.user.UpdateUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateUserViewModel @Inject constructor(
    private val updateUserUseCase: UpdateUserUseCase,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(UpdateUserUiState())
    val uiState: StateFlow<UpdateUserUiState> = _uiState.asStateFlow()

    private val userId: String = checkNotNull(savedStateHandle["userId"]) {
        "userId parameter wasn't found in SavedStateHandle"
    }

    init {
        // Precarga de datos del usuario a editar
        loadUserForEdit()
    }

    private fun loadUserForEdit() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            getUserByIdUseCase(userId).collect { result ->
                when (result) {
                    is Result.Loading -> {

                    }
                    is Result.Success -> {
                        val user = result.data

                        _uiState.value = _uiState.value.copy(

                            // mapeo a los campos del formulario
                            id             = user.idUsuario ?: "",
                            roleId         = user.idRol ?: _uiState.value.roleId,
                            firstName      = user.nombre ?: "",
                            lastName       = user.apellidoPaterno ?: "",
                            secondLastName = user.apellidoMaterno ?: "",
                            birthDate      = user.fechaNacimiento ?: "",
                            degree         = user.carrera ?: "",
                            email          = user.correoElectronico ?: "",
                            phone          = user.telefono ?: "",
                            isActive       = user.estatus ?: 1,
                            photoUrl       = user.foto ?: "",

                            isLoading = false,
                            error = null
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

    // ======= Handlers de campos =======
    fun updateIsActive(value: Int) {
        _uiState.value = _uiState.value.copy(isActive = value)
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

    fun updateUser() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val s = _uiState.value

            val user = UserDto(
                idUsuario        = s.id,
                idRol            = s.roleId,
                nombre           = s.firstName,
                apellidoPaterno  = s.lastName,
                apellidoMaterno  = s.secondLastName,
                fechaNacimiento  = s.birthDate,
                carrera          = s.degree,
                correoElectronico= s.email,
                telefono         = s.phone,
                estatus          = s.isActive,
                foto             = s.photoUrl
            )

            updateUserUseCase(user).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        // ya estamos en loading
                    }
                    is Result.Success -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            success = true,
                            successMessage = "Usuario actualizado exitosamente",
                            error = null
                        )
                    }
                    is Result.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = result.exception.message ?: "Error al actualizar"
                        )
                    }
                }
            }
        }
    }

    fun resetState() {
        _uiState.value = UpdateUserUiState()
    }

    // ======= Validaciones =======
    fun validateForm(): Boolean {
        val state = _uiState.value

        val firstNameError = if (state.firstName.isBlank()) "El nombre es requerido" else null
        val lastNameError  = if (state.lastName.isBlank())  "El apellido es requerido" else null
        val emailError     = if (!isValidEmail(state.email)) "Email inválido" else null
        val phoneError     = if (state.phone.length < 10) "Teléfono inválido" else null

        _uiState.value = _uiState.value.copy(
            firstNameError = firstNameError,
            lastNameError  = lastNameError,
            emailError     = emailError,
            phoneError     = phoneError
        )

        return firstNameError == null &&
                lastNameError  == null &&
                emailError     == null &&
                phoneError     == null
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun updateProfileImage(uri: Uri?) {
        _uiState.value = _uiState.value.copy(
            photoUrl = uri?.toString()
        )
    }


}

