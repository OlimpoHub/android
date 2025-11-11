package com.app.arcabyolimpo.data.mapper.supplies

import com.app.arcabyolimpo.data.remote.dto.supplies.FilterDto
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
        batch =
            batch.map {
                SupplyBatch(
                    quantity = it.quantity,
                    expirationDate = it.expirationDate,
                )
            },
    )

fun FilterDto.toDomain(): FilterData {
    val map = mutableMapOf<String, List<String>>()

    if (!categories.isNullOrEmpty()) map["Categorías"] = categories
    if (!measures.isNullOrEmpty()) map["Medidas"] = measures
    if (!workshops.isNullOrEmpty()) map["Talleres"] = workshops
    if (!order.isNullOrEmpty()) map["Ordén"] = listOf(order)

    return FilterData(map)
}

fun GetFiltersDto.toDomain(): FilterData {
    val map = mutableMapOf<String, List<String>>()

    if (!categories.isNullOrEmpty()) map["Categorías"] = categories
    if (!measures.isNullOrEmpty()) map["Medidas"] = measures
    if (!workshops.isNullOrEmpty()) map["Talleres"] = workshops

    return FilterData(map)
}
