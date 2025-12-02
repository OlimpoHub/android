package com.app.arcabyolimpo.data.remote.dto.attendance

import com.google.gson.annotations.SerializedName

data class AttendanceDto(
    @SerializedName("idAsistencia") val idAsistencia: String,
    @SerializedName("idUsuario") val idUsuario: String,
    @SerializedName("idTaller") val idTaller: String,
    @SerializedName("nombreTaller") val nombreTaller: String?,
    @SerializedName("fechaInicio") val fechaInicio: String,
    @SerializedName("fechaFin") val fechaFin: String?
)



