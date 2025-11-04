package com.app.arcabyolimpo.data.remote.dto.auth

/**
 * DTO used to request a new access token from the server.
 *
 * @param refreshToken The current refresh token associated with the session.
 */
data class RefreshRequestDto(
    val refreshToken: String,
)
