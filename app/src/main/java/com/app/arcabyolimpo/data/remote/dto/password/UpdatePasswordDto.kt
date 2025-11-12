package com.app.arcabyolimpo.data.remote.dto.password

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object (DTO) used to send the email and new password
 * to the API when updating a user's password.
 *
 * @property email The user's email address associated with the account.
 * @property password The new password to be set for the user.
 */

data class UpdatePasswordDto(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
)
