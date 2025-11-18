package com.app.arcabyolimpo.data.mapper.supplies

import com.app.arcabyolimpo.data.remote.dto.supplies.SupplyBatchDto
import com.app.arcabyolimpo.domain.model.supplies.Batch
import com.app.arcabyolimpo.domain.model.supplies.SupplyBatch
import com.app.arcabyolimpo.domain.model.supplies.SupplyBatchExt
import com.app.arcabyolimpo.data.remote.dto.supplies.SupplyBatchOneDto
import com.app.arcabyolimpo.domain.model.supplies.RegisterSupplyBatch

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
        batch =
            supplyBatches?.map { it ->
                Batch(
                    id = it.expirationDate ?: "",
                    quantity = it.quantity ?: 0,
                    expirationDate = it.expirationDate ?: "",
                    adquisitionType = it.adquisitionType ?: "",
                )
            } ?: emptyList(),
    )

fun SupplyBatchOneDto.toRegister(): RegisterSupplyBatch =
                    RegisterSupplyBatch(
                        supplyId = this.idInsumo.orEmpty(),
                        quantity = this.cantidadActual ?: 0,
                        expirationDate = this.fechaCaducidad.orEmpty(),
                        acquisition = this.idTipoAdquisicion.orEmpty(),
                        boughtDate = this.fechaActualizacion.orEmpty(),
                    )
