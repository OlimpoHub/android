package com.app.arcabyolimpo.data.remote.dto.user.updateuser

import com.google.gson.annotations.SerializedName

/**
 * Data transfer object representing the API response for user update requests.
 *
 * This DTO encapsulates the backend's response after attempting to update an existing
 * user's information, providing both a status flag and a descriptive message. The response
 * structure allows the application to determine whether the update operation succeeded
 * and to present appropriate feedback messages to the user based on the backend's
 * validation or processing results.
 *
 * The class uses Gson annotations to map JSON response fields to Kotlin properties,
 * ensuring proper deserialization of the API response regardless of field naming conventions.
 *
 * @property status A boolean flag indicating whether the update operation succeeded.
 *                  `true` indicates successful update, `false` indicates failure due to
 *                  validation errors, permissions issues, or other backend constraints.
 * @property message A descriptive message from the backend providing details about the
 *                   update result, which may include success confirmation or specific
 *                   error information to help users understand what went wrong and how
 *                   to correct their input.
 */

data class UpdateResponseDto(
    @SerializedName("status")
    val status: Boolean,

    @SerializedName("message")
    val message: String
)


