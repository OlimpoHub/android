package com.app.arcabyolimpo.data.remote.dto.workshops


import com.google.gson.annotations.SerializedName

/**
 * DTO representing detailed information of a workshop received from the API.
 *
 * @param id The unique identifier of the workshop.
 * @param idUser The unique identifier of the user.
 * @param image The url of the image to be upload.
 * @param name The name of the new workshop.
 * @param startHour The hour for the workshop to be started.
 * @param finishHour The hour for the workshop to be ended.
 * @param status The status of the workshop, active = 1, inactive = 0.
 * @param description The description of how is the workshop.
 * @param date The day when the workshop is going to take place.
 * @param videoTraining The video of the training depending on the workshop
 */
data class WorkshopDto(
    @SerializedName("idTaller") val id: String?,
    @SerializedName("idUsuario") val idUser: String?,
    @SerializedName("URL") val image: String?,
    @SerializedName("nombreTaller") val name: String?,
    @SerializedName("horaEntrada") val startHour: String?,
    @SerializedName("horaSalida") val finishHour: String?,
    @SerializedName("estatus") val status: Int,
    @SerializedName("descripcion",["Descripcion"]) val description: String?,
    @SerializedName("Fecha") val date: String?
)