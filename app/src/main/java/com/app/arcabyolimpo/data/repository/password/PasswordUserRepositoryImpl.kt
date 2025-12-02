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
    protected fun parseError(response: Response<*>): String =
        try {
            val errorBody = response.errorBody()?.string()
            JSONObject(errorBody ?: "{}").optString("message", "Unknown error")
        } catch (e: Exception) {
            "Unknown error"
        }

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
        override suspend fun postRecoverPassword(email: String): String {
            val response = api.recoverPassword(RecoverPasswordDto(email))
            val body = handleResponse(response)
            return body.message
        }

        override suspend fun getVerifyToken(token: String): VerifyToken {
            val response = api.verifyToken(token)
            val body = handleResponse(response)
            return body.toDomain()
        }

        override suspend fun postUpdatePassword(
            email: String,
            password: String,
        ): UpdatePassword {
            val response = api.updatePassword(UpdatePasswordDto(email, password))
            val body = handleResponse(response)
            return body.toDomain()
        }
    }
