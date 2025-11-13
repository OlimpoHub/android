package com.app.arcabyolimpo.data.mapper.user

import com.app.arcabyolimpo.data.remote.dto.user.UserDto
import com.app.arcabyolimpo.data.remote.dto.user.registeruser.RegisterUserDto


fun UserDto.toDomain(): UserDto {
    return UserDto(
        idUsuario = idUsuario,
        idRol = idRol,
        nombre = nombre,
        apellidoPaterno = apellidoPaterno,
        apellidoMaterno = apellidoMaterno,
        fechaNacimiento = fechaNacimiento,
        carrera = carrera,
        correoElectronico = correoElectronico,
        telefono = telefono,
        estatus = estatus,
        reglamentoInterno = reglamentoInterno,
        copiaINE = copiaINE,
        avisoConfidencialidad = avisoConfidencialidad,
        foto = foto
    )
}

// Domain DTO → Update DTO (for sending to API)
fun UserDto.toDto(): UserDto {
    return UserDto(
        idUsuario = idUsuario ?: "0",
        idRol = idRol,
        nombre = nombre,
        apellidoPaterno = apellidoPaterno,
        apellidoMaterno = apellidoMaterno.ifBlank { "N/A" },
        fechaNacimiento = formatDateForBackend(fechaNacimiento),
        carrera = carrera.ifBlank { "N/A" },
        correoElectronico = correoElectronico,
        telefono = telefono,
        estatus = estatus,
        reglamentoInterno = reglamentoInterno,
        copiaINE = copiaINE,
        avisoConfidencialidad = avisoConfidencialidad,
        foto = foto
    )
}

// Domain DTO → Register DTO (for registration)
fun UserDto.toRegisterDto(): RegisterUserDto {
    return RegisterUserDto(
        roleId = idRol ?: "",
        name = nombre,
        lastName = apellidoPaterno,
        secondLastName = apellidoMaterno.ifBlank { "" },
        birthDate = formatDateForBackend(fechaNacimiento),
        degree = carrera.ifBlank { "" },
        email = correoElectronico,
        phone = telefono,
        status = estatus ?: 1,
        internalRegulation = reglamentoInterno,
        idCopy = copiaINE,
        confidentialityNotice = avisoConfidencialidad
    )
}

// Helper function for date formatting
private fun formatDateForBackend(dateString: String?): String {
    if (dateString.isNullOrBlank()) return ""

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
