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
/**
 * ViewModel responsible for managing the state and business logic for updating existing users.
 *
 * This ViewModel handles loading user data for editing, managing form state, validating input,
 * and coordinating with the use cases to persist changes to the backend. It retrieves the userId
 * from navigation arguments via SavedStateHandle and automatically loads the user's current data
 * on initialization.
 *
 * The ViewModel exposes a StateFlow of UpdateUserUiState for the UI to observe and react to state
 * changes, including loading states, validation errors, success states, and error messages. All
 * field updates trigger real-time validation where applicable (e.g., email format, phone length),
 * providing immediate feedback to users.
 *
 * @property updateUserUseCase The use case responsible for updating user information on the backend.
 * @property getUserByIdUseCase The use case responsible for fetching user data by ID for editing.
 * @property savedStateHandle The SavedStateHandle containing navigation arguments, specifically the userId.
 * @throws IllegalStateException if userId parameter is not found in SavedStateHandle.
 */

@HiltViewModel
class UpdateUserViewModel @Inject constructor(
    private val updateUserUseCase: UpdateUserUseCase,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    /**
     * Private mutable state flow holding the current UI state for the update user form.
     *
     * This internal state flow is the single source of truth for all form data, validation errors,
     * loading states, and operation results. It is initialized with an empty UpdateUserUiState and
     * populated with user data during the loadUserForEdit process. All state modifications are
     * performed through this mutable flow, ensuring thread-safe state management within the ViewModel.
     *
     * Direct access is restricted to the ViewModel to maintain encapsulation and prevent external
     * modification. The UI observes changes through the public uiState property.
     */

    private val _uiState = MutableStateFlow(UpdateUserUiState())
    val uiState: StateFlow<UpdateUserUiState> = _uiState.asStateFlow()

    /**
     * The unique identifier of the user being edited, extracted from navigation arguments.
     *
     * This property retrieves the userId string parameter from the SavedStateHandle, which is passed
     * through navigation when the update screen is opened. The checkNotNull function ensures that the
     * parameter exists and throws an IllegalStateException with a descriptive message if it's missing,
     * preventing the ViewModel from operating in an invalid state.
     *
     * The userId is used by loadUserForEdit to fetch the correct user's data from the backend and is
     * included in the update request to identify which user record should be modified.
     *
     * @throws IllegalStateException if the userId parameter wasn't found in SavedStateHandle, indicating
     *         a navigation or routing error where the screen was opened without the required argument.
     */

    private val userId: String = checkNotNull(savedStateHandle["userId"]) {
        "userId parameter wasn't found in SavedStateHandle"
    }

    init {
        // Precarga de datos del usuario a editar
        loadUserForEdit()
    }

    /**
     * Loads the existing user data for editing from the backend.
     *
     * This private function is automatically called during ViewModel initialization to fetch the current
     * user information based on the userId parameter. It updates the UI state with loading indicators,
     * populates all form fields with the retrieved data, and handles any errors that occur during the
     * loading process.
     *
     * The function sets isLoading to true at the start and false on completion, maps all user DTO fields
     * to the corresponding UI state properties, and preserves the roleId default value if the fetched
     * user has no role assigned. Error states are captured and displayed to the user via the error
     * property in the UI state.
     */
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

    /**
     * Updates the user's active status in the form state.
     *
     * This function modifies the isActive field in the UI state, where 1 represents an active user
     * and 0 represents an inactive user. The status change is immediately reflected in the UI and
     * will be persisted when the form is submitted.
     *
     * @param value The new status value (1 for active, 0 for inactive).
     */
    fun updateIsActive(value: Int) {
        _uiState.value = _uiState.value.copy(isActive = value)
    }

    /**
     * Updates the user's first name in the form state with real-time validation.
     *
     * This function modifies the firstName field and immediately validates that it is not blank.
     * If the value is empty or contains only whitespace, a validation error message is set and
     * displayed inline in the UI. The error is cleared automatically when a valid value is provided.
     *
     * @param value The new first name value entered by the user.
     */

    fun updateFirstName(value: String) {
        _uiState.value = _uiState.value.copy(
            firstName = value,
            firstNameError = if (value.isBlank()) "El nombre es requerido" else null
        )
    }


    /**
     * Updates the user's paternal last name in the form state with real-time validation.
     *
     * This function modifies the lastName field and immediately validates that it is not blank.
     * If the value is empty or contains only whitespace, a validation error message is set and
     * displayed inline in the UI. The error is cleared automatically when a valid value is provided.
     *
     * @param value The new paternal last name value entered by the user.
     */

    fun updateLastName(value: String) {
        _uiState.value = _uiState.value.copy(
            lastName = value,
            lastNameError = if (value.isBlank()) "El apellido es requerido" else null
        )
    }


    /**
     * Updates the user's maternal last name in the form state.
     *
     * This function modifies the secondLastName field without performing any validation, as the
     * maternal last name is an optional field in the user registration form. The value is immediately
     * reflected in the UI state.
     *
     * @param value The new maternal last name value entered by the user, or an empty string to clear it.
     */

    fun updateSecondLastName(value: String) {
        _uiState.value = _uiState.value.copy(secondLastName = value)
    }


    /**
     * Updates the user's email address in the form state with real-time validation.
     *
     * This function modifies the email field and immediately validates the format using Android's
     * EMAIL_ADDRESS pattern matcher. If the email format is invalid, an error message is set and
     * displayed inline in the UI. The error is cleared automatically when a valid email is provided.
     *
     * @param value The new email address entered by the user.
     */

    fun updateEmail(value: String) {
        _uiState.value = _uiState.value.copy(
            email = value,
            emailError = if (!isValidEmail(value)) "Email inválido" else null
        )
    }


    /**
     * Updates the user's phone number in the form state with real-time validation.
     *
     * This function modifies the phone field and immediately validates that the length is at least
     * 10 characters. If the phone number is shorter than 10 digits, a validation error message is
     * set and displayed inline in the UI. The error is cleared automatically when a valid phone
     * number is provided.
     *
     * @param value The new phone number entered by the user.
     */

    fun updatePhone(value: String) {
        _uiState.value = _uiState.value.copy(
            phone = value,
            phoneError = if (value.length < 10) "Teléfono inválido" else null
        )
    }

    /**
     * Updates the user's birth date in the form state.
     *
     * This function modifies the birthDate field without performing any validation. The value is
     * expected to be in ISO 8601 format (YYYY-MM-DD) when provided by the date picker or manual
     * input. The value is immediately reflected in the UI state.
     *
     * @param value The new birth date in YYYY-MM-DD format.
     */

    fun updateBirthDate(value: String) {
        _uiState.value = _uiState.value.copy(birthDate = value)
    }

    /**
     * Updates the user's career or degree program in the form state.
     *
     * This function modifies the degree field without performing any validation, as the career
     * field is optional in the user registration form. The value is immediately reflected in the
     * UI state.
     *
     * @param value The new career or degree program entered by the user, or an empty string to clear it.
     */

    fun updateDegree(value: String) {
        _uiState.value = _uiState.value.copy(degree = value)
    }

    /**
     * Submits the updated user information to the backend for persistence.
     *
     * This function collects all form data from the current UI state, constructs a UserDto object with
     * the updated values, and invokes the UpdateUserUseCase to send the changes to the backend API. It
     * manages loading states throughout the operation, displays success or error messages via the UI
     * state, and sets the success flag to trigger screen dismissal on successful completion.
     *
     * The function runs asynchronously in the viewModelScope and collects the Result flow from the use
     * case, handling Loading, Success, and Error states appropriately. On success, the successMessage
     * is set to "Usuario actualizado exitosamente" and the success flag is set to true, triggering the
     * onSuccess callback in the UI.
     */
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


    /**
     * Resets the ViewModel state to its initial empty values.
     *
     * This function is called when the update screen is dismissed or disposed to ensure that stale
     * data doesn't persist if the screen is reopened. It creates a new instance of UpdateUserUiState
     * with all default values, clearing any form data, validation errors, success states, and error
     * messages.
     */

    fun resetState() {
        _uiState.value = UpdateUserUiState()
    }

    /**
     * Validates all required fields in the form before submission.
     *
     * This function performs comprehensive validation on all required fields (first name, last name,
     * email, and phone) and updates the UI state with any validation error messages. It uses helper
     * validation functions (isValidEmail) to check email format and verifies that required text fields
     * are not blank and that the phone number is at least 10 characters long.
     *
     * The function updates the UI state with error messages for each invalid field, allowing the UI
     * to display inline validation feedback to the user. All errors must be resolved before the form
     * can be submitted.
     *
     * @return true if all required fields pass validation, false if any validation errors exist.
     */
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

    /**
     * Validates email address format using Android's built-in pattern matcher.
     *
     * This private helper function checks whether the provided email string matches the standard email
     * address format using Android's Patterns.EMAIL_ADDRESS pattern. It is used by the updateEmail and
     * validateForm functions to provide real-time and pre-submission validation.
     *
     * @param email The email address string to validate.
     * @return true if the email matches a valid email format, false otherwise.
     */
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    /**
     * Updates the user's profile image URI in the form state.
     *
     * This function modifies the photoUrl field by converting the provided URI to a string representation.
     * The URI typically comes from the device's image picker when the user selects a new profile photo.
     * If null is provided, the photoUrl is cleared. The image preview in the UI updates immediately to
     * reflect the change.
     *
     * @param uri The URI of the selected image file, or null to clear the profile image.
     */
    fun updateProfileImage(uri: Uri?) {
        _uiState.value = _uiState.value.copy(
            photoUrl = uri?.toString()
        )
    }


}

