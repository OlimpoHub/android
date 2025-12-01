package com.app.arcabyolimpo.domain.model.product

import android.net.Uri

/**
 * Domain model representing a product's full details, typically used
 * for viewing, listing, or pre-filling an edit form.
 * This model contains the structured data required by the business logic,
 * converted from the ProductDto.
 */
data class ProductUpdate(
    val name: String,
    val unitaryPrice: String,
    val description: String,
    val idWorkshop: String,
    val idCategory: String,
    val status: String,
    val image: String?,
)