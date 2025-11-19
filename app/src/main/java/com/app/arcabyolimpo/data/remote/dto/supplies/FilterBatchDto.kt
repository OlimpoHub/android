package com.app.arcabyolimpo.data.remote.dto.supplies

/**
 * Represents the request body used when filtering supply batches.
 *
 * @property idSupply The ID of the supply whose batches will be filtered.
 * @property filters A map containing filter categories as keys and a list of selected values.
 * @property order Optional parameter to define sorting order (e.g., "ASC" or "DESC").
 */
data class FilterRequestDto(
    val idSupply: String,
    val filters: Map<String, List<String>>,
    val order: String?
)
