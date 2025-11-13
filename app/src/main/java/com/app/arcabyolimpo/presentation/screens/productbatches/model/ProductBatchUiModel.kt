package com.app.arcabyolimpo.presentation.screens.productbatches.model

import com.app.arcabyolimpo.domain.model.productbatches.ProductBatch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

data class ProductBatchUiModel(
    val idProducto: String,
    val nombre: String,
    val precioUnitario: String,
    val descripcion: String,
    val imagen: String?,
    val disponible: Int,
    val idInventario: String,
    val precioVentaFormatted: String,
    val cantidadProducida: Int,
    val fechaCaducidadFormatted: String?,
    val fechaRealizacionFormatted: String,
)
