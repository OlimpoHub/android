package com.app.arcabyolimpo.data.mapper.productbatches

import com.app.arcabyolimpo.data.remote.dto.productbatches.ProductBatchDto
import com.app.arcabyolimpo.data.remote.dto.productbatches.ProductBatchModifyDto
import com.app.arcabyolimpo.data.remote.dto.productbatches.ProductBatchRegisterDto
import com.app.arcabyolimpo.domain.model.productbatches.ProductBatch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Converts a [ProductBatchDto] (data layer) to a [ProductBatch] (domain layer).
 *
 * @return The corresponding [ProductBatch] domain model.
 */
fun ProductBatchDto.toDomain(): ProductBatch =
    ProductBatch(
        idProducto = idProducto,
        nombre = nombre,
        precioUnitario = precioUnitario,
        descripcion = descripcion,
        imagen = imagen,
        disponible = disponible,
        idInventario = idInventario,
        precioVenta = precioVenta,
        cantidadProducida = cantidadProducida,
        fechaCaducidad = fechaCaducidad,
        fechaRealizacion = fechaRealizacion,
    )

/**
 * Converts a [ProductBatch] (domain layer) to a [ProductBatchDto] (data layer).
 *
 * @return The corresponding [ProductBatchDto] data transfer object.
 */
fun ProductBatch.toDto(): ProductBatchDto =
    ProductBatchDto(
        idProducto = idProducto,
        nombre = nombre,
        precioUnitario = precioUnitario,
        descripcion = descripcion,
        imagen = imagen,
        disponible = disponible,
        idInventario = idInventario,
        precioVenta = precioVenta,
        cantidadProducida = cantidadProducida,
        fechaCaducidad = fechaCaducidad,
        fechaRealizacion = fechaRealizacion,
    )

private val inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
private val outputFormatter = DateTimeFormatter.ISO_LOCAL_DATE // This is "yyyy-MM-dd"

/**
 * Reformats a date string from "dd/MM/yyyy" to "yyyy-MM-dd" format.
 *
 * This is a helper function used to ensure dates are in the correct format expected
 * by the remote API.
 *
 * @param dateString The date string in "dd/MM/yyyy" format.
 * @return The date string reformatted to "yyyy-MM-dd".
 */
private fun reformatDate(dateString: String): String {
    val localDate = LocalDate.parse(dateString, inputFormatter)
    return localDate.format(outputFormatter)
}

/**
 * Converts a [ProductBatch] (domain layer) to a [ProductBatchRegisterDto] (data layer).
 *
 * This mapping is specifically for creating a new product batch. It handles the conversion
 * of price to a Double and reformats the date strings to "yyyy-MM-dd".
 *
 * @return A [ProductBatchRegisterDto] ready for network serialization.
 */
fun ProductBatch.toRegisterDto(): ProductBatchRegisterDto =
    ProductBatchRegisterDto(
        idProducto = idProducto,
        precioVenta = precioVenta.toDoubleOrNull() ?: 0.0,
        cantidadProducida = cantidadProducida,
        fechaCaducidad =
            fechaCaducidad
                ?.takeIf { it.isNotBlank() }
                ?.let { reformatDate(it) },
        fechaRealizacion = reformatDate(fechaRealizacion),
    )

/**
 * Converts a [ProductBatch] (domain layer) to a [ProductBatchModifyDto] (data layer).
 *
 * This mapping is specifically for updating an existing product batch. It handles the
 * conversion of price and reformats date strings to the API-required "yyyy-MM-dd" format.
 *
 * @return A [ProductBatchModifyDto] ready for network serialization.
 */
fun ProductBatch.toModifyDto(): ProductBatchModifyDto =
    ProductBatchModifyDto(
        PrecioVenta = precioVenta.toDoubleOrNull() ?: 0.0,
        CantidadProducida = cantidadProducida,
        FechaCaducidad =
            fechaCaducidad
                ?.takeIf { it.isNotBlank() }
                ?.let { reformatDate(it) },
        FechaRealizacion = reformatDate(fechaRealizacion),
    )
