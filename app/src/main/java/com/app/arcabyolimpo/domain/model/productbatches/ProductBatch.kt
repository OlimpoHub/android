package com.app.arcabyolimpo.domain.model.productbatches

data class ProductBatch(
    val idProducto: String,
    val nombre: String,
    val precioUnitario: String,
    val descripcion: String,
    val imagen: String?,
    val disponible: Int,
    val idInventario: String,
    val precioVenta: String,
    val cantidadProducida: Int,
    val fechaCaducidad: String?,
    val fechaRealizacion: String,
)
