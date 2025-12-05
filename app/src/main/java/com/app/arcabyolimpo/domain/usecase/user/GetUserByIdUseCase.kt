package com.app.arcabyolimpo.domain.usecase.user

import com.app.arcabyolimpo.data.remote.dto.user.UserDto
import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.repository.user.UsersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Use case for retrieving a specific user by their unique identifier.
 *
 * This use case encapsulates the business logic for fetching a single user,
 * orchestrating the repository call and wrapping the operation in a reactive
 * [Flow] that emits [Result] states. This pattern allows UI layers to observe
 * loading, success, and error states throughout the operation lifecycle.
 *
 * The use case follows the single responsibility principle by focusing solely
 * on the "get user by ID" operation, making it easily testable and reusable
 * across different presentation layers.
 *
 * @property repository The [UsersRepository] instance used to fetch user data.
 * @constructor Creates a use case instance with the provided repository via dependency injection.
 */
class GetUserByIdUseCase @Inject constructor(
    private val repository: UsersRepository,
) {
    /**
     * Executes the use case to retrieve a user by their ID, emitting result states as a [Flow].
     *
     * This operator function enables the use case to be invoked like a regular function.
     * It creates a cold Flow that emits three possible states during execution:
     * 1. [Result.Loading] - Emitted immediately to indicate the operation has started
     * 2. [Result.Success] - Emitted with the [UserDto] if the user is found successfully
     * 3. [Result.Error] - Emitted with the exception if the operation fails
     *
     * The Flow approach allows observers to react to each state change, enabling
     * responsive UI updates such as showing loading indicators, displaying user data,
     * or presenting error messages.
     *
     * @param id The unique identifier of the user to retrieve.
     * @return A [Flow] that emits [Result] states wrapping the [UserDto] or error information.
     */
    operator fun invoke(id: String): Flow<Result<UserDto>> =
        flow {
            try {
                emit(Result.Loading)
                val collab = repository.getUserById(id)
                emit(Result.Success(collab))
            } catch (e: Exception) {
                emit(Result.Error(e))
            }
        }
}