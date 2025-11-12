package com.app.arcabyolimpo.domain.usecase.password

import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.repository.password.PasswordUserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Use case responsible for initiating the password recovery process.
 *
 * This class communicates with [PasswordUserRepository] to request a password recovery
 * email for the given user email address. It emits a [Result] flow that represents
 * the different states of the operation: loading, success (with a confirmation message),
 * or error (with an exception).
 *
 * @property repository Repository interface that handles password-related API operations.
 *
 * @see PasswordUserRepository
 * @see Result
 */

class PostPasswordRecoveryUseCase
    @Inject
    constructor(
        private val repository: PasswordUserRepository,
    ) {
        operator fun invoke(email: String): Flow<Result<String>> =
            flow {
                try {
                    emit(Result.Loading)
                    val message = repository.postRecoverPassword(email)
                    emit(Result.Success(message))
                } catch (e: Exception) {
                    emit(Result.Error(e))
                }
            }
    }
