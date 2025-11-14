package com.app.arcabyolimpo.domain.model.product

import android.net.Uri

data class ProductAdd(
    val idWorkshop: String,
    val name: String,
    val unitaryPrice: String,
    val idCategory: String,
    val description: String,
    val status: String,
    val image: Uri,
)