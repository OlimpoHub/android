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
 * - [COORDINADOR]: Coordinator or admin role.
 * - [ASISTENTE / BECARIO]: Collaborator or standard user role.
 */
enum class UserRole { COORDINADOR, ASISTENTE, BECARIO }
