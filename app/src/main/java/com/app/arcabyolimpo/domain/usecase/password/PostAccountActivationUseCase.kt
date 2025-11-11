package com.app.arcabyolimpo.domain.usecase.password

import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.repository.password.PasswordUserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PostAccountActivationUseCase
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
