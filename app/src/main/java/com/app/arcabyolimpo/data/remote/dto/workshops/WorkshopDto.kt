package com.app.arcabyolimpo.data.remote.dto.workshops

import com.google.gson.annotations.SerializedName
import java.sql.Date
import java.sql.Time
data class WorkshopDto(
    @SerializedName("idTaller") val id: String,
    @SerializedName("idCapacitacion") val idTraining: String,
    @SerializedName("idUser") val idUser: String,
    @SerializedName("URL") val image: String,
    @SerializedName("nombreTaller") val name: String,
    @SerializedName("horaEntrada") val startHour: Time,
    @SerializedName("horaSalida") val finishHour: Time,
    @SerializedName("estatus") val status: Int,
    @SerializedName("HorarioTaller") val schedule: String,
    @SerializedName("Fecha") val date: Date
)