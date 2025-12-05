package com.app.arcabyolimpo.presentation.screens.user.detail

import com.app.arcabyolimpo.data.remote.dto.user.UserDto

/**
 * Represents the UI state for the user detail screen.
 *
 * This data class encapsulates all state information needed to render the user
 * detail screen and handle user interactions. It follows the unidirectional data
 * flow pattern, where the ViewModel updates this state and the UI observes and
 * reacts to changes.
 *
 * The state is divided into two main concerns: user detail display and deletion
 * operations. Each concern has its own loading and error states to allow the UI
 * to provide appropriate feedback for different operations simultaneously.
 *
 * @property collab The user data to be displayed, or null if not yet loaded or if
 *                  an error occurred during loading. When populated, contains all
 *                  user information including personal details, role, and status.
 * @property isLoading Indicates whether the user detail data is currently being loaded.
 *                     When true, the UI typically displays a loading indicator while
 *                     the user data is being fetched from the repository.
 * @property error Error message from the user detail fetch operation, or null if no
 *                 error occurred. When populated, contains a human-readable description
 *                 of what went wrong, allowing the UI to display appropriate error
 *                 messages and recovery options.
 * @property deleteLoading Indicates whether a user deletion operation is currently in progress.
 *                         When true, the UI typically displays a loading overlay or disables
 *                         interaction to prevent multiple deletion attempts.
 * @property deleteError Error message from the deletion operation, or null if no error occurred.
 *                       When populated, contains a human-readable description of why the
 *                       deletion failed, allowing the UI to inform the user and potentially
 *                       offer retry options.
 * @property deleted Indicates whether the user was successfully deleted. When true, the UI
 *                   typically shows a success message and navigates away from the detail
 *                   screen since the user no longer exists or is marked as inactive.
 */
data class UserDetailUiState(
    val collab: UserDto? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val deleteLoading: Boolean = false,
    val deleteError: String? = null,
    val deleted: Boolean = false,
)