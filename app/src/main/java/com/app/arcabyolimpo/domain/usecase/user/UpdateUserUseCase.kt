package com.app.arcabyolimpo.domain.usecase.user

import com.app.arcabyolimpo.data.remote.dto.user.UserDto
import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.repository.user.UsersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdateUserUseCase
@Inject
constructor(
    private val repository: UsersRepository,
) {
    operator fun invoke(user: UserDto): Flow<Result<UserDto>> =
        flow {
            try {
                emit(Result.Loading)
                val updatedUser = repository.updateUser(user)
                emit(Result.Success(updatedUser))
            } catch (e: Exception) {
                emit(Result.Error(e))
            }
        }
}
