package com.app.arcabyolimpo.data.remote.dto.user

import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("idUsuario") val idUsuario: String,

    @SerializedName("idRol") val idRol: String,

    @SerializedName("nombre") val nombre: String,

    @SerializedName("apellidoPaterno") val apellidoPaterno: String,

    @SerializedName("apellidoMaterno") val apellidoMaterno: String,

    @SerializedName("fechaNacimiento") val fechaNacimiento: String,

    @SerializedName("carrera") val carrera: String,

    @SerializedName("correoElectronico") val correoElectronico: String,

    @SerializedName("contrasena") val contrasena: String? = null,

    @SerializedName("telefono") val telefono: String,

    @SerializedName("estatus") val estatus: Int,

    @SerializedName("reglamentoInterno") val reglamentoInterno: Int? = null,

    @SerializedName("copiaINE") val copiaINE: Int? = null,

    @SerializedName("avisoConfidencialidad") val avisoConfidencialidad: Int? = null,

    @SerializedName("foto") val foto: String? = null
)