package com.app.arcabyolimpo.presentation.screens.productbatches.productBatchRegister

import com.app.arcabyolimpo.presentation.screens.productbatches.model.ProductBatchUiModel

data class ProductBatchRegisterUiState(
    val idProducto: String = "",
    val precioVenta: String = "",
    val cantidadProducida: String = "",
    val fechaCaducidad: String = "",
    val fechaRealizacion: String = "",
    val error: String? = null,
)
