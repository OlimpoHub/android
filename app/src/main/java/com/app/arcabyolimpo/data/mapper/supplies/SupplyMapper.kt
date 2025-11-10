package com.app.arcabyolimpo.data.mapper.supplies

import com.app.arcabyolimpo.data.remote.dto.supplies.SupplyBatchDto
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
            batch.map {
                SupplyBatch(
                    quantity = it.quantity,
                    expirationDate = it.expirationDate,
                    boughtDate = it.boughtDate,
                    supplyId = it.supplyId,
                )
            },
    )

fun SupplyBatchDto.toDomain(): SupplyBatch =
    SupplyBatch(
        quantity = quantity,
        expirationDate = expirationDate,
        boughtDate = boughtDate,
        supplyId = supplyId,
    )
