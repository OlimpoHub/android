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
/**
 * ViewModel for managing the user registration screen's state and business logic.
 *
 * This ViewModel orchestrates the complete user registration workflow including form
 * validation, duplicate user detection, profile photo upload, and final user creation.
 * It manages complex multi-step processes such as uploading images to a remote server
 * before creating the user record, ensuring data consistency and proper error handling
 * throughout the registration flow.
 *
 * The registration process follows these steps:
 * 1. **Form validation**: Ensures all required fields meet validation criteria
 * 2. **Duplicate detection**: Checks for existing users with the same email or full name
 * 3. **Image upload** (if provided): Uploads profile photo and obtains remote URL
 * 4. **User creation**: Submits user data with all information to the backend
 * 5. **Feedback**: Provides success or error messages through the UI state
 *
 * The ViewModel maintains separate state flows for the form data ([uiState]) and the
 * selected image URI ([selectedImageUri]), allowing the UI to independently observe
 * and react to changes in each. This separation also helps with configuration changes,
 * ensuring the selected image persists across screen rotations.
 *
 * All asynchronous operations are executed within the [viewModelScope], ensuring proper
 * lifecycle management and automatic cancellation when the ViewModel is cleared.
 *
 * @property registerUserUseCase Use case for creating new user accounts in the system.
 * @property getUsersUseCase Use case for retrieving existing users for duplicate detection.
 * @property postUploadImage Use case for uploading profile photos to the remote server.
 * @property context Application context for file operations and URI handling, injected
 *                   with @ApplicationContext to ensure proper lifecycle scope.
 * @constructor Creates a ViewModel instance with the required use cases and context
 *              via dependency injection.
 */

@HiltViewModel
class UserRegisterViewModel @Inject constructor(
    private val registerUserUseCase: RegisterUserUseCase,
    private val getUsersUseCase: GetUsersUseCase,
    private val postUploadImage: PostUploadImage,
    @ApplicationContext private val context: Context
) : ViewModel() {

    /**
     * Internal mutable state flow holding the current form and operation state.
     *
     * This private flow is used internally to update the UI state in response to
     * user input, validation results, and registration operation outcomes.
     */
    private val _uiState = MutableStateFlow(UserRegisterUiState())
    /**
     * Public read-only state flow exposing the current UI state to the presentation layer.
     *
     * The UI observes this flow to react to changes in form data, validation errors,
     * loading states, and registration results. The flow emits a new [UserRegisterUiState]
     * whenever any aspect of the state changes.
     */
    val uiState: StateFlow<UserRegisterUiState> = _uiState.asStateFlow()

    /**
     * Internal mutable state flow holding the selected profile image URI.
     *
     * Maintained separately from the main UI state to allow independent observation
     * and to preserve the selected image across configuration changes.
     */
    private val _selectedImageUri = MutableStateFlow<Uri?>(null)

    /**
     * Public read-only state flow exposing the selected image URI to the presentation layer.
     *
     * The UI observes this flow to display the selected image preview and to know
     * when an image has been selected or cleared.
     */
    val selectedImageUri: StateFlow<Uri?> = _selectedImageUri.asStateFlow()

    /**
     * Updates the selected profile image URI.
     *
     * This method is called when the user selects a new image from their device
     * or clears the current selection. The URI is stored separately from the main
     * UI state to allow the UI to independently observe image selection changes.
     *
     * @param uri The URI of the newly selected image, or null to clear the selection.
     */
    fun setSelectedImageUri(uri: Uri?) {
        _selectedImageUri.value = uri
    }

    /**
     * Converts a content URI to a temporary File for upload operations.
     *
     * This helper method reads the content from the provided URI using the content
     * resolver and writes it to a temporary file in the app's cache directory. This
     * conversion is necessary because the upload API requires a File object rather
     * than a URI.
     *
     * The temporary file is named with a timestamp to avoid conflicts and should be
     * deleted by the caller after the upload completes (successful or not) to prevent
     * cache buildup.
     *
     * @param context The Context used to access the content resolver and cache directory.
     * @param uri The content URI pointing to the image selected by the user.
     * @return A temporary File containing the image data, or null if the conversion fails
     *         due to I/O errors or if the URI cannot be read.
     */
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

    /**
     * Updates the role ID for the user being registered.
     *
     * This method is typically called when the user selects a role type button
     * in the UI. Valid values are "2" for Asistente (Assistant) or "3" for
     * Voluntario/Becario (Volunteer).
     *
     * @param value The role identifier string ("2" or "3").
     */
    fun updateRoleId(value: String) {
        _uiState.value = _uiState.value.copy(roleId = value)
    }

    /**
     * Updates the account status for the user being registered.
     *
     * This method converts a boolean active/inactive selection into the integer
     * representation used by the backend (1 for active, 0 for inactive).
     *
     * @param value True to set the user as active, false for inactive.
     */
    fun updateIsActive(value: Boolean) {
        _uiState.value = _uiState.value.copy(isActive = if (value) 1 else 0)
    }

    /**
     * Updates the first name field with real-time validation.
     *
     * This method updates the first name value and immediately validates it,
     * setting an error message if the field is blank. This provides instant
     * feedback to the user as they type.
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
     * Updates the paternal last name field with real-time validation.
     *
     * This method updates the paternal last name (apellido paterno) value and
     * immediately validates it, setting an error message if the field is blank.
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
     * Updates the maternal last name field without validation.
     *
     * The maternal last name (apellido materno) is optional in the registration
     * form, so no validation is performed on this field.
     *
     * @param value The new maternal last name value entered by the user.
     */
    fun updateSecondLastName(value: String) {
        _uiState.value = _uiState.value.copy(secondLastName = value)
    }

    /**
     * Updates the email field with real-time validation.
     *
     * This method updates the email value and immediately validates it using
     * the [isValidEmail] helper method, setting an error message if the email
     * format is invalid.
     *
     * @param value The new email value entered by the user.
     */
    fun updateEmail(value: String) {
        _uiState.value = _uiState.value.copy(
            email = value,
            emailError = if (!isValidEmail(value)) "Email inválido" else null
        )
    }
    /**
     * Updates the phone number field with real-time validation.
     *
     * This method updates the phone value and immediately validates it,
     * requiring a minimum length of 10 characters. Sets an error message
     * if the phone number is too short.
     *
     * @param value The new phone number value entered by the user.
     */
    fun updatePhone(value: String) {
        _uiState.value = _uiState.value.copy(
            phone = value,
            phoneError = if (value.length < 10) "Teléfono inválido" else null
        )
    }

    /**
     * Updates the birth date field.
     *
     * This method is called either when the user manually types a date or
     * when a date is selected from the date picker. The date should be in
     * YYYY-MM-DD format.
     *
     * @param value The new birth date value in YYYY-MM-DD format.
     */
    fun updateBirthDate(value: String) {
        _uiState.value = _uiState.value.copy(birthDate = value)
    }

    /**
     * Updates the academic degree/career field.
     *
     * This optional field stores the user's academic degree or career
     * (e.g., "Ingeniería en Sistemas"). No validation is required.
     *
     * @param value The new degree/career value entered by the user.
     */
    fun updateDegree(value: String) {
        _uiState.value = _uiState.value.copy(degree = value)
    }

    /**
     * Updates the internal regulations document compliance flag.
     *
     * This checkbox indicates whether the user has submitted or acknowledged
     * the internal regulations document. Used for compliance tracking.
     *
     * @param value True if the document has been submitted/acknowledged, false otherwise.
     */
    fun updateHasReglamentoInterno(value: Boolean) {
        _uiState.value = _uiState.value.copy(hasReglamentoInterno = value)
    }

    /**
     * Updates the national ID (INE) copy submission flag.
     *
     * This checkbox indicates whether the user has provided a copy of their
     * national identification. Used for compliance tracking.
     *
     * @param value True if the ID copy has been submitted, false otherwise.
     */
    fun updateHasCopiaIne(value: Boolean) {
        _uiState.value = _uiState.value.copy(hasCopiaIne = value)
    }

    /**
     * Updates the confidentiality notice acknowledgment flag.
     *
     * This checkbox indicates whether the user has acknowledged the
     * confidentiality notice. Used for compliance tracking.
     *
     * @param value True if the notice has been acknowledged, false otherwise.
     */
    fun updateHasAvisoConfidencialidad(value: Boolean) {
        _uiState.value = _uiState.value.copy(hasAvisoConfidencialidad = value)
    }

    /**
     * Initiates the user registration process with validation and duplicate detection.
     *
     * This method orchestrates the complete registration workflow:
     * 1. Validates all form fields using [validateForm]
     * 2. Fetches existing users to check for duplicates by email or full name
     * 3. If no duplicate is found, proceeds to [uploadImageAndRegister]
     * 4. Handles errors at each step with appropriate user feedback
     *
     * The duplicate detection prevents creating multiple accounts for the same
     * person, checking both email (which should be unique) and the combination
     * of first name, paternal last name, and maternal last name.
     *
     * If the duplicate check fails (e.g., network error), the registration still
     * proceeds as this is considered a non-critical validation. The backend may
     * have its own duplicate prevention mechanisms.
     *
     * All operations are executed within the [viewModelScope] to ensure proper
     * lifecycle management.
     */
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

    /**
     * Handles profile photo upload and proceeds with user registration.
     *
     * This private method manages the image upload workflow:
     * 1. Checks if an image was selected by the user
     * 2. Converts the image URI to a File using [getFileFromUri]
     * 3. Uploads the file to the remote server using [postUploadImage]
     * 4. Stores the returned image URL in the UI state
     * 5. Cleans up the temporary file after upload (success or failure)
     * 6. Proceeds to [proceedWithRegistration] with the image URL
     *
     * If no image was selected, the method skips directly to registration with
     * a null photo URL. If the image upload fails, the registration is aborted
     * and an error message is displayed to the user.
     *
     * The method uses [Flow.first] with a predicate to skip Loading results and
     * get the final Success or Error result from the upload operation.
     */
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

    /**
     * Completes the user registration by submitting all data to the backend.
     *
     * This private method constructs a [UserDto] from the current form state and
     * submits it to the backend via [registerUserUseCase]. It handles the registration
     * result by updating the UI state with either success or error information.
     *
     * On success, the method:
     * - Sets the success flag to true
     * - Provides a role-specific success message (e.g., "registered as asistente")
     * - Clears any error state
     *
     * On failure, the method:
     * - Clears the loading state
     * - Stores the error message for display to the user
     * - Logs the error for debugging purposes
     *
     * The boolean document compliance flags (hasReglamentoInterno, hasCopiaIne,
     * hasAvisoConfidencialidad) are converted to integers (1/0) to match the backend's
     * expected format.
     */
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

    /**
     * Resets all form fields and state to their default values.
     *
     * This method clears all user input, validation errors, and operation state,
     * preparing the form for a new registration. It's called when the user dismisses
     * the registration form or after a successful registration to ensure clean state
     * for the next use.
     *
     * The selected image URI is also cleared separately to ensure no stale image
     * data persists between registration attempts.
     */
    fun resetState() {
        _uiState.value = UserRegisterUiState()
        _selectedImageUri.value = null
    }

    /**
     * Validates all required form fields and updates validation error states.
     *
     * This method performs comprehensive form validation by checking:
     * - First name is not blank
     * - Last name is not blank
     * - Email matches valid email format
     * - Phone number has at least 10 digits
     *
     * Validation errors are set in the UI state for each invalid field, allowing
     * the UI to display field-specific error messages. The method returns true only
     * if all validations pass, allowing the registration process to proceed.
     *
     * @return True if all required fields are valid, false if any validation fails.
     */
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

    /**
     * Validates an email address using Android's built-in pattern matcher.
     *
     * This private helper method checks if the provided email string matches
     * the standard email address format using Android's EMAIL_ADDRESS pattern,
     * which follows RFC 5322 specifications.
     *
     * @param email The email string to validate.
     * @return True if the email matches valid email format, false otherwise.
     */
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

}