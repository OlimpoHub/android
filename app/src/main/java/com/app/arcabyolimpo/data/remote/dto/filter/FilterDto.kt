package com.app.arcabyolimpo.data.remote.dto.filter

/**
 * This class encapsulates both the selected filters (e.g., categories, measures,
 * workshops) and the desired sorting order (ascending or descending).
 *
 * Example JSON body sent to the API:
 * ```
 * {
 *   "filters": {
 *     "Categorías": ["Materiales", "Herramientas"],
 *     "Medidas": ["Piezas"]
 *   },
 *   "order": "ASC"
 * }
 * ```
 *
 * @property filters Map of filter names to the list of selected values.
 * Each key corresponds to a section (e.g., "Categorías", "Medidas", "Talleres").
 * @property order Optional sort direction — either `"ASC"` for ascending or `"DESC"` for descending.
 */
data class FilterDto(
    val filters: Map<String, List<String>> = emptyMap(),
    val order: String? = null,
)

/**
 * Utility function that creates a [FilterDto] object from the user’s current filter selections.
 *
 * The resulting [FilterDto] can then be sent to the API to perform filtered
 * queries or sorting operations.
 *
 * @param selectedMap Map containing section titles and their selected filter values.
 * Empty lists are automatically removed.
 * @param order Optional sorting order ("ASC" or "DESC") to include in the DTO.
 * @return A [FilterDto] ready to be sent to the API.
 */
fun createFilterSuppliesDto(
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
