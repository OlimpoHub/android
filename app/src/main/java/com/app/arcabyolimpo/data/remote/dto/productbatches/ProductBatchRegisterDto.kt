package com.app.arcabyolimpo.data.remote.dto.productbatches

/**
 * Data Transfer Object (DTO) for registering a new product batch.
 *
 * This class models the JSON payload sent to the API endpoint when creating
 * a new product batch. It contains only the essential data required for the
 * registration process.
 *
 * @param idProducto The unique identifier of the product associated with this batch.
 * @param precioVenta The selling price for the items in this batch.
 * @param cantidadProducida The total number of items produced in this batch.
 * @param fechaCaducidad The expiration date of the batch, in string format. Can be null.
 * @param fechaRealizacion The production date of the batch, in string format.
 */
data class ProductBatchRegisterDto(
    val idProducto: String,
    val precioVenta: Double,
    val cantidadProducida: Int,
    val fechaCaducidad: String?,
    val fechaRealizacion: String,
)
