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
) {
    fun asSections(): List<Section> =
        listOf(
            Section("Categorías", categories),
            Section("Medidas", measures),
            Section("Talleres", workshops),
        )
}

data class Section(
    val title: String,
    val items: List<String>,
)
