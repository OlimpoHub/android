package com.app.arcabyolimpo.presentation.screens.productbatches.mapper

import com.app.arcabyolimpo.domain.model.productbatches.ProductBatch
import com.app.arcabyolimpo.presentation.screens.productbatches.model.ProductBatchUiModel
import com.app.arcabyolimpo.presentation.screens.productbatches.productBatchModify.ProductBatchModifyUiState
import com.app.arcabyolimpo.presentation.screens.productbatches.productBatchRegister.ProductBatchRegisterUiState
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

/**
 * Maps a [ProductBatch] domain model to a [ProductBatchUiModel] for UI representation.
 *
 * This extension function transforms the raw data from the domain layer into a display-ready format.
 * It formats dates into a "dd MMM yyyy" pattern, creates a currency-formatted string for the
 * selling price, provides a default description if one is missing, and translates the availability
 * integer into a human-readable string ("Disponible" or "Caducado").
 *
 * @return A [ProductBatchUiModel] instance populated with formatted data suitable for display.
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
        precioVenta = precioVenta.toDoubleOrNull() ?: 0.0,
        fechaCaducidad = fechaCaducidad,
        fechaRealizacion = fechaRealizacion,
        precioVentaFormatted = "$$precioVenta MXN",
        cantidadProducida = cantidadProducida,
        fechaCaducidadFormatted = fechaCaducidad?.toReadableDate(),
        fechaRealizacionFormatted = fechaRealizacion.toReadableDate(),
    )
}

/**
 * Maps a [ProductBatchRegisterUiState] UI state to a [ProductBatch] domain model.
 *
 * This function gathers the user's input from the registration screen's state and converts
 * it into a [ProductBatch] object that can be processed by the domain/data layer. It populates
 * only the fields relevant for creating a new batch, leaving others (like `nombre` or `descripcion`)
 * as empty strings, since they will be fetched later or are not part of the initial creation payload.
 *
 * @return A [ProductBatch] instance representing the new batch to be created.
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

/**
 * Maps a [ProductBatchModifyUiState] UI state to a [ProductBatch] domain model.
 *
 * This function takes the edited data from the modification screen's UI state and converts it
 * into a [ProductBatch] object for the update operation. Similar to the register mapper, it focuses
 * on the fields that can be modified, leaving identifiers and other non-editable data as empty placeholders.
 *
 * @return A [ProductBatch] instance containing the updated information for an existing batch.
 */
fun ProductBatchModifyUiState.toDomain() =
    ProductBatch(
        idProducto = "",
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
