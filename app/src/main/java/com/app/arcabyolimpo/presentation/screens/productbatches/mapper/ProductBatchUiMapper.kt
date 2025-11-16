package com.app.arcabyolimpo.presentation.screens.productbatches.mapper

import com.app.arcabyolimpo.domain.model.productbatches.ProductBatch
import com.app.arcabyolimpo.presentation.screens.productbatches.model.ProductBatchUiModel
import com.app.arcabyolimpo.presentation.screens.productbatches.productBatchRegister.ProductBatchRegisterUiState
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

/** Maps a [ProductBatch] domain model to a [ProductBatchUiModel] for UI representation.
 *  @return ProductBatchUiModel
*/
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
        disponible = if (disponible == 0) "Caducado" else "Disponible",
        idInventario = idInventario,
        precioVentaFormatted = "$$precioVenta MXN",
        cantidadProducida = cantidadProducida,
        fechaCaducidadFormatted = fechaCaducidad?.toReadableDate(),
        fechaRealizacionFormatted = fechaRealizacion.toReadableDate(),
    )
}

/** Maps a [ProductBatchRegisterUiState] UI state to a [ProductBatch] domain model for business logic.
 *  @return ProductBatch
*/
fun ProductBatchRegisterUiState.toDomain() =
    ProductBatch(
        idProducto = idProducto,
        nombre = "",
        precioVenta = precioVenta,
        descripcion = "",
        disponible = 0,
        cantidadProducida = cantidadProducida.toIntOrNull() ?: 0,
        fechaRealizacion = fechaRealizacion,
        fechaCaducidad = fechaCaducidad,
        idInventario = "",
        precioUnitario = "0.0",
        imagen = "",
    )
