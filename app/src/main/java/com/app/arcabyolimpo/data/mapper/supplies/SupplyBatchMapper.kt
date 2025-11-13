package com.app.arcabyolimpo.data.mapper.supplies

import com.app.arcabyolimpo.data.remote.dto.supplies.SupplyBatchDto
import com.app.arcabyolimpo.domain.model.supplies.Batch
import com.app.arcabyolimpo.domain.model.supplies.SupplyBatch
import com.app.arcabyolimpo.domain.model.supplies.SupplyBatchExt

/** ---------------------------------------------------------------------------------------------- *
 * Helper function that maps the Dto of supplyBatch to return it into a kotlin object
 *
 * @return SupplyBatchExt
 * ---------------------------------------------------------------------------------------------- */
fun SupplyBatchDto.toDomain(): SupplyBatchExt =
    SupplyBatchExt(
        id = idSupply.toString(),
        name = name.replaceFirstChar { it.uppercase() },
        imageUrl = imageUrl,
        unitMeasure = unitMeasure,
        totalQuantity = totalQuantity,
        workshop = workshop,
        category = category,
        status = status,
        batch = supplyBatches?.map {
            Batch(
                quantity = it.quantity ?: 0,
                expirationDate = it.expirationDate ?: "",
                adquisitionType = it.adquisitionType ?: "",
            )
        } ?: emptyList(),
    )
