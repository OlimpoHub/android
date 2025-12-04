package com.app.arcabyolimpo.presentation.screens.productbatches.model

import com.app.arcabyolimpo.domain.model.productbatches.ProductBatch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

/**
 * Data model representing a Product Batch tailored for the UI layer.
 *
 * This class adapts the domain model data into a format that is ready for display,
 * including formatted strings for dates and prices. It also calculates the availability status.
 *
 * @param idProducto The unique identifier of the product.
 * @param nombre The name of the product.
 * @param precioUnitario The base unit price of the product.
 * @param descripcion The description of the product.
 * @param imagen The URL for the product's image, which can be null.
 * @param disponible The availability status, derived as either "Disponible" (Available) or "Caducado" (Expired).
 * @param idInventario The inventory ID associated with this product batch.
 * @param precioVenta The raw selling price as a Double.
 * @param fechaCaducidad The raw expiration date string, which can be null.
 * @param fechaRealizacion The raw production date string.
 * @param precioVentaFormatted The selling price formatted as a currency string for display.
 * @param cantidadProducida The integer quantity of items produced in the batch.
 * @param fechaCaducidadFormatted The expiration date formatted for display (e.g., "dd/MM/yyyy"), which can be null.
 * @param fechaRealizacionFormatted The production date formatted for display (e.g., "dd/MM/yyyy").
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
