package com.app.arcabyolimpo.data.remote.dto.supplies

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object used to request the deletion of a supply.
 *
 * This DTO is sent to the backend when the user wants to delete
 * a specific supply, identified by its unique ID.
 *
 * It is used, for example, in the [ArcaApi.deleteOneSupply] endpoint.
 */
data class DeleteDto (
    /**
     * Unique identifier of the supply to be deleted.
     * This value is serialized as "id" in the JSON body sent to the API.
     */
    @SerializedName("id") val id: String
)