package com.app.arcabyolimpo.data.repository.user

import com.app.arcabyolimpo.data.mapper.user.toDomain
import com.app.arcabyolimpo.data.mapper.user.toRegisterDto
import com.app.arcabyolimpo.data.mapper.user.toUpdateDto
import com.app.arcabyolimpo.data.remote.api.ArcaApi
import com.app.arcabyolimpo.data.remote.dto.user.UserDto
import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.repository.user.UsersRepository
import javax.inject.Inject

/**
 * Implementation of [UsersRepository] that provides user data operations
 * through the Arca API.
 *
 * This repository acts as a bridge between the data layer (API) and the domain layer,
 * transforming DTOs to domain models and handling API communication. It encapsulates
 * all user-related network operations and error handling logic.
 *
 * @property api The [ArcaApi] instance used to perform network requests.
 * @constructor Creates a repository instance with the provided API client via dependency injection.
 */

class UsersRepositoryImpl @Inject constructor(
    private val api: ArcaApi,
) : UsersRepository {

    /**
     * Retrieves all users from the API and maps them to domain models.
     *
     * This method fetches the complete list of users available in the system,
     * transforming each DTO received from the API into its corresponding domain
     * representation for use throughout the application.
     *
     * @return A [List] of [UserDto] domain models representing all users.
     * @throws Exception if the API request fails or network issues occur.
     */

    override suspend fun getUsers(): List<UserDto> {
        val response = api.getAllUsers()
        return response.map { it.toDomain() }
    }

    /**
    * Retrieves a specific user by their unique identifier.
    *
    * This method queries the API for a user with the given ID. If the user exists,
    * it returns the first matching result mapped to a domain model. The method
    * provides clear error messages distinguishing between "user not found" scenarios
    * and general API failures.
    *
    * @param id The unique identifier of the user to retrieve.
    * @return A [UserDto] domain model representing the requested user.
    * @throws Exception with message "Usuario no encontrado" if no user matches the ID.
    * @throws Exception with a descriptive message if the API request fails for other reasons.
    */
    override suspend fun getUserById(id: String): UserDto {
        try {
            val response = api.getUserById(id)
            if (response.isNotEmpty()) {
                return response.first().toDomain()
            } else {
                throw Exception("Usuario no encontrado")
            }
        } catch (e: Exception) {
            if (e.message == "Usuario no encontrado") {
                throw e
            }
            throw Exception("Error al obtener usuario: ${e.message}", e)
        }
    }

    /**
     * Registers a new user in the system through the API.
     *
     * This method converts the provided domain user model into a registration DTO
     * suitable for the API, submits the registration request, and returns the
     * original user object upon successful creation. Error handling ensures that
     * registration failures are properly communicated with context.
     *
     * @param user The [UserDto] domain model containing the user information to register.
     * @return The same [UserDto] instance passed as input, confirming successful registration.
     * @throws Exception with a descriptive message if the registration request fails.
     */

    override suspend fun registerUser(user: UserDto): UserDto {
        return try {
            val dto = user.toRegisterDto()
            api.registerUser(dto)
            user
        } catch (e: Exception) {
            throw Exception("Error al registrar usuario: ${e.message}", e)
        }
    }

    /**
     * Updates an existing user's information in the system.
     *
     * This method transforms the provided domain user model into an update DTO,
     * sends the update request to the API, and returns the original user object
     * upon successful modification. Errors during the update process are wrapped
     * with descriptive messages for easier debugging.
     *
     * @param user The [UserDto] domain model containing the updated user information.
     * @return The same [UserDto] instance passed as input, confirming successful update.
     * @throws Exception with a descriptive message if the update request fails.
     */
    override suspend fun updateUser(user: UserDto): UserDto {
        return try {
            val dto = user.toUpdateDto()
            api.updateUser(dto)
            user
        } catch (e: Exception) {
            throw Exception("Error al actualizar usuario: ${e.message}", e)
        }
    }

    /**
     * Deletes a user from the system by their unique identifier.
     *
     * This method sends a deletion request to the API for the specified user ID.
     * It returns a boolean indicating success or failure, with exceptions providing
     * detailed error information when the deletion cannot be completed.
     *
     * @param id The unique identifier of the user to delete.
     * @return `true` if the user was successfully deleted.
     * @throws Exception with a descriptive message if the deletion request fails.
     */
    override suspend fun deleteUser(id: String): Boolean {
        return try {
            api.deleteUser(id)
            true
        } catch (e: Exception) {
            throw Exception("Error al eliminar usuario: ${e.message}", e)
        }
    }

    /**
     * Retrieves all users wrapped in a domain-level [Result] type for safe error handling.
     *
     * This method provides an alternative to [getUsers] by returning results in a [Result]
     * wrapper, which encapsulates both success and failure states. This approach allows
     * calling code to handle errors gracefully without relying on exception catching.
     * Successful responses contain a list of domain-mapped users, while failures contain
     * the original exception for proper error handling.
     *
     * @return A [Result.Success] containing a [List] of [UserDto] if the request succeeds,
     *         or a [Result.Error] containing the exception if the request fails.
     */
    override suspend fun getUsersDomain(): Result<List<UserDto>> {
        return try {
            val response = api.getAllUsers()
            val users = response.map { it.toDomain() }
            Result.Success(users)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}