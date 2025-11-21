package com.app.arcabyolimpo.data.mapper.beneficiaries

import com.app.arcabyolimpo.data.remote.dto.beneficiaries.BeneficiaryDto
import com.app.arcabyolimpo.data.remote.dto.beneficiaries.GetBeneficiariesDisabilitiesDto
import com.app.arcabyolimpo.data.remote.dto.filter.FilterDto
import com.app.arcabyolimpo.data.remote.dto.supplies.GetFiltersDto
import com.app.arcabyolimpo.data.remote.dto.beneficiaries.BeneficiaryFormData
import com.app.arcabyolimpo.domain.model.beneficiaries.Beneficiary
import com.app.arcabyolimpo.domain.model.filter.FilterData
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
        firstName = firstName.orEmpty(),
        paternalName = paternalName.orEmpty(),
        maternalName = maternalName.orEmpty(),
        birthdate = formatApiDate(birthdate),
        emergencyNumber = emergencyNumber.orEmpty(),
        emergencyName = emergencyName.orEmpty(),
        emergencyRelation = emergencyRelation.orEmpty(),
        details = details.orEmpty(),
        entryDate = formatApiDate(entryDate),
        image = image.orEmpty(),
        disabilities = disabilities.orEmpty(),
        status = status ?: 0,
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

/**
 * Converts a [FilterDto] (data layer model) into a [FilterData] (domain model).
 *
 * This transformation is used when the filter data entered by the user needs to be
 * represented in the domain layer for processing or state management.
 *
 * The function copies all existing filters from [FilterDto.filters] and, if an
 * [FilterDto.order] value is present, it adds it as an additional entry with
 * the key `"order"`.
 *
 * Example:
 * ```
 * val dto = FilterDto(
 *     filters = mapOf("Categorías" to listOf("Materiales")),
 *     order = "ASC"
 * )
 *
 * val domain = dto.toDomain()
 * // domain.sections => { "Categorías" = ["Materiales"], "order" = ["ASC"] }
 * ```
 *
 * @return A [FilterData] object containing the combined filter information and order.
 */
fun FilterDto.toDomain(): FilterData {
    val map = mutableMapOf<String, List<String>>()

    map.putAll(filters)

    order?.let {
        map["order"] = listOf(it)
    }

    return FilterData(map)
}

/**
 * Converts a [GetFiltersDto] (DTO from the API) into a [FilterData] (domain model).
 *
 * This function maps the API’s raw filter data — such as categories, measures,
 * and workshops — into a domain-level structure that can be displayed in the UI
 * as filter sections.
 *
 * The keys used in the resulting map correspond to localized section names:
 * `"Categorías"`, `"Medidas"`, and `"Talleres"`.
 *
 * Example:
 * ```
 * val dto = GetFiltersDto(
 *     beneficiaries = listOf("Motora", "Fisica"),
 * )
 * val domain = dto.toDomain()
 * // domain.sections => {
 * //   "Beneficiares" = ["Motora", "Fisica"],
 * // }
 * ```
 *
 * @return A [FilterData] object containing the available filter options organized by section.
 */
fun GetBeneficiariesDisabilitiesDto.toDomain(): FilterData {
    val map = mutableMapOf<String, List<String>>()

    if (!disabilities.isNullOrEmpty()) map["Discapacidades"] = disabilities

    return FilterData(map)
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
        firstName = nombre,
        paternalName = apellidoPaterno,
        maternalName = apellidoMaterno,
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