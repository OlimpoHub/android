package com.app.arcabyolimpo.data.mapper.supplies

import com.app.arcabyolimpo.data.remote.dto.supplies.FilterSuppliesDto
import com.app.arcabyolimpo.data.remote.dto.supplies.SupplyDto
import com.app.arcabyolimpo.domain.model.supplies.FilterData
import com.app.arcabyolimpo.domain.model.supplies.Supply
import com.app.arcabyolimpo.domain.model.supplies.SupplyBatch
import kotlin.String
import kotlin.collections.List

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

fun FilterSuppliesDto.toFilterData(): FilterData {
    val map = mutableMapOf<String, List<String>>()

    if (!categories.isNullOrEmpty()) map["Categorías"] = categories
    if (!measures.isNullOrEmpty()) map["Medidas"] = measures
    if (!workshops.isNullOrEmpty()) map["Talleres"] = workshops

    return FilterData(map)
}
