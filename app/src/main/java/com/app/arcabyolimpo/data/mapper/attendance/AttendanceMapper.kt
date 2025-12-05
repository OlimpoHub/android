package com.app.arcabyolimpo.data.mapper.attendance

import com.app.arcabyolimpo.data.remote.dto.attendance.AttendanceDto

/**
 * Maps a [AttendanceDto] object to a domain-level [AttendanceDto].
 *
 * Although this mapper currently returns the same data structure,
 * it provides a layer of abstraction that allows future transformations
 * between data and domain models if the representations ever differ.
 *
 * @receiver The [AttendanceDto] instance received from the API or data source.
 * @return A [AttendanceDto] domain model with the same field values.
 */

fun AttendanceDto.toDomain(): AttendanceDto =
    AttendanceDto(
        idAsistencia = idAsistencia,
        idUsuario = idUsuario,
        idTaller = idTaller,
        nombreTaller = nombreTaller,
        fechaInicio = fechaInicio,
        fechaFin = fechaFin,
    )