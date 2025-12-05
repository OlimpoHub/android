package com.app.arcabyolimpo.domain.repository.user

import com.app.arcabyolimpo.data.remote.dto.user.UserDto
import com.app.arcabyolimpo.domain.common.Result

/**
 * Repository interface defining the contract for user data operations.
 *
 * This interface establishes the domain layer's requirements for user management,
 * abstracting away the implementation details of data sources (API, database, cache).
 * It provides two patterns for error handling: traditional exception-based methods
 * and Result-wrapped methods for more functional error management.
 *
 * Implementations of this interface should handle all data source communication,
 * error handling, and data transformation between data and domain layers.
 */
interface UsersRepository {
    /**
     * Retrieves all users from the data source.
     *
     * This method fetches the complete list of users available in the system,
     * returning them as domain models ready for business logic processing.
     *
     * @return A [List] of [UserDto] domain models representing all users.
     * @throws Exception if the data retrieval fails due to network, database, or other errors.
     */
    suspend fun getUsers(): List<UserDto>

    /**
     * Retrieves a specific user by their unique identifier.
     *
     * This method queries the data source for a user matching the provided ID.
     * If no user is found, an exception is thrown to indicate the absence of
     * the requested resource.
     *
     * @param id The unique identifier of the user to retrieve.
     * @return A [UserDto] domain model representing the requested user.
     * @throws Exception if the user is not found or if the data retrieval fails.
     */
    suspend fun getUserById(id: String): UserDto

    /**
     * Registers a new user in the system.
     *
     * This method creates a new user record with the provided information,
     * persisting it to the data source. Upon successful registration, the
     * same user object is returned to confirm the operation.
     *
     * @param user The [UserDto] domain model containing the user information to register.
     * @return The same [UserDto] instance passed as input, confirming successful registration.
     * @throws Exception if the registration fails due to validation errors, conflicts, or data source issues.
     */
    suspend fun registerUser(user: UserDto): UserDto

    /**
     * Deletes a user from the system by their unique identifier.
     *
     * This method removes the user record associated with the provided ID
     * from the data source. The boolean return value indicates whether
     * the deletion was successfully completed.
     *
     * @param id The unique identifier of the user to delete.
     * @return `true` if the user was successfully deleted, `false` otherwise.
     * @throws Exception if the deletion operation fails due to data source errors.
     */
    suspend fun deleteUser(id: String): Boolean

    /**
     * Retrieves all users wrapped in a [Result] type for functional error handling.
     *
     * This method provides an alternative to [getUsers] by encapsulating the operation
     * result in a [Result] wrapper, which explicitly represents both success and failure
     * states. This approach enables calling code to handle errors without exception
     * catching, promoting a more functional programming style.
     *
     * @return A [Result.Success] containing a [List] of [UserDto] if the request succeeds,
     *         or a [Result.Error] containing the exception if the request fails.
     */
    suspend fun getUsersDomain(): Result<List<UserDto>>

    /**
     * Updates an existing user's information in the system.
     *
     * This method modifies the user record associated with the provided user's ID,
     * applying the new information to the data source. The original user object
     * is returned upon successful update to confirm the operation.
     *
     * @param user The [UserDto] domain model containing the updated user information.
     * @return The same [UserDto] instance passed as input, confirming successful update.
     * @throws Exception if the update fails due to validation errors, user not found, or data source issues.
     */
    suspend fun updateUser(user: UserDto): UserDto
}