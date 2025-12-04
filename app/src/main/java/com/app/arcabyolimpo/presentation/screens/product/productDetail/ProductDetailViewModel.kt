package com.app.arcabyolimpo.presentation.screens.product.productDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.usecase.product.DeleteProductUseCase
import com.app.arcabyolimpo.domain.usecase.product.GetProductByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel responsible for managing state and business logic for the product detail screen.
 *
 * This ViewModel handles loading product details from the backend, managing the product deletion
 * workflow including confirmation dialogs, and coordinating UI state updates for loading indicators,
 * error messages, and navigation triggers. It exposes a StateFlow of ProductDetailUiState for
 * reactive UI updates following the unidirectional data flow pattern.
 *
 * The ViewModel orchestrates interactions between the UI layer and domain use cases, transforming
 * domain results into UI state changes. It manages the complete lifecycle of product detail operations
 * including initial data loading, user-initiated deletion with confirmation, success/error feedback
 * via snackbars, and automatic navigation after successful deletion.
 *
 * All operations are executed within the viewModelScope to ensure proper lifecycle management and
 * automatic cancellation when the ViewModel is cleared.
 *
 * @property deleteProductUseCase The use case responsible for deleting a product from the backend.
 *                                 Encapsulates the business logic and API interactions for product removal.
 * @property getProductByIdUseCase The use case responsible for fetching detailed product information
 *                                 by its unique identifier. Handles data retrieval and error scenarios.
 */

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val deleteProductUseCase: DeleteProductUseCase,
    private val getProductByIdUseCase: GetProductByIdUseCase,
) : ViewModel() {

    /**
     * Private mutable state flow holding the current UI state for the product detail screen.
     *
     * This internal state flow serves as the single source of truth for all screen state including
     * product data, loading indicators, error messages, dialog visibility, and navigation flags. It
     * is initialized with an empty ProductDetailUiState and updated through various ViewModel functions
     * as operations progress and complete.
     *
     * All state modifications use the update function to ensure thread-safe atomic updates, preventing
     * race conditions when multiple state changes occur concurrently. Direct access is restricted to
     * the ViewModel to maintain encapsulation and enforce proper state management patterns.
     */

    private val _uiState: MutableStateFlow<ProductDetailUiState> =
        MutableStateFlow(ProductDetailUiState())

    /**
     * Public read-only state flow exposing the current UI state to observers.
     *
     * This StateFlow provides the UI layer with reactive access to all screen state including product
     * details, loading status, error messages, dialog visibility, and navigation triggers. The UI
     * collects from this flow to automatically update when state changes occur, ensuring the displayed
     * information is always synchronized with the current state.
     *
     * Exposed as an immutable StateFlow to prevent external modification and ensure that all state
     * changes flow through the ViewModel's functions, maintaining data integrity and predictable
     * state management throughout the screen lifecycle.
     */

    val uiState: StateFlow<ProductDetailUiState> = _uiState.asStateFlow()

    /**
     * Loads product details from the backend by product ID.
     *
     * This function initiates an asynchronous operation to fetch complete product information using
     * the provided product identifier. It updates the UI state throughout the loading process,
     * displaying loading indicators during the fetch operation, populating the product data on success,
     * or showing error messages if the operation fails.
     *
     * The function collects from the GetProductByIdUseCase result flow and transforms domain Result
     * types into appropriate UI state updates. Loading state is set immediately when the operation
     * begins, success state populates the product property with fetched data, and error state displays
     * a user-friendly error message while clearing any previously loaded product data.
     *
     * This function should be called when the screen first loads or when the product needs to be
     * refreshed, typically from the composable's LaunchedEffect or initialization logic.
     *
     * @param productId The unique identifier of the product to load. Must be a valid product ID that
     *                  exists in the backend system, otherwise an error state will be triggered.
     */

    fun loadProduct(productId: String) {
        viewModelScope.launch {
            getProductByIdUseCase(productId).collect { result ->
                _uiState.update { state ->
                    when (result) {
                        is Result.Loading ->
                            state.copy(isLoading = true, error = null)

                        is Result.Success ->
                            state.copy(
                                isLoading = false,
                                product = result.data,
                                error = null
                            )

                        is Result.Error ->
                            state.copy(
                                isLoading = false,
                                product = null,
                                error = result.exception.message ?: "Error al cargar el producto"
                            )
                    }
                }
            }
        }
    }

    /**
     * Toggles the visibility of the delete confirmation decision dialog.
     *
     * This function controls whether the confirmation dialog is displayed before proceeding with
     * product deletion. It updates the UI state's decisionDialogVisible flag, which the UI observes
     * to show or hide the confirmation dialog component.
     *
     * Typically called with true when the user clicks the delete button to show the confirmation
     * dialog, and with false when the user cancels the deletion or after the dialog is dismissed.
     * The dialog provides a safety mechanism to prevent accidental deletions by requiring explicit
     * user confirmation before executing the destructive operation.
     *
     * @param show true to display the confirmation dialog, false to hide it.
     */

    fun toggleDecisionDialog(show: Boolean) {
        _uiState.update { it.copy(decisionDialogVisible = show) }
    }

    /**
     * Marks the snackbar as shown and hides it from the UI.
     *
     * This function resets the snackbarVisible flag to false after the snackbar has been displayed
     * to the user. It should be called by the UI layer after the snackbar notification appears and
     * is dismissed, either automatically after a timeout or manually by the user.
     *
     * Resetting this flag prevents the snackbar from reappearing on configuration changes or
     * recompositions and prepares the state for the next operation that may need to display a
     * snackbar notification.
     */
    fun onSnackbarShown() {
        _uiState.update { it.copy(snackbarVisible = false) }
    }

    /**
     * Resets the navigation flag after the back navigation has been handled.
     *
     * This function clears the shouldNavigateBack flag after the UI has processed the navigation
     * trigger and navigated to the previous screen. It prevents repeated navigation attempts that
     * could occur on configuration changes, recompositions, or if the state is observed multiple times.
     *
     * Should be called by the UI layer immediately after executing the navigation action in response
     * to the shouldNavigateBack flag being set to true, ensuring the navigation only occurs once per
     * deletion operation.
     */

    fun onNavigatedBackHandled() {
        _uiState.update { it.copy(shouldNavigateBack = false) }
    }

    /**
     * Deletes a product from the backend and manages the complete deletion workflow.
     *
     * This function initiates an asynchronous operation to permanently delete a product identified
     * by the provided ID. It manages the entire deletion workflow including loading state during
     * the operation, hiding the confirmation dialog, displaying success or error notifications via
     * snackbar, and triggering back navigation on successful deletion.
     *
     * The function collects from the DeleteProductUseCase result flow and transforms domain Result
     * types into appropriate UI state updates. On success, it sets snackbarVisible to true for user
     * feedback, shouldNavigateBack to true to trigger navigation, and hides the confirmation dialog.
     * On error, it displays the error message via snackbar while preventing navigation, allowing the
     * user to stay on the screen and potentially retry the operation.
     *
     * The loading state is managed throughout the operation to provide visual feedback and prevent
     * concurrent deletion attempts. The confirmation dialog is automatically closed regardless of
     * the operation outcome.
     *
     * @param id The unique identifier of the product to delete. Must be a valid product ID that
     *           exists in the backend system. Deletion is permanent and cannot be undone.
     */
    fun deleteProduct(id: String) {
        viewModelScope.launch {
            deleteProductUseCase(id).collect { result ->
                _uiState.update { state ->
                    when (result) {
                        is Result.Loading ->
                            state.copy(isLoading = true)

                        is Result.Success ->
                            state.copy(
                                isLoading = false,
                                error = null,
                                decisionDialogVisible = false,
                                snackbarVisible = true,
                                shouldNavigateBack = true,
                            )

                        is Result.Error ->
                            state.copy(
                                isLoading = false,
                                error = result.exception.message,
                                decisionDialogVisible = false,
                                snackbarVisible = true,
                                shouldNavigateBack = false,
                            )
                    }
                }
            }
        }
    }
}
