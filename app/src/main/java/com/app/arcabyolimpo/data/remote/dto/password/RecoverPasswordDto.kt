package com.app.arcabyolimpo.data.remote.dto.password

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object (DTO) used to send a password recovery request to the API.
 *
 * This class represents the payload for initiating the password recovery process,
 * containing only the user's email address.
 *
 * @property email The email address of the user requesting password recovery.
 */

data class RecoverPasswordDto(
    @SerializedName("email") val email: String,
)
