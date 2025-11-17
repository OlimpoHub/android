package com.app.arcabyolimpo.data.remote.dto.workshops

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object that represents the response of a delete Workshop operation.
 *
 * This DTO is returned by the backend after attempting to delete a workshop,
 * for example, when calling the [ArcaApi.deleteWorkshops] endpoint.
 */
data class DeleteResponseWorkshopDto (
    /**
     * Indicates whether the delete operation was successful.
     *
     * If `true`, the workshop was deleted correctly. If `false`,
     * the operation failed and the [message] usually contains
     * more details about the error.
     */

    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
)
