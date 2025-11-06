package com.app.arcabyolimpo.domain.model.auth

/**
 * Domain model representing an authenticated user.
 *
 * @property id Unique identifier of the user.
 * @property username The username or login handle.
 * @property role The user’s assigned [UserRole].
 */
data class User(
    val id: String,
    val username: String,
    val role: UserRole,
)

/**
 * Enumerates the possible roles a user can have in the system.
 *
 * - [COORD]: Coordinator or admin role.
 * - [COLAB]: Collaborator or standard user role.
 */
enum class UserRole { COORD, COLAB }
