package com.app.arcabyolimpo.data.mapper.beneficiaries

import com.app.arcabyolimpo.data.remote.dto.beneficiaries.BeneficiaryDto
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