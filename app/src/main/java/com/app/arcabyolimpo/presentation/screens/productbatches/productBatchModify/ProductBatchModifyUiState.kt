package com.app.arcabyolimpo.presentation.screens.productbatches.productBatchModify

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
