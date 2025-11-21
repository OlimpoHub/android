package com.app.arcabyolimpo.domain.model.supplies

/** ---------------------------------------------------------------------------------------------- *
 * SupplyBatchExt -> EXTENDED supply data class, with all the attributes the view contains
 * ---------------------------------------------------------------------------------------------- */
data class SupplyBatchExt(
    val id: String,
    val name: String,
    val imageUrl: String?,
    val unitMeasure: String,
    val totalQuantity: Int,
    val workshop: String,
    val category: String,
    val status: Int,
    val batch: List<Batch> = emptyList(),
)

/** ---------------------------------------------------------------------------------------------- *
 * Batch -> Secondary class that contains all the extra attributes from a supply batch
 * ---------------------------------------------------------------------------------------------- */
data class Batch(
    val id: String = "",
    val quantity: Int = 0,
    val expirationDate: String = "",
    val adquisitionType: String = "",
)

/** ---------------------------------------------------------------------------------------------- *
 * FilteredBatch -> Secondary class that contains all the extra attributes from a supply batch
 * ---------------------------------------------------------------------------------------------- */

data class FilteredBatch(
    val id: String = "",
    val idSupply: String = "",
    val quantity: Int = 0,
    val expirationDate: String = "",
    val adquisitionType: String = "",
)
