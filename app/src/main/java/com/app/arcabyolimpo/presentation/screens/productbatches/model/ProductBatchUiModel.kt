package com.app.arcabyolimpo.presentation.screens.productbatches.model

data class ProductBatchUiModel(
    val idProducto: String,
    val nombre: String,
    val precioUnitario: String,
    val descripcion: String,
    val imagen: String,
    val disponible: Boolean,
    val idInventario: String,
    val precioVenta: String,
    val cantidadProducida: Int,
    val fechaCaducidad: String,
    val fechaRealizacion: String,
)
