package com.app.arcabyolimpo.data.mapper.beneficiaries

import com.app.arcabyolimpo.data.remote.dto.beneficiaries.BeneficiaryDto
import com.app.arcabyolimpo.data.remote.dto.beneficiaries.GetBeneficiariesDisabilitiesDto
import com.app.arcabyolimpo.data.remote.dto.supplies.GetFiltersDto
import com.app.arcabyolimpo.domain.model.beneficiaries.Beneficiary
import com.app.arcabyolimpo.domain.model.filter.FilterData

/**
 * Extension function to convert a [BeneficiaryDto] from the data (remote) layer
 * into a [Beneficiary] domain model. This mapping ensures the domain layer remains
 * independent from data transfer object (DTO) structures.
 */
fun BeneficiaryDto.toDomain(): Beneficiary =
    Beneficiary(
        id = id.orEmpty(),
        name = name?.replaceFirstChar { it.uppercase() }.orEmpty(),
        birthdate = birthdate.orEmpty(),
        emergencyNumber = emergencyNumber.orEmpty(),
        emergencyName = emergencyName.orEmpty(),
        emergencyRelation = emergencyRelation.orEmpty(),
        details = details.orEmpty(),
        entryDate = entryDate.orEmpty(),
        image = image.orEmpty(),
        disabilities = disabilities.orEmpty(),
        status = status ?: 0,
    )

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
