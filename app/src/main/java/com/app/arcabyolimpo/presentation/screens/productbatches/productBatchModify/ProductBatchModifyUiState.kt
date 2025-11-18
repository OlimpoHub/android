package com.app.arcabyolimpo.presentation.screens.productbatches.productBatchModify

data class ProductBatchModifyUiState(
    val precioVenta: String = "",
    val cantidadProducida: String = "",
    val fechaCaducidad: String = "",
    val fechaRealizacion: String = "",
    val error: String? = null,
)
