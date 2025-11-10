package com.app.arcabyolimpo.data.mapper.beneficiaries

import com.app.arcabyolimpo.data.remote.dto.beneficiaries.BeneficiaryDto
import com.app.arcabyolimpo.domain.model.beneficiaries.Beneficiary

/**
 * Extension function to convert a [BeneficiaryDto] from the data (remote) layer
 * into a [Beneficiary] domain model. This mapping ensures the domain layer remains
 * independent from data transfer object (DTO) structures.
 */
fun BeneficiaryDto.toDomain(): Beneficiary {
    return Beneficiary(
        id = id,
        name = name.replaceFirstChar { it.uppercase() },
        birthdate = birthdate,
        emergencyNumber = emergencyNumber,
        emergencyName = emergencyName,
        emergencyRelation = emergencyRelation,
        details = details,
        entryDate = entryDate,
        image = image,
        disabilities = disabilities,
        status = status
    )
}