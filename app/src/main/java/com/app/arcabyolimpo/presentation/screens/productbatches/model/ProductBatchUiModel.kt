package com.app.arcabyolimpo.presentation.screens.productbatches.model

import com.app.arcabyolimpo.domain.model.productbatches.ProductBatch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

/**
 * Data model representing a Product Batch in the domain layer.
 * 
 * @param idProducto: String -> product batch ID
 * @param nombre: String -> name of the product
 * @param precioUnitario: String -> unit price of the product
 * @param descripcion: String -> description of the product
 * @param imagen: String? -> image URL of the product (nullable)
 * @param disponible: String -> availability status of the product now as string "Caducado" or "Disponible"
 * @param idInventario: String -> inventory ID associated with the product
 * @param precioVenta: String -> sale price of the product
 * @param cantidadProducida: Int -> quantity produced in the batch
 * @param fechaCaducidad: String? -> expiration date of the product (nullable)
 * @param fechaRealizacion: String -> production date of the product
 */
data class ProductBatchUiModel(
    val idProducto: String,
    val nombre: String,
    val precioUnitario: String,
    val descripcion: String,
    val imagen: String?,
    val disponible: String,
    val idInventario: String,

    val precioVenta: Double,
    val fechaCaducidad: String?,
    val fechaRealizacion: String,

    val precioVentaFormatted: String,
    val cantidadProducida: Int,
    val fechaCaducidadFormatted: String?,
    val fechaRealizacionFormatted: String,
)
