package com.app.arcabyolimpo.data.remote.dto.productbatches

import com.google.gson.annotations.SerializedName

data class ProductBatchDto(
    @SerializedName("idProducto") val idProducto: String,
    @SerializedName("Nombre") val nombre: String,
    @SerializedName("PrecioUnitario") val precioUnitario: String,
    @SerializedName("Descripcion") val descripcion: String,
    @SerializedName("imagen") val imagen: String?,
    @SerializedName("Disponible") val disponible: Int,
    @SerializedName("idInventario") val idInventario: String,
    @SerializedName("PrecioVenta") val precioVenta: String,
    @SerializedName("CantidadProducida") val cantidadProducida: Int,
    @SerializedName("FechaCaducidad") val fechaCaducidad: String?,
    @SerializedName("FechaRealizacion") val fechaRealizacion: String,
)
