package com.app.arcabyolimpo.domain.usecase.user

import com.app.arcabyolimpo.data.remote.dto.user.UserDto
import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.repository.user.UsersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Use case for updating an existing user's information in the system.
 *
 * This use case encapsulates the business logic for user updates,
 * orchestrating the repository call and wrapping the operation in a reactive
 * [Flow] that emits [Result] states. This pattern enables UI layers to observe
 * and react to loading, success, and error states throughout the update
 * process, providing a responsive user experience during profile modifications
 * or administrative user management operations.
 *
 * The use case follows the single responsibility principle by focusing solely
 * on the user update operation, making it easily testable, reusable,
 * and maintainable across different presentation layers such as profile editing
 * screens or administrative dashboards.
 *
 * @property repository The [UsersRepository] instance used to persist user updates.
 * @constructor Creates a use case instance with the provided repository via dependency injection.
 */
class UpdateUserUseCase
@Inject
constructor(
    private val repository: UsersRepository,
) {
    /**
     * Executes the use case to update a user's information, emitting result states as a [Flow].
     *
     * This operator function enables the use case to be invoked like a regular function.
     * It creates a cold Flow that emits three possible states during the update process:
     * 1. [Result.Loading] - Emitted immediately to indicate the update operation has started
     * 2. [Result.Success] - Emitted with the updated [UserDto] if the operation succeeds,
     *    allowing the UI to reflect the modified user information
     * 3. [Result.Error] - Emitted with the exception if the update fails due to validation
     *    errors, permissions issues, network problems, or the user not being found
     *
     * The Flow approach allows observers to react to each state change, enabling
     * responsive UI updates such as showing loading indicators during the operation,
     * refreshing the display with updated user data upon success, or presenting
     * appropriate error messages that guide users in correcting validation issues
     * or understanding what went wrong.
     *
     * @param user The [UserDto] domain model containing the updated user information to persist.
     * @return A [Flow] that emits [Result] states wrapping the updated [UserDto] or error information.
     */
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
