package com.app.arcabyolimpo.data.mapper.beneficiaries

import com.app.arcabyolimpo.data.remote.dto.beneficiaries.BeneficiaryDto
import com.app.arcabyolimpo.domain.model.beneficiaries.Beneficiary
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

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
        birthdate = formatApiDate(birthdate),
        emergencyNumber = emergencyNumber.orEmpty(),
        emergencyName = emergencyName.orEmpty(),
        emergencyRelation = emergencyRelation.orEmpty(),
        details = details.orEmpty(),
        entryDate = formatApiDate(entryDate),
        image = image.orEmpty(),
        disabilities = disabilities.orEmpty(),
        status = status ?: 0
    )
}

/**
 * Private helper function to parse and format the date string.
 *
 * It takes the ISO string and return a formatted string.
 *
 * @param dateString The raw date string from the API
 * @return A formatted date string "dd/MM/yyyy" or an empty string if invalid.
 */
private fun formatApiDate(dateString: String?): String {
    if (dateString.isNullOrBlank()) {
        return ""
    }

    return try {
        val dateOnlyString = dateString.split("T").firstOrNull() ?: return ""

        val date = LocalDate.parse(dateOnlyString)

        val outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

        date.format(outputFormatter)
    } catch (e: DateTimeParseException) {
        ""
    }
}