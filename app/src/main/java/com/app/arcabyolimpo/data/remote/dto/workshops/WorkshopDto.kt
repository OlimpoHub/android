package com.app.arcabyolimpo.data.remote.dto.workshops

import com.google.gson.annotations.SerializedName
data class WorkshopDto(
    @SerializedName("idTaller") val id: String,
    @SerializedName("idCapacitacion") val idTraining: String,
    @SerializedName("idUsuario") val idUser: String,
    @SerializedName("url") val image: String,
    @SerializedName("nombreTaller") val name: String,
    @SerializedName("horaEntrada") val startHour: String,
    @SerializedName("horaSalida") val finishHour: String,
    @SerializedName("estatus") val status: Int,
    @SerializedName("horarioTaller") val schedule: String,
    @SerializedName("fecha") val date: String
)