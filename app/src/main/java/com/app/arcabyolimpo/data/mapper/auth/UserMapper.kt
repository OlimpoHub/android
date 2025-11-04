package com.app.arcabyolimpo.data.mapper.auth

import com.app.arcabyolimpo.data.remote.dto.auth.UserDto
import com.app.arcabyolimpo.domain.model.auth.User
import com.app.arcabyolimpo.domain.model.auth.UserRole

/**
 * Extension function to convert a [UserDto] from the data (remote) layer
 * into a [User] domain model. This keeps the domain layer independent.
 */
fun UserDto.toDomain(): User =
    User(
        id = id,
        username = username,
        role = UserRole.valueOf(role.uppercase()),
    )
