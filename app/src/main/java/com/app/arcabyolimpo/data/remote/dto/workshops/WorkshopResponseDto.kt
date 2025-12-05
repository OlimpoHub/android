package com.app.arcabyolimpo.data.remote.dto.workshops

import com.google.gson.annotations.SerializedName

/**
 * DTO representing the full response returned when requesting
 * detailed information about a specific workshop.
 *
 * This object groups both the workshop's core data and the list of
 * beneficiaries associated with it.
 *
 * @param workshopList A list containing the workshop data returned by the API.
 * @param beneficiaries A list of beneficiaries linked to the workshop.
 * The type is generic ([Any]) because the backend structure may vary.
 */
data class WorkshopResponseDto(
    @SerializedName("workshop") val workshopList: List<WorkshopDto>,
    @SerializedName("beneficiaries") val beneficiaries: List<Any>
)

