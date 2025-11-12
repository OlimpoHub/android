package com.app.arcabyolimpo.data.mapper.supplies

import com.app.arcabyolimpo.data.remote.dto.filter.FilterDto
import com.app.arcabyolimpo.data.remote.dto.supplies.GetFiltersDto
import com.app.arcabyolimpo.data.remote.dto.supplies.SupplyDto
import com.app.arcabyolimpo.domain.model.filter.FilterData
import com.app.arcabyolimpo.domain.model.supplies.Supply
import com.app.arcabyolimpo.domain.model.supplies.SupplyBatch
import kotlin.String
import kotlin.collections.List

/**
 * Extension function to convert a [SupplyDto] from the data (remote) layer
 * into a [Supply] domain model. This mapping ensures the domain layer remains
 * independent from data transfer object (DTO) structures.
 *
 * It capitalizes the first character of the supply name and maps each batch
 * from the DTO into a [SupplyBatch] domain model.
 */
fun SupplyDto.toDomain(): Supply =
    Supply(
        id = id,
        imageUrl = image,
        name = name.replaceFirstChar { it.uppercase() },
        unitMeasure = unitMeasure,
        batch = batch.flatMap { supplyBatchDto ->
            supplyBatchDto.supplyBatches?.map { spec ->
                SupplyBatch(
                    quantity = spec.quantity,
                    expirationDate = spec.expirationDate
                )
            } ?: emptyList()
        }
    )

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
 *     categories = listOf("Herramientas", "Materiales"),
 *     measures = listOf("Piezas", "Litros"),
 *     workshops = listOf("Taller A")
 * )
 * val domain = dto.toDomain()
 * // domain.sections => {
 * //   "Categorías" = ["Herramientas", "Materiales"],
 * //   "Medidas" = ["Piezas", "Litros"],
 * //   "Talleres" = ["Taller A"]
 * // }
 * ```
 *
 * @return A [FilterData] object containing the available filter options organized by section.
 */
fun GetFiltersDto.toDomain(): FilterData {
    val map = mutableMapOf<String, List<String>>()

    if (!categories.isNullOrEmpty()) map["Categorías"] = categories
    if (!measures.isNullOrEmpty()) map["Medidas"] = measures
    if (!workshops.isNullOrEmpty()) map["Talleres"] = workshops

    return FilterData(map)
}
