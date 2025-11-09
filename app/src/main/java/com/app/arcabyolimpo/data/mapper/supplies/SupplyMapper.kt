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

fun FilterSuppliesDto.toDomain(): FilterData =
    FilterData(
        categories = categories.orEmpty().map { it.safeCapitalize() },
        measures = measures.orEmpty().map { it.safeCapitalize() },
        workshops = workshops.orEmpty().map { it.safeCapitalize() },
    )

private fun String.safeCapitalize(): String = if (this.isNotEmpty()) replaceFirstChar { it.uppercase() } else this
