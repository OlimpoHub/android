package com.app.arcabyolimpo.data.remote.dto.user.registeruser

import com.google.gson.annotations.SerializedName
/**
 * Data transfer object representing the API response for user registration requests.
 *
 * This DTO encapsulates the backend's response after attempting to register a new user,
 * providing both a status flag and a descriptive message. The response structure allows
 * the application to determine whether registration succeeded and to present appropriate
 * feedback messages to the user based on the backend's validation or processing results.
 *
 * The class uses Gson annotations to map JSON response fields to Kotlin properties,
 * ensuring proper deserialization of the API response regardless of field naming conventions.
 *
 * @property status A boolean flag indicating whether the registration operation succeeded.
 *                  `true` indicates successful registration, `false` indicates failure.
 * @property message A descriptive message from the backend providing details about the
 *                   registration result, which may include success confirmation or specific
 *                   error information to help users understand validation failures or other issues.
 */
data class RegisterResponseDto(
    @SerializedName("status")
    val status: Boolean,

    @SerializedName("message")
    val message: String
)


