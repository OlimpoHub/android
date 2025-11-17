package com.app.arcabyolimpo.data.repository.user

import com.app.arcabyolimpo.data.mapper.user.toDomain
import com.app.arcabyolimpo.data.mapper.user.toRegisterDto
import com.app.arcabyolimpo.data.remote.api.ArcaApi
import com.app.arcabyolimpo.data.remote.dto.user.UserDto
import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.repository.user.UsersRepository
import javax.inject.Inject

class UsersRepositoryImpl @Inject constructor(
    private val api: ArcaApi,
) : UsersRepository {

    override suspend fun getUsers(): List<UserDto> {
        val response = api.getAllUsers()
        return response.map { it.toDomain() }
    }

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

    override suspend fun registerUser(user: UserDto): UserDto {
        return try {
            val dto = user.toRegisterDto()
            api.registerUser(dto)
            user
        } catch (e: Exception) {
            throw Exception("Error al registrar usuario: ${e.message}", e)
        }
    }

    override suspend fun deleteUser(id: String): Boolean {
        return try {
            api.deleteUser(id)
            true
        } catch (e: Exception) {
            throw Exception("Error al eliminar usuario: ${e.message}", e)
        }
    }

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