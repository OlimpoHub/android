package com.app.arcabyolimpo.data.remote.dto.password

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object (DTO) representing the response from the password recovery endpoint.
 *
 * This class is used to capture the server's response message after a password recovery
 * request has been made.
 *
 * @property message A message from the API indicating the result of the password recovery request.
 */

data class RecoverPasswordResponseDto(
    @SerializedName("message") val message: String,
)
