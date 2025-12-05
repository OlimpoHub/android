package com.app.arcabyolimpo.data.repository.password

import com.app.arcabyolimpo.data.mapper.password.toDomain
import com.app.arcabyolimpo.data.remote.api.ArcaApi
import com.app.arcabyolimpo.data.remote.dto.password.RecoverPasswordDto
import com.app.arcabyolimpo.data.remote.dto.password.UpdatePasswordDto
import com.app.arcabyolimpo.domain.model.password.UpdatePassword
import com.app.arcabyolimpo.domain.model.password.VerifyToken
import com.app.arcabyolimpo.domain.repository.password.PasswordUserRepository
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.json.JSONObject
import retrofit2.Response

/**
 * Base repository providing common utilities for handling Retrofit responses
 * across multiple repositories.
 *
 * This class centralizes error parsing and success/error validation, allowing
 * child repositories to avoid boilerplate response handling.
 *
 * Functions:
 * - [parseError] extracts a user-friendly error message from the backend.
 * - [handleResponse] validates the HTTP response and returns its body or throws an exception.
 */

abstract class BaseRepository {
    /**
     * Extracts a meaningful message from an error response returned by Retrofit.
     *
     * @param response Retrofit HTTP response containing an error body.
     * @return A user-friendly error message, or a fallback message if parsing fails.
     */
    protected fun parseError(response: Response<*>): String =
        try {
            val errorBody = response.errorBody()?.string()
            JSONObject(errorBody ?: "{}").optString("message", "Unknown error")
        } catch (e: Exception) {
            "Unknown error"
        }

    /**
     * Validates a Retrofit response and returns its body.
     *
     * @param response Response returned by a Retrofit API call.
     * @return The response body if the call was successful.
     * @throws Exception containing the backend error message if the response is not successful.
     */
    protected fun <T> handleResponse(response: Response<T>): T {
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Empty response")
        } else {
            throw Exception(parseError(response))
        }
    }
}

/**
 * Repository implementation for handling user password-related operations,
 * such as password recovery, token verification, and password updates.
 *
 * This class communicates with the backend via [ArcaApi] and converts
 * API DTOs into domain models using mapper extensions.
 *
 * Responsibilities:
 * - Send password recovery requests.
 * - Validate password recovery tokens.
 * - Update user passwords.
 *
 * This repository extends [BaseRepository] to reuse common response handling logic.
 */

@Singleton
class PasswordUserRepositoryImpl
    @Inject
    constructor(
        private val api: ArcaApi,
    ) : BaseRepository(),
        PasswordUserRepository {
        /**
         * Sends a password recovery request for the specified email.
         *
         * @param email Email associated with the user account.
         * @return A backend-generated success message.
         * @throws Exception if the backend returns an error.
         */
        override suspend fun postRecoverPassword(email: String): String {
            val response = api.recoverPassword(RecoverPasswordDto(email))
            val body = handleResponse(response)
            return body.message
        }

        /**
         * Validates a password reset token by sending it to the backend.
         *
         * @param token Unique password reset token sent to the user's email.
         * @return A [VerifyToken] domain model indicating token validity.
         * @throws Exception if the token is invalid or the API call fails.
         */
        override suspend fun getVerifyToken(token: String): VerifyToken {
            val response = api.verifyToken(token)
            val body = handleResponse(response)
            return body.toDomain()
        }

        /**
         * Updates a user's password using their email and the new password.
         *
         * @param email Email of the user whose password will be updated.
         * @param password New password provided by the user.
         * @return An [UpdatePassword] domain model containing the API confirmation message.
         * @throws Exception if the update fails or the backend rejects the request.
         */
        override suspend fun postUpdatePassword(
            email: String,
            password: String,
        ): UpdatePassword {
            val response = api.updatePassword(UpdatePasswordDto(email, password))
            val body = handleResponse(response)
            return body.toDomain()
        }
    }
