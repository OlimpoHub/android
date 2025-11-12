package com.app.arcabyolimpo.domain.usecase.password

import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.model.password.UpdatePassword
import com.app.arcabyolimpo.domain.repository.password.PasswordUserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Use case responsible for registering or updating a user's password.
 *
 * This class interacts with [PasswordUserRepository] to send the user's email and new password
 * to the server for account activation or password reset. It returns a [Flow] that emits a [Result]
 * representing the current state of the operation: loading, success (with the updated password response),
 * or error (with an exception).
 *
 * @property repository Repository interface that manages password-related API interactions.
 *
 * @see PasswordUserRepository
 * @see Result
 * @see UpdatePassword
 */

class PostPasswordRegistrationUseCase
    @Inject
    constructor(
        private val repository: PasswordUserRepository,
    ) {
        operator fun invoke(
            email: String,
            password: String,
        ): Flow<Result<UpdatePassword>> =
            flow {
                try {
                    emit(Result.Loading)
                    val response = repository.postUpdatePassword(email, password)
                    emit(Result.Success(response))
                } catch (e: Exception) {
                    emit(Result.Error(e))
                }
            }
    }
