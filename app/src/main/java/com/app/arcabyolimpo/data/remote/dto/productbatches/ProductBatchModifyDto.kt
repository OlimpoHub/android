package com.app.arcabyolimpo.data.remote.dto.productbatches

/**
 * Data Transfer Object (DTO) for modifying an existing product batch.
 *
 * This class models the JSON payload sent to the API endpoint when updating
 * a product batch. It contains the fields that can be modified.
 *
 * @param PrecioVenta The new selling price for the items in this batch.
 * @param CantidadProducida The new total number of items produced in this batch.
 * @param FechaCaducidad The new expiration date of the batch, in string format. Can be null.
 * @param FechaRealizacion The new production date of the batch, in string format.
 */
data class ProductBatchModifyDto(
    val PrecioVenta: Double,
    val CantidadProducida: Int,
    val FechaCaducidad: String?,
    val FechaRealizacion: String,
)
