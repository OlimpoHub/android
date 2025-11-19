package com.app.arcabyolimpo.data.remote.dto.product

import com.google.gson.annotations.SerializedName

data class ProductDto(
    @SerializedName("idProducto") val idProducto: String?,
    @SerializedName("idTaller") val idTaller: String?,
    @SerializedName("Nombre") val nombre: String,
    @SerializedName("PrecioUnitario") val precioUnitario: String,
    @SerializedName("idCategoria") val idCategoria: String?,
    @SerializedName("Descripcion") val descripcion: String?,
    @SerializedName("imagen") val imagen: String?,
    @SerializedName("Disponible") val disponible: Int?,
    @SerializedName("nombreTaller") val nombreTaller: String? = null,
)
