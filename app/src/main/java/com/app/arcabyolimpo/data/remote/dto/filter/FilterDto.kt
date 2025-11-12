package com.app.arcabyolimpo.data.remote.dto.filter

data class FilterDto(
    val filters: Map<String, List<String>> = emptyMap(),
    val order: String? = null,
)

fun createFilterDto(
    selectedMap: Map<String, MutableList<String>>,
    order: String? = null,
): FilterDto {
    val cleaned =
        selectedMap
            .filterValues { it.isNotEmpty() }
            .mapValues { it.value.toList() }

    return FilterDto(
        filters = cleaned,
        order = order,
    )
}
