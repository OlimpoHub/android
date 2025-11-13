package com.app.arcabyolimpo.presentation.screens.productbatches.mapper

import com.app.arcabyolimpo.domain.model.productbatches.ProductBatch
import com.app.arcabyolimpo.presentation.screens.productbatches.model.ProductBatchUiModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

fun ProductBatch.toUiModel(): ProductBatchUiModel {
    val dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")

    fun String.toReadableDate(): String =
        try {
            LocalDate.parse(this.substring(0, 10)).format(dateFormatter)
        } catch (e: DateTimeParseException) {
            this
        }

    return ProductBatchUiModel(
        idProducto = idProducto,
        nombre = nombre,
        precioUnitario = precioUnitario,
        descripcion = descripcion.ifBlank { "Sin descripción" },
        imagen = imagen ?: "",
        disponible = disponible,
        idInventario = idInventario,
        precioVentaFormatted = "$$precioVenta MXN",
        cantidadProducida = cantidadProducida,
        fechaCaducidadFormatted = fechaCaducidad?.toReadableDate(),
        fechaRealizacionFormatted = fechaRealizacion.toReadableDate(),
    )
}
