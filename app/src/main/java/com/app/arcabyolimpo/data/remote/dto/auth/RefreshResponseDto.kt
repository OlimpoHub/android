package com.app.arcabyolimpo.data.remote.dto.auth

/**
 * DTO returned by the server when a token refresh operation is successful.
 *
 * @param accessToken The new valid access token.
 */
data class RefreshResponseDto(
    val accessToken: String,
)
