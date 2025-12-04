package com.app.arcabyolimpo.data.mapper.user

import com.app.arcabyolimpo.data.remote.dto.user.UserDto
import com.app.arcabyolimpo.data.remote.dto.user.registeruser.RegisterUserDto
import com.app.arcabyolimpo.data.remote.dto.user.updateuser.UpdateUserDto

/**
 * Maps a [UserDto] object to a domain-level [UserDto].
 *
 * Although this mapper currently returns the same data structure,
 * it provides a layer of abstraction that allows future transformations
 * between data and domain models if the representations ever differ.
 *
 * @receiver The [UserDto] instance received from the API or data source.
 * @return A [UserDto] domain model with the same field values.
 */

fun UserDto.toDomain(): UserDto =
    UserDto(
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
        foto = foto,
    )

/**
 * Maps a domain [UserDto] to a data transfer [UserDto] suitable for API communication.
 *
 * This mapper prepares user data for transmission to the backend by applying
 * necessary transformations such as default values for optional fields, date
 * formatting for backend compatibility, and handling blank strings. It ensures
 * that null or blank values are properly converted to default values expected
 * by the API.
 *
 * @receiver The domain [UserDto] instance to be transformed.
 * @return A [UserDto] formatted for API communication with applied defaults and formatting.
 */
fun UserDto.toDto(): UserDto {
    return UserDto(
        idUsuario = idUsuario ?: "0",
        idRol = idRol,
        nombre = nombre,
        apellidoPaterno = apellidoPaterno,
        apellidoMaterno = apellidoMaterno?.ifBlank { null },
        fechaNacimiento = formatDateForBackend(fechaNacimiento),
        carrera = carrera?.ifBlank { "N/A" },
        correoElectronico = correoElectronico,
        telefono = telefono,
        estatus = estatus,
        reglamentoInterno = reglamentoInterno,
        copiaINE = copiaINE,
        avisoConfidencialidad = avisoConfidencialidad,
        foto = foto
    )
}

/**
 * Maps a domain [UserDto] to a [RegisterUserDto] for user registration requests.
 *
 * This mapper transforms the general-purpose user domain model into the specific
 * structure required by the registration endpoint. It handles field name mappings
 * between domain and API conventions (e.g., "nombre" to "name"), applies default
 * values for required fields, and ensures date formatting meets backend requirements.
 * Blank optional fields are converted to empty strings as expected by the registration API.
 *
 * @receiver The domain [UserDto] instance containing the user information to register.
 * @return A [RegisterUserDto] properly formatted for the registration API endpoint.
 */
fun UserDto.toRegisterDto(): RegisterUserDto {
    return RegisterUserDto(
        roleId = idRol ?: "",
        name = nombre ?: "",
        lastName = apellidoPaterno ?: "",
        secondLastName = apellidoMaterno?.ifBlank { null } ?: "",
        birthDate = formatDateForBackend(fechaNacimiento),
        degree = carrera?.ifBlank { null } ?: "",
        email = correoElectronico ?: "",
        phone = telefono ?: "",
        status = estatus ?: 1,
        internalRegulation = reglamentoInterno,
        idCopy = copiaINE,
        confidentialityNotice = avisoConfidencialidad,
        foto = foto
    )
}

/**
 * Maps a domain [UserDto] to an [UpdateUserDto] for user update requests.
 *
 * This mapper transforms the general-purpose user domain model into the specific
 * structure required by the update endpoint. It includes the user ID as a required
 * field and handles all field mappings between domain and API conventions. Similar
 * to the registration mapper, it applies default values, date formatting, and
 * converts blank optional fields appropriately for the update API contract.
 *
 * @receiver The domain [UserDto] instance containing the updated user information.
 * @return An [UpdateUserDto] properly formatted for the update API endpoint.
 */
fun UserDto.toUpdateDto(): UpdateUserDto {
    return UpdateUserDto(
        id = this.idUsuario ?: "",
        roleId = idRol ?: "",
        name = nombre ?: "",
        lastName = apellidoPaterno ?: "",
        secondLastName = apellidoMaterno?.ifBlank { null } ?: "",
        birthDate = formatDateForBackend(fechaNacimiento),
        degree = carrera?.ifBlank { null } ?: "",
        email = correoElectronico ?: "",
        phone = telefono ?: "",
        status = estatus ?: 1,
        internalRegulation = reglamentoInterno,
        idCopy = copiaINE,
        confidentialityNotice = avisoConfidencialidad,
        foto = foto
    )
}


/**
 * Formats a date string to ensure backend compatibility.
 *
 * This helper function standardizes date formatting by handling multiple input
 * formats and converting them to the YYYY-MM-DD format expected by the backend.
 * It handles three scenarios:
 * 1. Dates already in YYYY-MM-DD format (returned as-is)
 * 2. ISO 8601 timestamps with time component (extracts date portion before 'T')
 * 3. Other formats (returned as-is, relying on backend validation)
 *
 * If the input is null, blank, or parsing fails, an empty string is returned
 * to allow the backend to handle the missing date according to its validation rules.
 *
 * @param dateString The date string to format, potentially in various formats.
 * @return A formatted date string in YYYY-MM-DD format, or an empty string if formatting fails.
 */
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