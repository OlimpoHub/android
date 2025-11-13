package com.app.arcabyolimpo.domain.usecase.user.register

import com.app.arcabyolimpo.data.remote.dto.user.UserDto
import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.repository.user.UsersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val repository: UsersRepository
) {
    operator fun invoke(collab: UserDto): Flow<Result<UserDto>> =
        flow {
            try {
                emit(Result.Loading)
                val registeredCollab = repository.registerUser(collab)
                emit(Result.Success(registeredCollab))
            } catch (e: Exception) {
                emit(Result.Error(e))
            }
        }
}