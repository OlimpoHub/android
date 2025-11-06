package com.app.arcabyolimpo.domain.model.ExternalCollaborator

data class ExternalCollab(
    val id: Int? = null,
    val roleId: Int,
    val firstName: String,
    val lastName: String,
    val secondLastName: String,
    val birthDate: String,
    val degree: String,
    val email: String,
    val phone: String,
    val isActive: Boolean,
    val photoUrl: String? = null
)