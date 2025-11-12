package com.app.arcabyolimpo.data.mapper.user

import com.app.arcabyolimpo.data.remote.dto.user.UserDto


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