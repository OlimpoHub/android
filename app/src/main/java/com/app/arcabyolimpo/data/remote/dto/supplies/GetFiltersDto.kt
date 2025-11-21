package com.app.arcabyolimpo.data.remote.dto.supplies

import com.google.gson.annotations.SerializedName

/**
 * This class is used to deserialize the JSON response from the backend endpoint
 * that provides available filters for supplies. Each property corresponds to
 * a list of possible values under a specific filter category.
 *
 * Example JSON:
 * ```
 * {
 *   "categories": ["Herramientas", "Materiales"],
 *   "measures": ["Litros", "Piezas"],
 *   "workshops": ["Taller A", "Taller B"]
 * }
 * ```
 *
 * @property categories Optional list of category names that can be used to filter supplies.
 * @property measures Optional list of unit measures (e.g., "Litros", "Piezas") available for filtering.
 * @property workshops Optional list of workshops or departments associated with the supplies.
 */
data class GetFiltersDto(
    @SerializedName("categories") val categories: List<String>? = null,
    @SerializedName("measures") val measures: List<String>? = null,
    @SerializedName("workshops") val workshops: List<String>? = null,
)
