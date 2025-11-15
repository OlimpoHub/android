package com.app.arcabyolimpo.presentation.screens.productbatches.productBatchRegister

import com.app.arcabyolimpo.presentation.screens.productbatches.model.ProductBatchUiModel

/** UI state for ProductBatchRegisterScreen.
 * @param idProducto String -> ID of the product
 * @param precioVenta String -> sale price
 * @param cantidadProducida String -> produced quantity
 * @param fechaCaducidad String -> expiration date
 * @param fechaRealizacion String -> production date
 * @param error String? -> error message if any
*/
data class ProductBatchRegisterUiState(
    val idProducto: String = "",
    val precioVenta: String = "",
    val cantidadProducida: String = "",
    val fechaCaducidad: String = "",
    val fechaRealizacion: String = "",
    val error: String? = null,
)
