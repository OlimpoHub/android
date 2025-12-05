package com.app.arcabyolimpo.presentation.screens.productbatches.productBatchModify

/**
 * Represents the UI state for the Product Batch Modification screen.
 * This data class encapsulates all the data needed to render the screen, including
 * user inputs, validation status, and error messages.
 *
 * @property precioVenta The current selling price of the batch, as a string.
 * @property cantidadProducida The current quantity of items in the batch, as a string.
 * @property fechaCaducidad The expiration date of the batch, formatted as a string (e.g., "dd/MM/yyyy").
 * @property fechaRealizacion The production date of the batch, formatted as a string (e.g., "dd/MM/yyyy").
 * @property error A nullable string holding a general error message (e.g., for network failures).
 * @property isPrecioVentaError A boolean flag indicating if the selling price field has a validation error.
 * @property isCantidadProducidaError A boolean flag indicating if the produced quantity field has a validation error.
 * @property isFechaRealizacionError A boolean flag indicating if the production date field has a validation error.
 * @property isFechaCaducidadError A boolean flag indicating if the expiration date field has a validation error.
 */

data class ProductBatchModifyUiState(
    val precioVenta: String = "",
    val cantidadProducida: String = "0",
    val fechaCaducidad: String = "",
    val fechaRealizacion: String = "",
    val error: String? = null,
    val isPrecioVentaError: Boolean = false,
    val isCantidadProducidaError: Boolean = false,
    val isFechaRealizacionError: Boolean = false,
    val isFechaCaducidadError: Boolean = false,
)
