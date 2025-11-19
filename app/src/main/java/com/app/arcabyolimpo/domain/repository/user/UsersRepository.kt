package com.app.arcabyolimpo.domain.repository.user

import com.app.arcabyolimpo.data.remote.dto.user.UserDto
import com.app.arcabyolimpo.domain.common.Result

interface UsersRepository {
    suspend fun getUsers(): List<UserDto>
    suspend fun getUserById(id: String): UserDto
    suspend fun registerUser(user: UserDto): UserDto
    suspend fun deleteUser(id: String): Boolean
    suspend fun getUsersDomain(): Result<List<UserDto>>
    suspend fun updateUser(user: UserDto): UserDto
}