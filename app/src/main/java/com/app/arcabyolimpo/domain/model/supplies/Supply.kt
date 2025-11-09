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
    val sections: Map<String, List<String>>,
) {
    fun asSections(): List<Section> =
        sections.map { (title, options) ->
            Section(title, options)
        }
}

data class Section(
    val title: String,
    val items: List<String>,
)
