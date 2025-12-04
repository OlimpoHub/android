package com.app.arcabyolimpo.presentation.screens.user.updateuser

import com.app.arcabyolimpo.data.remote.dto.user.UserDto

/**
 * Represents the UI state for the user update screen.
 *
 * This data class encapsulates all state information needed to render the user
 * update form, validate user input, and handle the update process. Unlike the
 * registration state, this includes the user's existing ID and is pre-populated
 * with current user data fetched from the backend.
 *
 * The state follows the unidirectional data flow pattern where the ViewModel
 * loads existing user data, updates this state based on user edits, and the UI
 * observes and reacts to changes. This ensures that all modifications are tracked
 * and can be validated before submission.
 *
 * The state is organized into three main sections:
 * 1. **User Information Fields** - Current and editable user data from the form
 * 2. **Operation State** - Loading, error, and success states for the update operation
 * 3. **Field Validation Errors** - Individual error messages for form field validation
 *
 * Key differences from registration state:
 * - Includes user ID for identifying which record to update
 * - Pre-populated with existing user data on initialization
 * - No document compliance flags (these cannot be modified during updates)
 * - Simpler role handling (role typically cannot be changed during updates)
 *
 * @property id The unique identifier of the user being updated. Required for the backend
 *              to identify which user record to modify. Loaded from navigation parameters.
 * @property firstName The user's first name as entered or modified in the update form.
 *                     Pre-populated with existing data when the form loads.
 * @property lastName The user's paternal last name (apellido paterno) as entered or modified.
 *                    Pre-populated with existing data when the form loads.
 * @property secondLastName The user's maternal last name (apellido materno) as entered or modified.
 *                          Optional field pre-populated with existing data if available.
 * @property email The user's email address for account identification and communication.
 *                 Pre-populated with existing email and validated against standard format.
 * @property phone The user's phone number for contact purposes. Pre-populated with existing
 *                 phone number and validated to ensure minimum length (10 digits).
 * @property birthDate The user's date of birth in YYYY-MM-DD format. Pre-populated with
 *                     existing date and can be modified via manual input or date picker.
 * @property degree The user's academic degree or career field (e.g., "Ingeniería en Sistemas").
 *                  Optional field pre-populated with existing data if available.
 * @property roleId The identifier of the user's role. Typically not editable during updates,
 *                  but included for maintaining consistency with registration flow. Defaults
 *                  to "3" (Voluntario) if not provided by the backend.
 * @property isActive The user's account status represented as an integer. Can be toggled between
 *                    1 (active) and 0 (inactive) during the update process.
 * @property photoUrl The URL of the user's profile photo. Pre-populated with existing photo URL
 *                    if available, or can be updated with a new image during the edit process.
 *                    Null indicates no photo is available.
 * @property isLoading Indicates whether the update operation or initial data load is in progress.
 *                     When true, the UI typically displays loading indicators and disables
 *                     form submission to prevent duplicate update attempts.
 * @property error Error message from the update operation or data loading, or null if no error
 *                 occurred. When populated, contains a human-readable description of what went
 *                 wrong, allowing the UI to display appropriate error feedback.
 * @property success Indicates whether the user was successfully updated. When true, the UI
 *                   typically shows a success message and navigates back to the detail or
 *                   list screen to reflect the updated information.
 * @property successMessage A custom success message to display upon successful update,
 *                          or null if using default messaging. Provides confirmation
 *                          feedback to the user about the successful operation.
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
data class UpdateUserUiState(
    val id: String = "",
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