package com.app.arcabyolimpo.domain.model.password

data class VerifyToken(
    val valid: Boolean,
    val email: String,
    val message: String,
)
