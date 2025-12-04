package com.app.arcabyolimpo.domain.usecase.user

import com.app.arcabyolimpo.data.remote.dto.user.UserDto
import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.repository.user.UsersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Use case responsible for fetching a list of users from the repository layer.
 *
 * This class communicates with [UsersRepository] to retrieve all user data from the remote source.
 * It emits a [Flow] of [Result] objects to represent different states of the operation — loading, success,
 * or error. On success, it provides a list of [UserDto] objects.
 *
 * @property repository Repository interface that handles user-related data operations.
 *
 * @see UsersRepository
 * @see UserDto
 * @see Result
 */

class GetUsersUseCase
    @Inject
    constructor(
        private val repository: UsersRepository,
    ) {
        /**
         * Executes the request to fetch all users.
         *
         * @return A [Flow] emitting the current state of the operation wrapped in [Result].
         */
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
