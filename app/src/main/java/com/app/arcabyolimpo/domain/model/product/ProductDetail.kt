package com.app.arcabyolimpo.domain.model.product

/**
 * Domain model representing a product's full details, typically used
 * for viewing, listing, or pre-filling an edit form.
 * This model contains the structured data required by the business logic,
 * converted from the ProductDto.
 */
data class ProductDetail(
    val idProduct: String,
    val name: String,
    val unitaryPrice: String,
    val description: String,
    val status: Int,
    val workshopName: String,
    val categoryDescription: String,
    val image: String?,
)