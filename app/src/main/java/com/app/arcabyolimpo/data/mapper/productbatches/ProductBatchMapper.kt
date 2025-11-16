package com.app.arcabyolimpo.data.mapper.productbatches

import com.app.arcabyolimpo.data.remote.dto.productbatches.ProductBatchDto
import com.app.arcabyolimpo.data.remote.dto.productbatches.ProductBatchRegisterDto
import com.app.arcabyolimpo.domain.model.productbatches.ProductBatch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Mapping the Dto to Domain model and viceversa
 *
 * @return ProductBatch
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
 * Helper function to reformat the date string.
 * It parses "dd/MM/yyyy" and returns "yyyy-MM-dd".
 */
private fun reformatDate(dateString: String): String {
    val localDate = LocalDate.parse(dateString, inputFormatter)
    return localDate.format(outputFormatter)
}

fun ProductBatch.toRegisterDto(): ProductBatchRegisterDto =
    ProductBatchRegisterDto(
        idProducto = idProducto,
        precioVenta = precioVenta.toDoubleOrNull() ?: 0.0,
        cantidadProducida = cantidadProducida,
        fechaCaducidad = fechaCaducidad?.let { reformatDate(it) },
        fechaRealizacion = reformatDate(fechaRealizacion),
    )
