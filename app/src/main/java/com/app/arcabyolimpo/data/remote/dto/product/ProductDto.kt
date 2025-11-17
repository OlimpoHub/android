package com.app.arcabyolimpo.data.remote.dto.product

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object (DTO) representing detailed information of a product received from the API.
 *
 * This DTO is used to fetch the complete details of a single product item,
 * including its names, descriptions, price, and image URL.
 *
 * @param name The name of the product.
 * @param unitaryPrice The selling price of a single unit.
 * @param workshopName The name of the workshop that created the product.
 * @param categoryDescription The description or name of the product category.
 * @param status The availability status of the product (e.g., 1 for available).
 * @param description The detailed description of the product.
 * @param imageUrl The URL or path of the product image.
 */
data class ProductDto(
    @SerializedName("idProducto") val idProduct: String,
    @SerializedName("Nombre") val name: String,
    @SerializedName("PrecioUnitario") val unitaryPrice: String,
    @SerializedName("nombreTaller") val workshopName: String,
    @SerializedName("Categoria") val categoryDescription: String,
    @SerializedName("Disponible") val status: Int, // 1 o 0
    @SerializedName("Descripcion") val description: String,
    @SerializedName("imagen") val image: String?
)