package com.app.arcabyolimpo.domain.usecase.user.delete

import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.repository.user.UsersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DeleteUserUseCase @Inject constructor(
    private val repository: UsersRepository,
) {
    operator fun invoke(id: String): Flow<Result<Boolean>> =
        flow {
            try {
                emit(Result.Loading)
                val isDeleted = repository.deleteUser(id)
                emit(Result.Success(isDeleted))
            } catch (e: Exception) {
                emit(Result.Error(e))
            }
        }
}
