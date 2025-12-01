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
    suspend fun postRecoverPassword(email: String): String

    suspend fun getVerifyToken(token: String): VerifyToken

    suspend fun postUpdatePassword(
        email: String,
        password: String,
    ): UpdatePassword
}
