package com.app.arcabyolimpo.domain.usecase.password

import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.repository.password.PasswordUserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Use case responsible for handling the account activation process by requesting
 * a password recovery email to be sent to the specified address.
 *
 * This use case interacts with [PasswordUserRepository] to trigger the backend API call
 * that sends an activation or password recovery link to the user's email.
 * It emits a [Result] flow representing loading, success, or error states.
 *
 * @property repository Repository interface for password-related network operations.
 *
 * @see PasswordUserRepository
 * @see Result
 */

class PostAccountActivationUseCase
    @Inject
    constructor(
        private val repository: PasswordUserRepository,
    ) {
        /**
         * Executes the request to send an account activation or password recovery email.
         *
         * @param email The user's email address to which the activation/recovery link
         *              should be sent.
         *
         * @return A [Flow] emitting the backend response message wrapped inside [Result].
         */
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
