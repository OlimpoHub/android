package com.app.arcabyolimpo.data.remote.dto.user

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object (DTO) representing a user retrieved from the backend.
 *
 * This class contains all user-related fields as returned by the API, including
 * identification details, personal information, contact data, and document status.
 * All fields are nullable to safely handle incomplete or optional backend responses.
 *
 * @property idUsuario Unique identifier of the user.
 * @property idRol Identifier of the user's role within the system.
 * @property nombre User's first name.
 * @property apellidoPaterno User's paternal last name.
 * @property apellidoMaterno User's maternal last name.
 * @property fechaNacimiento User's birth date in string format (typically ISO-8601).
 * @property carrera Academic or professional career associated with the user.
 * @property correoElectronico User's registered email address.
 * @property contrasena User's hashed password (if returned by the API — often null or omitted).
 * @property telefono User's phone number.
 * @property estatus Numerical flag representing the user's status or activation state.
 * @property reglamentoInterno Indicator whether the user has provided the internal regulation document.
 * @property copiaINE Indicator whether the user has submitted a copy of their INE ID.
 * @property avisoConfidencialidad Indicator whether the confidentiality notice has been accepted or submitted.
 * @property foto URL or encoded string representing the user's profile photo.
 */

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
