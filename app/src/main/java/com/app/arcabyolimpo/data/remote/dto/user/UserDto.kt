package com.app.arcabyolimpo.data.remote.dto.user

import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("idUsuario") val idUsuario: String? = null,

    @SerializedName("idRol") val idRol: String? = null,
    @SerializedName("nombre") val nombre: String? = null,
    @SerializedName("apellidoPaterno") val apellidoPaterno: String? = null,
    @SerializedName("apellidoMaterno") val apellidoMaterno: String? = null,
    @SerializedName("fechaNacimiento") val fechaNacimiento: String? = null,
    @SerializedName("carrera") val carrera: String? = null,
    @SerializedName("correoElectronico") val correoElectronico: String? = null,
    @SerializedName("contrasena") val contrasena: String? = null,
    @SerializedName("telefono") val telefono: String? = null,
    @SerializedName("estatus") val estatus: Int? = null,
    @SerializedName("reglamentoInterno") val reglamentoInterno: Int? = null,
    @SerializedName("copiaINE") val copiaINE: Int? = null,
    @SerializedName("avisoConfidencialidad") val avisoConfidencialidad: Int? = null,
    @SerializedName("foto") val foto: String? = null,
)
