package com.app.arcabyolimpo.domain.usecase.password

import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.model.password.UpdatePassword
import com.app.arcabyolimpo.domain.repository.password.PasswordUserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

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
