package com.app.arcabyolimpo.data.remote.dto.productbatches

data class ProductBatchModifyDto(
    val PrecioVenta: Double,
    val CantidadProducida: Int,
    val FechaCaducidad: String?,
    val FechaRealizacion: String,
)
