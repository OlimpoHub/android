package com.app.arcabyolimpo.data.mapper.supplies

import com.app.arcabyolimpo.data.remote.dto.supplies.SupplyBatchDto
import com.app.arcabyolimpo.domain.model.supplies.Supply
import com.app.arcabyolimpo.domain.model.supplies.SupplyBatch

fun SupplyBatchDto.toDomain(): Supply {
    return Supply(
        id = idSupply.toString(),
        name = name.replaceFirstChar { it.uppercase() },
        imageUrl = imageUrl,
        unitMeasure = unitMeasure,
        batch =
            supplyBatches?.map {
                SupplyBatch(
                    quantity = it.quantity,
                    expirationDate = it.expirationDate,
                )
            } ?: emptyList()

    )
}