package com.app.arcabyolimpo.presentation.screens.product.productDetail

import com.app.arcabyolimpo.domain.model.product.Product

/**
 * Represents the UI state for the product detail screen.
 *
 * This immutable data class encapsulates all state information needed to render the product detail
 * screen, including loading indicators, product data, error messages, dialog visibility flags, and
 * navigation triggers. It follows the unidirectional data flow pattern where the ViewModel emits
 * state updates and the UI reacts to these changes.
 *
 * The state manages both transient UI elements (like dialogs and snackbars) and persistent data
 * (like the product information), ensuring a single source of truth for all screen-related state.
 * All properties have sensible defaults to represent the initial empty state when the screen first
 * loads.
 *
 * @property isLoading Indicates whether a network operation (loading or deleting) is in progress.
 *                     When true, the UI typically displays a loading indicator and may disable
 *                     interactive elements to prevent concurrent operations.
 * @property product The product data to display on the screen, or null if no product is loaded yet
 *                   or if an error occurred during loading. Contains all product information including
 *                   name, description, price, images, and other details.
 * @property error An error message string to display to the user when operations fail, or null if
 *                 no error has occurred. Typically shown in a snackbar or error dialog. Examples
 *                 include network errors, invalid product IDs, or deletion failures.
 * @property deleted Indicates whether the product has been successfully deleted. When true, this
 *                   flag can be used to trigger cleanup operations or update UI state before navigation.
 *                   Note: This property may be deprecated in favor of shouldNavigateBack.
 * @property decisionDialogVisible Controls the visibility of the confirmation dialog shown before
 *                                 deleting a product. When true, the dialog is displayed asking the
 *                                 user to confirm the destructive action.
 * @property snackbarVisible Controls the visibility of the snackbar notification. When true, a snackbar
 *                           is displayed showing success or error messages after operations complete.
 *                           Should be reset to false after the snackbar is shown.
 * @property shouldNavigateBack A navigation trigger flag that indicates the screen should navigate back
 *                              to the previous screen. Set to true after successful product deletion.
 *                              Should be reset to false after the navigation is handled to prevent
 *                              repeated navigation attempts.
 */

data class ProductDetailUiState(
    val isLoading: Boolean = false,
    val product: Product? = null,
    val error: String? = null,
    val deleted: Boolean = false,
    val decisionDialogVisible: Boolean = false,
    val snackbarVisible: Boolean = false,
    val shouldNavigateBack: Boolean = false,
)
