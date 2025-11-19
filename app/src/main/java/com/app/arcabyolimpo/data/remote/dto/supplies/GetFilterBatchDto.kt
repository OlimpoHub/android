package com.app.arcabyolimpo.data.remote.dto.supplies

import com.google.gson.annotations.SerializedName

/**
 * Represents the filter metadata returned by the backend,
 * containing the available options to build the filtering UI.
 *
 * @property acquisitionType A list of available acquisition types for filtering.
 */
data class GetFilterBatchDto(
    @SerializedName("acquisitionTypes") val acquisitionType: List<String>? = null,
)
