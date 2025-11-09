package com.app.arcabyolimpo.domain.model.supplies

data class Supply(
    val id: String,
    val name: String,
    val imageUrl: String,
    val unitMeasure: String,
    val batch: List<SupplyBatch>,
)

data class SupplyBatch(
    val quantity: Int,
    val expirationDate: String,
)

data class FilterData(
    val categories: List<String>,
    val measures: List<String>,
    val workshops: List<String>,
)
