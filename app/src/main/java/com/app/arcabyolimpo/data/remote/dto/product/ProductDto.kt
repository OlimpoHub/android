package com.app.arcabyolimpo.data.remote.dto.product

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object (DTO) representing a product item received from the API.
 *
 * This DTO is used across the app to transport product information from
 * the remote layer, including identifiers, pricing, category, availability,
 * and optional descriptive fields.
 *
 * @param idProducto Unique identifier of the product in the backend. It may be null
 *                   in some responses (e.g., when the product is just created or in partial listings).
 * @param idTaller Identifier of the workshop/center that owns or created the product.
 * @param nombre Display name of the product.
 * @param precioUnitario Selling price of a single unit of the product (as a String from the API).
 * @param idCategoria Identifier of the category this product belongs to (nullable if not categorized).
 * @param descripcion Optional detailed description of the product.
 * @param imagen Optional URL or path to the product image.
 * @param disponible Availability status flag (e.g., 1 for available, 0 for not available).
 * @param nombreTaller Optional human-readable name of the workshop associated with the product.
 */
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
