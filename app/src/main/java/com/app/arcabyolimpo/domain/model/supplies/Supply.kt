package com.app.arcabyolimpo.domain.model.supplies

/**
 * Domain model representing a supply item in the system.
 *
 * @property id Unique identifier of the supply.
 * @property name The name of the supply.
 * @property imageUrl The URL or path to the supply's image.
 * @property unitMeasure The unit of measurement used for the supply (e.g., "kg", "pcs").
 * @property batch A list of [SupplyBatch] objects representing different batches of the supply.
 */
data class Supply(
    val id: String,
    val name: String,
    val imageUrl: String?,
    val unitMeasure: String,
    val batch: List<SupplyBatch>,
)

/**
 * Domain model representing a batch of a supply.
 *
 * @property quantity The number of units in this batch.
 * @property expirationDate The expiration date of the batch, formatted as a string.
 */
data class SupplyBatch(
    val quantity: Int,
    val expirationDate: String,
)

/**
 * Data class representing a supply batch
 * with additional information.
 *
 * @property supplyId The unique identifier of the supply.
 * @property quantity The quantity of the supply batch.
 * @property expirationDate The expiration date of the supply batch.
 * @property adquisition The type of acquisition for the supply batch.
 * @property boughtDate The date when the supply batch was bought.
 */
data class RegisterSupplyBatch(
    val supplyId: String,
    val quantity: Int,
    val expirationDate: String,
    val acquisition: String,
    val boughtDate: String,
)

/**
 * Data class representing an acquisition
 *
 * @property id The unique identifier of the acquisition
 * @property description The description of the acquisition.
 */
data class Acquisition(
    val id: String,
    val description: String,
)

/**
 * Data class representing a success message.
 *
 * @property message The content of the success message.
 */
data class SuccessMessage(
    val message: String,
)

/**
 * Data class representing a supply batch item.
 *
 * @property id The unique identifier of the supply batch.
 * @property quantity The quantity of the supply batch.
 * @property expirationDate The expiration date of the supply batch.
 * @property adquisitionType The type of acquisition for the supply batch.
 * @property boughtDate The date when the supply batch was bought.
 * @property measure The unit of measurement for the supply batch.
 */
data class SupplyBatchItem(
    val id: String,
    val quantity: Int,
    val expirationDate: String,
    val adquisitionType: String,
    val boughtDate: String,
    val measure: String,
)

/**
 * Data class representing a list of supply batches.
 *
 * @property batch A list of [SupplyBatchItem] objects representing different batches of the supply.
 */
data class SupplyBatchList(
    val batch: List<SupplyBatchItem>,
)
