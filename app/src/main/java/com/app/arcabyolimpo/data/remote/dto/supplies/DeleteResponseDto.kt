package com.app.arcabyolimpo.data.remote.dto.supplies

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object that represents the response of a delete supply operation.
 *
 * This DTO is returned by the backend after attempting to delete a supply,
 * for example, when calling the [ArcaApi.deleteOneSupply] endpoint.
 */
data class DeleteResponseDto (
    /**
     * Indicates whether the delete operation was successful.
     *
     * If `true`, the supply was deleted correctly. If `false`,
     * the operation failed and the [message] usually contains
     * more details about the error.
     */
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
)