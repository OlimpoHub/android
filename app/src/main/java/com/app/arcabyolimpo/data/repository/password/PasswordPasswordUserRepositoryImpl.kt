package com.app.arcabyolimpo.data.repository.password

import com.app.arcabyolimpo.data.mapper.password.toDomain
import com.app.arcabyolimpo.data.remote.api.ArcaApi
import com.app.arcabyolimpo.data.remote.dto.password.RecoverPasswordDto
import com.app.arcabyolimpo.data.remote.dto.password.UpdatePasswordDto
import com.app.arcabyolimpo.data.remote.dto.password.VerifyTokenDto
import com.app.arcabyolimpo.domain.model.password.UpdatePassword
import com.app.arcabyolimpo.domain.model.password.VerifyToken
import com.app.arcabyolimpo.domain.repository.password.PasswordUserRepository
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
class PasswordPasswordUserRepositoryImpl
    @Inject
    constructor(
        private val api: ArcaApi,
    ) : PasswordUserRepository {

    override suspend fun postRecoverPassword(
        email: String
    ): String {
        val response = api.recoverPassword(RecoverPasswordDto(email))
        return response.message
    }

    override suspend fun getVerifyToken(
        token: String
    ): VerifyToken {
        val response = api.verifyToken(token)
        return response.toDomain()
    }

    override suspend fun postUpdatePassword(
        email: String,
        password: String
    ): UpdatePassword {
        val response = api.updatePassword(UpdatePasswordDto(email, password))
        return response.toDomain()
    }
}
