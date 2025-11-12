package com.app.arcabyolimpo.domain.repository.user

import com.app.arcabyolimpo.data.remote.dto.user.UserDto

interface UsersRepository {
    suspend fun getUsers(): List<UserDto>
}