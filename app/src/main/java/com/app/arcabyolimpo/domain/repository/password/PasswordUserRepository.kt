package com.app.arcabyolimpo.domain.repository.password

import com.app.arcabyolimpo.domain.model.password.UpdatePassword
import com.app.arcabyolimpo.domain.model.password.VerifyToken

interface PasswordUserRepository {
    suspend fun postRecoverPassword(email: String): String

    suspend fun getVerifyToken(token: String): VerifyToken

    suspend fun postUpdatePassword(
        email: String,
        password: String,
    ): UpdatePassword
}
