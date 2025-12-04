package com.app.arcabyolimpo.domain.repository.password

import com.app.arcabyolimpo.domain.model.password.UpdatePassword
import com.app.arcabyolimpo.domain.model.password.VerifyToken

/**
 * Repository interface defining the contract for user password recovery and update operations.
 *
 * This layer abstracts the communication with the backend for processes such as requesting
 * password recovery, verifying a reset token, and updating a user's password. Implementations
 * of this interface handle all necessary network or data operations.
 *
 * @see com.app.arcabyolimpo.domain.usecase.password PostPasswordRecoveryUseCase
 * @see com.app.arcabyolimpo.domain.usecase.password GetVerifyTokenUseCase
 * @see com.app.arcabyolimpo.domain.usecase.password PostUpdatePasswordUseCase
 */

interface PasswordUserRepository {
    /**
     * Sends a password recovery request for the specified email.
     *
     * @param email The email associated with the user requesting password recovery.
     * @return A success message returned by the backend (e.g., confirmation that an email was sent).
     */
    suspend fun postRecoverPassword(email: String): String

    /**
     * Validates a password reset token by sending it to the backend.
     *
     * @param token The unique reset token received by the user.
     * @return A [VerifyToken] model containing validation details (validity, user info, etc.).
     */
    suspend fun getVerifyToken(token: String): VerifyToken

    /**
     * Submits a request to update the user's password.
     *
     * @param email The email of the user whose password is being updated.
     * @param password The new password to set for the user.
     * @return An [UpdatePassword] model containing the backend response message.
     */
    suspend fun postUpdatePassword(
        email: String,
        password: String,
    ): UpdatePassword
}
