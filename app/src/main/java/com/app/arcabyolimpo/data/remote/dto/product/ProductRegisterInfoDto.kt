package com.app.arcabyolimpo.data.remote.dto.product

/**
 * Data Transfer Object (DTO) that groups the information needed
 * to populate the product registration form.
 *
 * It usually contains the available categories and workshops
 * that the user can select when creating or editing a product.
 *
 * @param Categories List of available product categories.
 * @param Workshops List of available workshops/centers.
 */
data class ProductRegisterInfoDto(
    val Categories: List<CategoryItemDto>,
    val Workshops: List<WorkshopItemDto>,
)

/**
 * DTO representing a single product category item returned by the API.
 *
 * @param idCategoria Unique identifier of the category.
 * @param Descripcion Human-readable description or name of the category.
 */
data class CategoryItemDto(
    val idCategoria: String,
    val Descripcion: String,
)

/**
 * DTO representing a single workshop item returned by the API.
 *
 * @param idTaller Unique identifier of the workshop/center.
 * @param Nombre Display name of the workshop associated with the product.
 */

data class WorkshopItemDto(
    val idTaller: String,
    val Nombre: String,
)

