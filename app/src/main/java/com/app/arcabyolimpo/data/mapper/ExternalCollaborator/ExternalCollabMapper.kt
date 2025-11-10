package com.app.arcabyolimpo.data.mapper.ExternalCollaborator

import com.app.arcabyolimpo.data.remote.dto.ExternalCollaborator.ExternalCollabDto
import com.app.arcabyolimpo.data.remote.dto.ExternalCollaborator.RegisterExtCollab.RegisterExternalCollabDto
import com.app.arcabyolimpo.domain.model.ExternalCollaborator.ExternalCollab

fun ExternalCollabDto.toDomain(): ExternalCollab {
    return ExternalCollab(
        id = idUsuario,
        roleId = idRol?.toIntOrNull() ?: 0,
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
        idUsuario = "0",
        idRol = this.roleId.toString(),
        nombre = this.firstName,
        apellidoPaterno = this.lastName,
        apellidoMaterno = this.secondLastName.ifBlank { "N/A" },
        fechaNacimiento = formatDateForBackend(this.birthDate),
        carrera = this.degree.ifBlank { "N/A" },
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

fun ExternalCollab.toRegisterDto(): RegisterExternalCollabDto {
    return RegisterExternalCollabDto(
        roleId = this.roleId.toString(),
        name = this.firstName,
        lastName = this.lastName,
        secondLastName = this.secondLastName.ifBlank { "" },
        birthDate = formatDateForBackend(this.birthDate),
        degree = this.degree.ifBlank { "" },
        email = this.email,
        phone = this.phone,
        status = if (this.isActive) 1 else 0,
        internalRegulation = null,
        idCopy = null,
        confidentialityNotice = null
    )
}

private fun formatDateForBackend(dateString: String): String {
    if (dateString.isBlank()) return ""

    return try {
        if (dateString.matches(Regex("\\d{4}-\\d{2}-\\d{2}"))) {
            return dateString
        }

        if (dateString.contains("T")) {
            return dateString.substringBefore("T")
        }

        dateString
    } catch (e: Exception) {
        ""
    }
}