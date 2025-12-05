package com.app.arcabyolimpo.data.remote.dto.product

import com.google.gson.annotations.SerializedName

/**
 * Data transfer object representing product information from the API.
 *
 * This DTO encapsulates the structure of product data as returned by the backend,
 * using Spanish field names that match the API's naming conventions. Gson annotations
 * handle the mapping between the API's JSON field names and Kotlin property names,
 * ensuring proper deserialization regardless of the differences in naming conventions.
 *
 * The DTO supports optional fields to accommodate various API response scenarios,
 * such as products without images, descriptions, or workshop associations. This
 * flexibility allows the application to handle incomplete data gracefully without
 * causing deserialization failures.
 *
 * @property idProducto The unique identifier of the product. Nullable to handle cases
 *                      where the API might not return an ID (though this should be rare).
 * @property idTaller The unique identifier of the workshop that produces this product.
 *                    Nullable to support products not yet associated with a workshop.
 * @property nombre The name of the product as defined in the backend system.
 * @property precioUnitario The unit price of the product as a string. String type allows
 *                          flexibility in handling various price formats and precision.
 * @property idCategoria The unique identifier of the category this product belongs to.
 *                       Nullable to support uncategorized products or pending categorization.
 * @property descripcion A textual description of the product providing details about its
 *                       features, materials, or usage. Nullable for products without descriptions.
 * @property imagen The URL or path to the product's image. Nullable for products without
 *                  associated images, allowing the UI to display placeholders.
 * @property disponible The availability status of the product as an integer (typically 1 for
 *                      available, 0 for unavailable). Nullable with a default assumption
 *                      of available if not specified.
 * @property nombreTaller The name of the workshop that produces this product. This field
 *                        is typically populated in expanded API responses or joined queries.
 *                        Defaults to null for basic product listings.
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
