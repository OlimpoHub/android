package com.app.arcabyolimpo.data.remote.dto.supplies

import com.google.gson.annotations.SerializedName

/**
 * DTO returned by GET /supplyBatch/modify/:idSupplyBatch
 */
data class SupplyBatchOneDto(
    @SerializedName("idInventario") val idInventario: String?,
    @SerializedName("idInsumo") val idInsumo: String?,
    @SerializedName("CantidadActual") val cantidadActual: Int?,
    @SerializedName("FechaActualizacion") val fechaActualizacion: String?,
    @SerializedName("FechaCaducidad") val fechaCaducidad: String?,
    @SerializedName("idTipoAdquisicion") val idTipoAdquisicion: String?,
)
