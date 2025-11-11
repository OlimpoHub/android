package com.app.arcabyolimpo.data.mapper.supplies

import com.app.arcabyolimpo.data.remote.dto.supplies.SupplyDto
import com.app.arcabyolimpo.domain.model.supplies.Supply
import com.app.arcabyolimpo.domain.model.supplies.SupplyBatch

/**
 * Extension function to convert a [SupplyDto] from the data (remote) layer
 * into a [Supply] domain model. This mapping ensures the domain layer remains
 * independent from data transfer object (DTO) structures.
 *
 * It capitalizes the first character of the supply name and maps each batch
 * from the DTO into a [SupplyBatch] domain model.
 */
fun SupplyDto.toDomain(): Supply {
    return Supply(
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
