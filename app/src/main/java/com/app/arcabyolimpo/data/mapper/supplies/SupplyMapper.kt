package com.app.arcabyolimpo.data.mapper.supplies

import com.app.arcabyolimpo.data.remote.dto.supplies.SupplyDto
import com.app.arcabyolimpo.domain.model.supplies.Supply
import com.app.arcabyolimpo.domain.model.supplies.SupplyBatch

fun SupplyDto.toDomain(): Supply =
    Supply(
        id = id,
        imageUrl = image,
        name = name.replaceFirstChar { it.uppercase() },
        unitMeasure = unitMeasure,
        batch =
            batch.flatMap { supplyBatchDto ->
                supplyBatchDto.supplyBatches?.map { spec ->
                    SupplyBatch(
                        quantity = spec.quantity,
                        expirationDate = spec.expirationDate,
                    )
                } ?: emptyList()
            },
    )
