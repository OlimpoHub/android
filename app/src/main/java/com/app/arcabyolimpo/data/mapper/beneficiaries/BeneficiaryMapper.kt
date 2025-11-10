package com.app.arcabyolimpo.data.mapper.beneficiaries

import com.app.arcabyolimpo.data.remote.dto.beneficiaries.BeneficiaryDto

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