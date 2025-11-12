package com.app.arcabyolimpo.data.repository.user

import com.app.arcabyolimpo.data.mapper.user.toDomain
import com.app.arcabyolimpo.data.remote.api.ArcaApi
import com.app.arcabyolimpo.data.remote.dto.user.UserDto
import com.app.arcabyolimpo.domain.repository.user.UsersRepository
import javax.inject.Inject


class UsersRepositoryImpl
    @Inject
    constructor(
        private val api: ArcaApi,
    ) : UsersRepository {
        override suspend fun getUsers(): List<UserDto> {
            val response = api.getAllUsers()
            return response.map{ it.toDomain() }
        }

}