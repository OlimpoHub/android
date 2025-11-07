package com.app.arcabyolimpo.data.remote.dto.auth

/**
 * DTO that represents the response returned after a successful login request.
 *
 * @param user The authenticated user’s information.
 * @param accessToken The short-lived token used for authorized API requests.
 * @param refreshToken The long-lived token used to renew the access token.
 */
data class LoginResponseDto(
    val user: UserDto,
    val accessToken: String,
    val refreshToken: String,
)
