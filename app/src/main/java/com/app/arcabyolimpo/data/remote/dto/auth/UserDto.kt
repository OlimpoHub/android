package com.app.arcabyolimpo.data.remote.dto.auth

/**
 * DTO representing a user as returned by the API.
 *
 * @param id Unique identifier of the user.
 * @param username The user’s username.
 * @param role The user’s role (e.g., "coord", "colab").
 */
data class UserDto(
    val id: String,
    val username: String,
    val role: String,
)
