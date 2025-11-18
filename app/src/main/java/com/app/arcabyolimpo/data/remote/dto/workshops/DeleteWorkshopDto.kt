package com.app.arcabyolimpo.data.remote.dto.workshops

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object used to request the deletion of a workshop.
 *
 * This DTO is sent to the backend when the user wants to delete
 * a specific workshop, identified by its unique ID.
 *
 * It is used, for example, in the [ArcaApi.deleteWorkshops] endpoint.
 */
data class DeleteWorkshopDto (
    /**
     * Unique identifier of the workshop to be deleted.
     * This value is serialized as "id" in the JSON body sent to the API.
     */
    @SerializedName("id") val id: String
)