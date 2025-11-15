package com.app.arcabyolimpo.domain.model.product

import android.net.Uri

/** ---------------------------------------------------------------------------------------------- *
 * Data class for adding a product, but this class is used so kotlin can handle the post easier
 * ---------------------------------------------------------------------------------------------- */
data class ProductAdd(
    val idWorkshop: String,
    val name: String,
    val unitaryPrice: String,
    val idCategory: String,
    val description: String,
    val status: String,
    val image: Uri,
)