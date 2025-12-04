package com.app.arcabyolimpo.domain.model.password

/**
 * Represents the result of a password update operation.
 *
 * This model is returned by the domain layer after attempting to update
 * a user's password. It contains the operation status and a descriptive
 * message for the UI or calling logic.
 *
 * @property status Indicates whether the password update was successful.
 * @property message Human-readable message describing the result of the update.
 */
data class UpdatePassword(
    val status: Boolean,
    val message: String,
)
