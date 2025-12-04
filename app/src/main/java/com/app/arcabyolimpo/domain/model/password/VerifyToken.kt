package com.app.arcabyolimpo.domain.model.password

/**
 * Represents the result of validating a password recovery token.
 *
 * This model is used by the domain layer to indicate whether a provided token
 * is valid, and to return the associated email and a descriptive message.
 *
 * @property valid Indicates whether the token is valid and can be used.
 * @property email Email address associated with the validated token.
 * @property message Human-readable message describing the validation result.
 */
data class VerifyToken(
    val valid: Boolean,
    val email: String,
    val message: String,
)
