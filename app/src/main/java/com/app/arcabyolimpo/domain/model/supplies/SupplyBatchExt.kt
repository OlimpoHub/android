package com.app.arcabyolimpo.domain.model.supplies

data class SupplyBatchExt(
    val id: String,
    val name: String,
    val imageUrl: String,
    val unitMeasure: String,
    val totalQuantity: Int,
    val workshop: String,
    val category: String,
    val status: Int,
    val batch: List<Batch>
)

data class Batch(
    val quantity: Int,
    val expirationDate: String,
    val adquisitionType: String,
)