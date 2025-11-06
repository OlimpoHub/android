package com.app.arcabyolimpo.domain.usecase.auth

import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.model.auth.User
import com.app.arcabyolimpo.domain.repository.auth.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/** Use case that encapsulates the login business logic. */
class LoginUseCase
    @Inject
    constructor(
        private val repository: UserRepository,
    ) {
        /**
         * Executes the login flow with the given credentials.
         *
         * Emits:
         * - [Result.Loading] while authentication is in progress.
         * - [Result.Success] with the authenticated [User] on success.
         * - [Result.Error] if an exception occurs.
         *
         * @param username The user’s login username.
         * @param password The user’s password.
         * @return A [Flow] representing the login process state.
         */
        operator fun invoke(
            username: String,
            password: String,
        ): Flow<Result<User>> =
            flow {
                emit(Result.Loading)
                try {
                    val user = repository.login(username, password)
                    emit(Result.Success(user))
                } catch (e: Exception) {
                    emit(Result.Error(e))
                }
            }
    }
