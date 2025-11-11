package com.app.arcabyolimpo.domain.usecase.password

import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.model.password.VerifyToken
import com.app.arcabyolimpo.domain.repository.password.PasswordUserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

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
