package com.app.arcabyolimpo.data.remote.dto.productbatches

data class ProductBatchRegisterDto(
    val idProducto: String,
    val precioVenta: Double,
    val cantidadProducida: Int,
    val fechaCaducidad: String?,
    val fechaRealizacion: String,
)
