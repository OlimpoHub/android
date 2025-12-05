package com.app.arcabyolimpo.presentation.screens.user.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.usecase.user.GetUserByIdUseCase
import com.app.arcabyolimpo.domain.usecase.user.delete.DeleteUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for managing the user detail screen's state and business logic.
 *
 * This ViewModel coordinates the retrieval and display of detailed user information,
 * as well as handling user deletion operations. It manages the UI state through a
 * reactive [StateFlow], allowing the presentation layer to observe and react to
 * changes in loading states, data availability, and operation results.
 *
 * The ViewModel retrieves the user ID from navigation arguments via [SavedStateHandle]
 * and automatically loads the user details upon initialization. It provides functionality
 * for refreshing user data and performing delete operations with comprehensive error
 * handling and state management.
 *
 * All operations are executed within the [viewModelScope], ensuring proper lifecycle
 * management and automatic cancellation when the ViewModel is cleared.
 *
 * @property getUserByIdUseCase Use case for retrieving detailed user information by ID.
 * @property deleteUserUseCase Use case for performing user deletion operations.
 * @property savedStateHandle Handle for accessing navigation arguments and saved state.
 * @constructor Creates a ViewModel instance with the required use cases and saved state handle
 *              via dependency injection.
 */
@HiltViewModel
class UserDetailViewModel @Inject constructor(
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    /**
     * The unique identifier of the user whose details are being displayed.
     *
     * This value is extracted from the navigation arguments via [SavedStateHandle]
     * and is required for all user detail operations. The [checkNotNull] ensures
     * that navigation was properly configured with this parameter, providing a
     * clear error message if the parameter is missing.
     */
    private val userId: String = checkNotNull(savedStateHandle["userId"]){
        "userId parameter wasn't found in SavedStateHandle"
    }

    /**
     * Internal mutable state flow holding the current UI state.
     *
     * This private flow is used internally to update the UI state in response to
     * use case results and user actions, while the public [uiState] provides
     * read-only access to prevent external modification.
     */
    private val _uiState = MutableStateFlow(UserDetailUiState())

    /**
     * Public read-only state flow exposing the current UI state to the presentation layer.
     *
     * The UI observes this flow to react to changes in user data, loading states,
     * and operation results. The flow emits a new [UserDetailUiState] whenever
     * the state changes, triggering UI recomposition as needed.
     */
    val uiState: StateFlow<UserDetailUiState> = _uiState.asStateFlow()

    init {
        loadCollabDetail()
    }

    /**
     * Loads or refreshes the detailed information for the current user.
     *
     * This method initiates the user detail retrieval process by calling the
     * [GetUserByIdUseCase] with the stored [userId]. It manages the loading state
     * throughout the operation and handles all possible result states from the use case:
     * - Loading: Maintains the loading state while the fetch is in progress
     * - Success: Updates the UI state with the retrieved user data
     * - Error: Captures and stores the error message for display to the user
     *
     * The method clears any previous errors before starting a new load operation,
     * ensuring clean state management. It's designed to be called both during
     * initialization and when the user explicitly requests a refresh (e.g., after
     * navigation back or pull-to-refresh actions).
     *
     * All operations are executed within the [viewModelScope] to ensure proper
     * coroutine lifecycle management.
     */
    fun loadCollabDetail() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            getUserByIdUseCase(userId).collect { result ->
                when (result) {
                    is Result.Loading -> {
                    }
                    is Result.Success -> {
                       _uiState.value = _uiState.value.copy(
                            collab = result.data,
                            isLoading = false
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
     * Initiates a delete operation for the user identified by the provided ID.
     *
     * This method executes the user deletion process through the [DeleteUserUseCase],
     * managing the deletion-specific state throughout the operation. It handles all
     * possible result states from the use case:
     * - Loading: Sets the deletion loading state to show appropriate UI feedback
     * - Success: Marks the deletion as completed, triggering navigation and success messages
     * - Error: Captures and stores the error message for display to the user
     *
     * Before starting the deletion, the method resets all deletion-related state flags
     * to ensure clean state management and prevent stale success or error messages from
     * previous operations.
     *
     * The UI layer should observe the [uiState] to react to the deletion states,
     * typically showing loading indicators during the operation, displaying success
     * messages and navigating away on success, or showing error messages on failure.
     *
     * All operations are executed within the [viewModelScope] to ensure proper
     * coroutine lifecycle management.
     *
     * @param idString The unique identifier of the user to delete, provided as a string.
     */
    fun deleteCollabById(idString: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(deleteLoading = true, deleteError = null, deleted = false)

            deleteUserUseCase(idString).collect { result ->
                when (result) {
                    is Result.Loading -> {
                    }
                    is Result.Success -> {
                        _uiState.value = _uiState.value.copy(
                            deleteLoading = false,
                            deleted = true
                        )
                    }
                    is Result.Error -> {
                        _uiState.value = _uiState.value.copy(
                            deleteLoading = false,
                            deleteError = result.exception.message ?: "Error al eliminar"
                        )
                    }
                }
            }
        }
    }

    /**
     * Resets all deletion-related state flags to their default values.
     *
     * This method clears the deletion loading state, error messages, and success flags,
     * preparing the ViewModel for future deletion operations or allowing the UI to
     * dismiss deletion-related feedback messages. It's typically called after the UI
     * has finished responding to a deletion result (e.g., after showing a snackbar
     * message or navigating away).
     *
     * Resetting the state prevents stale deletion states from affecting subsequent
     * operations or UI renders, ensuring clean state management throughout the
     * user detail screen's lifecycle.
     */
    fun resetDeleteState() {
        _uiState.value = _uiState.value.copy(deleteLoading = false, deleteError = null, deleted = false)
    }
}