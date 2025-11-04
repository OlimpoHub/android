package com.app.arcabyolimpo.data.repository.auth

import com.app.arcabyolimpo.data.local.auth.UserPreferences
import com.app.arcabyolimpo.data.mapper.auth.toDomain
import com.app.arcabyolimpo.data.remote.api.ArcaApi
import com.app.arcabyolimpo.data.remote.dto.auth.LoginRequestDto
import com.app.arcabyolimpo.domain.common.AppError
import com.app.arcabyolimpo.domain.model.auth.User
import com.app.arcabyolimpo.domain.repository.auth.UserRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * Implementation of [UserRepository] that handles user authentication logic,
 * including login, token persistence, and logout.
 */
class UserRepositoryImpl
    @Inject
    constructor(
        private val api: ArcaApi,
        private val authPreferences: UserPreferences,
    ) : UserRepository {
        /**
         * Attempts to log in the user with the given [username] and [password].
         * Persists user, access and refresh tokens locally for session management.

         * @param username The user's login username.
         * @param password The user's password.
         * @return The authenticated [User] domain model.
         * @throws AppError If the login fails for any reason.
         */
        override suspend fun login(
            username: String,
            password: String,
        ): User {
            try {
                val response = api.login(LoginRequestDto(username, password))

                authPreferences.saveTokens(
                    accessToken = response.accessToken,
                    refreshToken = response.refreshToken,
                )

                authPreferences.saveUser(
                    id = response.user.id,
                    username = response.user.username,
                    role = response.user.role,
                )

                return response.user.toDomain()
            } catch (e: HttpException) {
                throw when (e.code()) {
                    401 -> AppError.Unauthorized()
                    in 500..599 -> AppError.ServerError()
                    else -> AppError.Unknown(e.message ?: "Error inesperado")
                }
            } catch (e: IOException) {
                throw AppError.NetworkError()
            } catch (e: Exception) {
                throw AppError.Unknown(e.message ?: "Error desconocido")
            }
        }

        /**
         * Logs the user out by clearing all stored authentication data
         * from [UserPreferences], effectively ending the session.
         */
        override suspend fun logout() {
            authPreferences.clearAll()
        }
    }
