package com.app.arcabyolimpo.domain.usecase.password

import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.model.password.VerifyToken
import com.app.arcabyolimpo.domain.repository.password.PasswordUserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Use case responsible for verifying a password recovery token.
 *
 * This class communicates with the [PasswordUserRepository] to validate the provided token
 * and emits a [Result] flow to represent loading, success, or error states.
 *
 * @property repository Repository interface for password-related operations.
 *
 * @see PasswordUserRepository
 * @see Result
 */

class GetVerifyTokenUseCase
    @Inject
    constructor(
        private val repository: PasswordUserRepository,
    ) {
        operator fun invoke(token: String): Flow<Result<VerifyToken>> =
            flow {
                try {
                    emit(Result.Loading)
                    val response = repository.getVerifyToken(token)
                    emit(Result.Success(response))
                } catch (e: Exception) {
                    emit(Result.Error(e))
                }
            }
    }
