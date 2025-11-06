package com.app.arcabyolimpo.domain.repository.auth

import com.app.arcabyolimpo.domain.model.auth.User

/** Defines the contract for user authentication and session management. */
interface UserRepository {
    /**
     * Attempts to authenticate a user with the provided credentials.
     *
     * @param username The user’s login name.
     * @param password The user’s password.
     * @return The authenticated [User] if successful.
     * @throws Exception If authentication fails.
     */
    suspend fun login(
        username: String,
        password: String,
    ): User

    /** Logs out the current user by clearing stored session data. */
    suspend fun logout()
}
