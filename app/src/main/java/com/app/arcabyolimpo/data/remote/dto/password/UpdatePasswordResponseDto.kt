package com.app.arcabyolimpo.data.remote.dto.password

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object (DTO) representing the API response after attempting
 * to update a user's password.
 *
 * @property status Indicates whether the password update was successful (true) or not (false).
 * @property message A descriptive message returned by the API, often used to inform
 * the user about the result of the operation.
 */

data class UpdatePasswordResponseDto(
    @SerializedName("status") val status: Boolean,
    @SerializedName("message") val message: String,
)
