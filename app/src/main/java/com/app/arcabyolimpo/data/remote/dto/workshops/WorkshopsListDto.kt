package com.app.arcabyolimpo.data.remote.dto.workshops

import com.google.gson.annotations.SerializedName

/**
 * DTO representing detailed information the workshop data before being overwritten.
 *
 * @param id The unique identifier of the workshop.
 * @param image The url of the image to be upload.
 * @param name The name of the new workshop.
 * @param idTraining The unique identifier of the training.
 * @param idUser The unique identifier of the user.
 */

data class WorkshopsListDto (
    @SerializedName("idTaller") val id: String,
    @SerializedName("URL") val image: String,
    @SerializedName("nombreTaller") val name: String,
    @SerializedName("idCapacitacion") val idTraining: String,
    @SerializedName("idUser") val idUser: String
)