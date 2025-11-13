package com.app.arcabyolimpo.data.remote.dto.beneficiaries

import com.google.gson.annotations.SerializedName

/**
 * This class is used to deserialize the JSON response from the backend endpoint
 * that provides available filters for supplies. Each property corresponds to
 * a list of possible values under a specific filter category.
 *
 * Example JSON:
 * ```
 * {
 *   "disabilities": [
 *         "Fisica",
 *         "Motora"
 *     ]
 * }
 * ```
 *
 * @property disabilities Optional list of disabilities names that can be used to filter beneficiaries.
 */
data class GetBeneficiariesDisabilitiesDto(
    @SerializedName("disabilities") val disabilities: List<String>? = null,
)
