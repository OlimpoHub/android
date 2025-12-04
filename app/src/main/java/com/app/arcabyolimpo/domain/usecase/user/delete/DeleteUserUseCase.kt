package com.app.arcabyolimpo.domain.usecase.user.delete

import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.repository.user.UsersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
/**
 * Use case for deleting a user from the system by their unique identifier.
 *
 * This use case encapsulates the business logic for user deletion,
 * orchestrating the repository call and wrapping the operation in a reactive
 * [Flow] that emits [Result] states. This pattern enables UI layers to observe
 * and react to loading, success, and error states throughout the deletion
 * process, allowing for appropriate user feedback and confirmation workflows.
 *
 * The use case follows the single responsibility principle by focusing solely
 * on the user deletion operation, making it easily testable, reusable,
 * and maintainable across different presentation layers. This is particularly
 * important for sensitive operations like deletion, where clear state management
 * and error handling are critical.
 *
 * @property repository The [UsersRepository] instance used to perform the deletion operation.
 * @constructor Creates a use case instance with the provided repository via dependency injection.
 */
class DeleteUserUseCase @Inject constructor(
    private val repository: UsersRepository,
) {
    /**
     * Executes the use case to delete a user by their ID, emitting result states as a [Flow].
     *
     * This operator function enables the use case to be invoked like a regular function.
     * It creates a cold Flow that emits three possible states during the deletion process:
     * 1. [Result.Loading] - Emitted immediately to indicate the deletion has started
     * 2. [Result.Success] - Emitted with a boolean value (typically `true`) if the user
     *    was successfully deleted from the system
     * 3. [Result.Error] - Emitted with the exception if deletion fails due to permissions,
     *    network issues, or the user not being found
     *
     * The Flow approach allows observers to react to each state change, enabling
     * responsive UI updates such as showing loading indicators during the operation,
     * displaying confirmation messages upon success, or presenting appropriate error
     * messages that help users understand why the deletion couldn't be completed.
     *
     * @param id The unique identifier of the user to be deleted.
     * @return A [Flow] that emits [Result] states wrapping a boolean success indicator or error information.
     */
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
