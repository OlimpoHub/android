package com.app.arcabyolimpo.data.remote.dto.productbatches

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object for Product Batch info from the API
 * 
 * @param idProducto: String -> product batch ID
 * @param nombre: String -> name of the product
 * @param precioUnitario: String -> unit price of the product
 * @param descripcion: String -> description of the product
 * @param imagen: String? -> image URL of the product (nullable)
 * @param disponible: Int -> availability status of the product
 * @param idInventario: String -> inventory ID associated with the product
 * @param precioVenta: String -> sale price of the product
 * @param cantidadProducida: Int -> quantity produced in the batch
 * @param fechaCaducidad: String? -> expiration date of the product (nullable)
 * @param fechaRealizacion: String -> production date of the product
 */
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
