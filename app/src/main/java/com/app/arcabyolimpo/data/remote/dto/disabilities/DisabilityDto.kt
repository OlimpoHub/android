package com.app.arcabyolimpo.data.remote.dto.disabilities

import com.google.gson.annotations.SerializedName

/**
 * DTO representing detailed information of a disability received from the API.
 *
 * @param id The unique identifier of the disability.
 * @param name The name of the disability
 * @param characteristics The characteristics from the disability
 */

data class DisabilityDto (
    @SerializedName("idDiscapacidad") val id: String,
    @SerializedName("nombre") val name: String,
    @SerializedName("caracteristicas") val characteristics: String,
)