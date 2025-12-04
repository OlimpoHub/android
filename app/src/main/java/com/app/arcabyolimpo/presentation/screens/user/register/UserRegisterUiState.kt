package com.app.arcabyolimpo.presentation.screens.user.register


/**
 * Represents the UI state for the user registration screen.
 *
 * This data class encapsulates all state information needed to render the user
 * registration form, validate user input, and handle the registration process.
 * It follows the unidirectional data flow pattern where the ViewModel updates
 * this state and the UI observes and reacts to changes.
 *
 * The state is organized into four main sections:
 * 1. **User Information Fields** - Basic user data being collected through the form
 * 2. **Document Compliance Flags** - Boolean flags tracking required document submissions
 * 3. **Operation State** - Loading, error, and success states for the registration operation
 * 4. **Field Validation Errors** - Individual error messages for form field validation
 *
 * This separation allows the UI to provide granular feedback on form validation
 * while also managing the overall registration process state independently.
 *
 * @property firstName The user's first name as entered in the registration form.
 *                     Defaults to empty string for initial form state.
 * @property lastName The user's paternal last name (apellido paterno) as entered in the form.
 *                    Defaults to empty string for initial form state.
 * @property secondLastName The user's maternal last name (apellido materno) as entered in the form.
 *                          Optional field that defaults to empty string.
 * @property email The user's email address used for account identification and communication.
 *                 Defaults to empty string and is validated against standard email format.
 * @property phone The user's phone number for contact purposes. Defaults to empty string
 *                 and is validated to ensure it meets minimum length requirements (10 digits).
 * @property birthDate The user's date of birth in YYYY-MM-DD format. Defaults to empty string
 *                     and can be set via manual input or date picker interaction.
 * @property degree The user's academic degree or career field (e.g., "Ingeniería en Sistemas").
 *                  Optional field that defaults to empty string.
 * @property roleId The identifier of the role to be assigned to the user. Defaults to "3"
 *                  (Voluntario/Becario role). Can be "2" for Asistente or "3" for Voluntario.
 * @property isActive The user's account status represented as an integer. Defaults to 1 (active).
 *                    Values: 1 for active, 0 for inactive.
 * @property photoUrl The URL of the user's uploaded profile photo, or null if no photo was uploaded.
 *                    Defaults to null. This URL is obtained after successfully uploading an
 *                    image to the server.
 * @property hasReglamentoInterno Boolean flag indicating whether the user has submitted or
 *                                acknowledged the internal regulations document. Defaults to false.
 * @property hasCopiaIne Boolean flag indicating whether the user has provided a copy of their
 *                       national identification (INE). Defaults to false.
 * @property hasAvisoConfidencialidad Boolean flag indicating whether the user has acknowledged
 *                                     the confidentiality notice. Defaults to false.
 * @property isLoading Indicates whether the registration operation is currently in progress.
 *                     When true, the UI typically displays loading indicators and disables
 *                     form submission to prevent duplicate registrations.
 * @property error Error message from the registration operation, or null if no error occurred.
 *                 When populated, contains a human-readable description of what went wrong
 *                 (e.g., duplicate user, network error, validation failure), allowing the UI
 *                 to display appropriate error feedback.
 * @property success Indicates whether the user was successfully registered. When true, the UI
 *                   typically shows a success message and navigates away from the registration
 *                   screen after a brief delay.
 * @property successMessage A custom success message to display upon successful registration,
 *                          or null if using default messaging. When populated, provides
 *                          context-specific feedback such as the registered user's role type.
 * @property firstNameError Validation error message for the first name field, or null if valid.
 *                          Populated when the field is blank or fails validation rules.
 * @property lastNameError Validation error message for the last name field, or null if valid.
 *                         Populated when the field is blank or fails validation rules.
 * @property emailError Validation error message for the email field, or null if valid.
 *                      Populated when the email format is invalid or fails validation rules.
 * @property phoneError Validation error message for the phone field, or null if valid.
 *                      Populated when the phone number is too short or fails validation rules
 *                      (minimum 10 digits required).
 */
data class UserRegisterUiState (
    val firstName: String = "",
    val lastName: String = "",
    val secondLastName: String = "",
    val email: String = "",
    val phone: String = "",
    val birthDate: String = "",
    val degree: String = "",
    val roleId: String = "3",
    val isActive: Int = 1,
    val photoUrl: String? = null,
    val hasReglamentoInterno: Boolean = false,
    val hasCopiaIne: Boolean = false,
    val hasAvisoConfidencialidad: Boolean = false,

    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false,
    val successMessage: String? = null,

    // Validation errors
    val firstNameError: String? = null,
    val lastNameError: String? = null,
    val emailError: String? = null,
    val phoneError: String? = null
    )

