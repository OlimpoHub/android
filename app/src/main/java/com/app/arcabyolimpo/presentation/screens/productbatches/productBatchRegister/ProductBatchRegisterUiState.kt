package com.app.arcabyolimpo.presentation.screens.productbatches.productBatchRegister

import com.app.arcabyolimpo.presentation.screens.productbatches.model.ProductBatchUiModel

/**
 * Represents the state of the Product Batch Registration UI at any given time.
 * This data class holds all the information related to user input, validation errors,
 * and data loaded from the backend, ensuring a single source of truth for the UI.
 *
 * @property idProducto The unique identifier of the selected product.
 * @property precioVenta The selling price for the new batch.
 * @property cantidadProducida The quantity of items produced in this batch.
 * @property fechaCaducidad The expiration date of the products in this batch.
 * @property fechaRealizacion The date when the batch was produced.
 * @property error A general error message to be displayed to the user (e.g., network error). Null if no error.
 * @property isIdProductoError Flag to indicate a validation error on the product selection field.
 * @property isPrecioVentaError Flag to indicate a validation error on the selling price field.
 * @property isCantidadProducidaError Flag to indicate a validation error on the produced quantity field.
 * @property isFechaRealizacionError Flag to indicate a validation error on the production date field.
 * @property isFechaCaducidadError Flag to indicate a validation error on the expiration date field.
 * @property productIds A list of all available product IDs to populate the selection dropdown.
 * @property names A list of all available product names, corresponding to the productIds list.
 * @property isLoadingProducts Flag to indicate that the list of products is currently being loaded.
 */
data class ProductBatchRegisterUiState(
    val idProducto: String = "",
    val precioVenta: String = "",
    val cantidadProducida: String = "0",
    val fechaCaducidad: String = "",
    val fechaRealizacion: String = "",
    val error: String? = null,
    val isIdProductoError: Boolean = false,
    val isPrecioVentaError: Boolean = false,
    val isCantidadProducidaError: Boolean = false,
    val isFechaRealizacionError: Boolean = false,
    val isFechaCaducidadError: Boolean = false,
    val productIds: List<String> = emptyList(),
    val names: List<String> = emptyList(),
    val isLoadingProducts: Boolean = false,
)
