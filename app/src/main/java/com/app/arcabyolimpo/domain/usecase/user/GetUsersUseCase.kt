package com.app.arcabyolimpo.domain.usecase.user

import com.app.arcabyolimpo.data.remote.dto.user.UserDto
import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.repository.user.UsersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetUsersUseCase
@Inject
constructor(
    private val repository: UsersRepository,
) {
    operator fun invoke(): Flow<Result<List<UserDto>>> =
        flow {
            try {
                emit(Result.Loading)
                val users = repository.getUsers()
                emit(Result.Success(users))
            } catch (e: Exception) {
                emit(Result.Error(e))
            }
        }
}