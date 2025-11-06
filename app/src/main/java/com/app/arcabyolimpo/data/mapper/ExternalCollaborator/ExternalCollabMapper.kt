package com.app.arcabyolimpo.data.mapper.ExternalCollaborator

import com.app.arcabyolimpo.data.remote.dto.ExternalCollaborator.ExternalCollabDto
import com.app.arcabyolimpo.domain.model.ExternalCollaborator.ExternalCollab

fun ExternalCollabDto.toDomain(): ExternalCollab {
    return ExternalCollab(
        id = idUsuario,
        roleId = idRol.toIntOrNull() ?: 0,
        firstName = nombre,
        lastName = apellidoPaterno,
        secondLastName = apellidoMaterno,
        birthDate = fechaNacimiento,
        degree = carrera,
        email = correoElectronico,
        phone = telefono,
        isActive = estatus == 1,
        photoUrl = foto
    )
}

// Domain → DTO (for sending to API)
fun ExternalCollab.toDto(): ExternalCollabDto {
    return ExternalCollabDto(
        idUsuario = this.id?.toString() ?: "",
        idRol = this.roleId.toString(),
        nombre = this.firstName,
        apellidoPaterno = this.lastName,
        apellidoMaterno = this.secondLastName,
        fechaNacimiento = this.birthDate,
        carrera = this.degree,
        correoElectronico = this.email,
        contrasena = null,
        telefono = this.phone,
        estatus = if (this.isActive) 1 else 0,
        reglamentoInterno = null,
        copiaINE = null,
        avisoConfidencialidad = null,
        foto = this.photoUrl
    )
}