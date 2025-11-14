package com.app.arcabyolimpo.data.mapper.beneficiaries

import com.app.arcabyolimpo.data.remote.dto.beneficiaries.BeneficiaryDto
import com.app.arcabyolimpo.data.remote.dto.beneficiaries.BeneficiaryFormData
import com.app.arcabyolimpo.data.remote.dto.workshops.WorkshopDto
import com.app.arcabyolimpo.data.remote.dto.workshops.WorkshopFormData
import com.app.arcabyolimpo.domain.model.beneficiaries.Beneficiary

/**
 * Extension function to convert a [BeneficiaryDto] from the data (remote) layer
 * into a [Beneficiary] domain model. This mapping ensures the domain layer remains
 * independent from data transfer object (DTO) structures.
 */
fun BeneficiaryDto.toDomain(): Beneficiary {
    val fullName = listOfNotNull(
        firstName,
        paternalName,
        maternalName
    ).joinToString(" ").trim()
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    return Beneficiary(
        id = id.orEmpty(),
        name = fullName.ifEmpty { "Nombre no disponible" },
        birthdate = birthdate.orEmpty(),
        emergencyNumber = emergencyNumber.orEmpty(),
        emergencyName = emergencyName.orEmpty(),
        emergencyRelation = emergencyRelation.orEmpty(),
        details = details.orEmpty(),
        entryDate = entryDate.orEmpty(),
        image = image.orEmpty(),
        disabilities = disabilities.orEmpty(),
        status = status ?: 0
    )
}

/**
 * Converts a [BeneficiaryFormData] (form submission) to a [Beneficiary] (for API).
 */
fun BeneficiaryFormData.toBeneficiaryDto() = BeneficiaryDto(
    id = id.orEmpty(),
    firstName = nombre,
    paternalName = apellidoPaterno,
    maternalName = apellidoMaterno,
    birthdate = fechaNacimiento,
    emergencyNumber = numeroEmergencia.toString(),
    emergencyName = nombreContactoEmergencia,
    emergencyRelation = relacionContactoEmergencia,
    details = descripcion,
    entryDate = fechaIngreso,
    image = foto,
    disabilities = discapacidad.orEmpty(),
    status = estatus
)

/**
 * Converts a [BeneficiaryFormData] directly to a [Beneficiary] (domain model).
 * Useful for showing previews or local data handling.
 */
fun BeneficiaryFormData.toDomain(): Beneficiary {
    val fullName = listOfNotNull(
        nombre.takeIf { it.isNotBlank() },
        apellidoPaterno.takeIf { it.isNotBlank() },
        apellidoMaterno.takeIf { it.isNotBlank() }
    ).joinToString(" ").trim()
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }

    return Beneficiary(
        id = id ?: "",
        name = fullName.ifEmpty { "Nombre no disponible" },
        birthdate = fechaNacimiento,
        emergencyNumber = numeroEmergencia,
        emergencyName = nombreContactoEmergencia,
        emergencyRelation = relacionContactoEmergencia,
        details = descripcion,
        entryDate = fechaIngreso,
        image = foto,
        disabilities = discapacidad,
        status = estatus
    )
}