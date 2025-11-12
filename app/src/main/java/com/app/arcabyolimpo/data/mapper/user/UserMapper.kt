package com.app.arcabyolimpo.data.mapper.user

import com.app.arcabyolimpo.data.remote.dto.user.UserDto

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
