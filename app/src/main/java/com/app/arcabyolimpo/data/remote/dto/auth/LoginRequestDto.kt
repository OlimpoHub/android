package com.app.arcabyolimpo.data.remote.dto.auth

/**
 * DTO used when sending a login request.
 *
 * @param username The user’s login username.
 * @param password The corresponding password for authentication.
 */
data class LoginRequestDto(
    val username: String,
    val password: String,
)
