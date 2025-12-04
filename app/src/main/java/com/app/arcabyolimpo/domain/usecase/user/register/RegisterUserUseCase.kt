package com.app.arcabyolimpo.domain.usecase.user.register

import com.app.arcabyolimpo.data.remote.dto.user.UserDto
import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.repository.user.UsersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Use case for registering a new user in the system.
 *
 * This use case encapsulates the business logic for user registration,
 * orchestrating the repository call and wrapping the operation in a reactive
 * [Flow] that emits [Result] states. This pattern enables UI layers to observe
 * and react to loading, success, and error states throughout the registration
 * process, providing a responsive user experience during account creation.
 *
 * The use case follows the single responsibility principle by focusing solely
 * on the user registration operation, making it easily testable, reusable,
 * and maintainable across different presentation layers.
 *
 * @property repository The [UsersRepository] instance used to persist user registration data.
 * @constructor Creates a use case instance with the provided repository via dependency injection.
 */
class RegisterUserUseCase @Inject constructor(
    private val repository: UsersRepository
) {
    /**
     * Executes the use case to register a new user, emitting result states as a [Flow].
     *
     * This operator function enables the use case to be invoked like a regular function.
     * It creates a cold Flow that emits three possible states during the registration process:
     * 1. [Result.Loading] - Emitted immediately to indicate registration has started
     * 2. [Result.Success] - Emitted with the registered [UserDto] if the operation succeeds
     * 3. [Result.Error] - Emitted with the exception if registration fails due to validation,
     *    network issues, or backend errors
     *
     * The Flow approach allows observers to react to each state change, enabling
     * responsive UI updates such as showing loading spinners during registration,
     * navigating to success screens, or displaying appropriate error messages to guide
     * the user through correcting registration issues.
     *
     * @param collab The [UserDto] domain model containing the new user's information to register.
     * @return A [Flow] that emits [Result] states wrapping the registered [UserDto] or error information.
     */

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