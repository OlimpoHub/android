package com.app.arcabyolimpo.data.remote.dto.supplies

import com.google.gson.annotations.SerializedName

data class FilterSuppliesDto(
    @SerializedName("categories") val categories: List<String>? = null,
    @SerializedName("measures") val measures: List<String>? = null,
    @SerializedName("workshops") val workshops: List<String>? = null,
    @SerializedName("order") val order: String? = null,
)

fun createFilterSuppliesDto(
    selectedMap: Map<String, List<String>>,
    order: String? = null,
): FilterSuppliesDto {
    val dtoMap = mutableMapOf<String, List<String>>()
    selectedMap.forEach { (key, value) ->
        if (value.isNotEmpty()) {
            dtoMap[key] = value
        }
    }

    return FilterSuppliesDto(
        categories = dtoMap["Categorías"],
        measures = dtoMap["Medidas"],
        workshops = dtoMap["Talleres"],
        order = order,
    )
}
