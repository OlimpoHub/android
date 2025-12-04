package com.app.arcabyolimpo.data.mapper.supplies

import com.app.arcabyolimpo.data.remote.dto.supplies.FilteredBatchDto
import com.app.arcabyolimpo.data.remote.dto.supplies.GetFilterBatchDto
import com.app.arcabyolimpo.data.remote.dto.supplies.SupplyBatchDto
import com.app.arcabyolimpo.data.remote.dto.supplies.SupplyBatchSpecsDto
import com.app.arcabyolimpo.domain.model.filter.FilterData
import com.app.arcabyolimpo.domain.model.supplies.Batch
import com.app.arcabyolimpo.domain.model.supplies.SupplyBatchExt
import com.app.arcabyolimpo.data.remote.dto.supplies.SupplyBatchOneDto
import com.app.arcabyolimpo.domain.model.supplies.RegisterSupplyBatch
import kotlin.math.exp

/**
 * Helper function that maps the Dto of supplyBatch to return it into a kotlin object
 *
 * @return SupplyBatchExt
 */
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
/**
 * Helper function that maps the Dto of supplyBatch to return it into a kotlin object
 *
 * @return SupplyBatchExt
 * */
fun SupplyBatchOneDto.toRegister(): RegisterSupplyBatch =
                    RegisterSupplyBatch(
                        supplyId = this.idInsumo.orEmpty(),
                        quantity = this.cantidadActual ?: 0,
                        expirationDate = this.fechaCaducidad.orEmpty(),
                        acquisition = this.idTipoAdquisicion.orEmpty(),
                        boughtDate = this.fechaActualizacion.orEmpty(),
                    )
/**
 * Maps filter metadata from [GetFilterBatchDto] to the domain model [FilterData].
 *
 * @return A [FilterData] instance containing UI-usable filter information.
 */
fun GetFilterBatchDto.toDomain(): FilterData {
    val map = mutableMapOf<String, List<String>>()

    if (!acquisitionType.isNullOrEmpty()) {
        map["Tipo de Adquisición"] = acquisitionType
    }

    return FilterData(map)
}

/**
 * Maps a [FilteredBatchDto] into the domain model [Batch].
 *
 * @return A [Batch] object representing a filtered batch.
 */
fun FilteredBatchDto.toDomain(): Batch {
    return Batch(
        id = id ?: "",
        adquisitionType = adquisitionType ?: "",
        expirationDate = expirationDate ?: "",
        quantity = quantity ?: 0,
    )
}

