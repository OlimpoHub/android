package com.app.arcabyolimpo.data.remote.dto.product

data class ProductRegisterInfoDto(
    val Categories: List<CategoryItemDto>,
    val Workshops: List<WorkshopItemDto>,
)

data class CategoryItemDto(
    val idCategoria: String,
    val Descripcion: String,
)

data class WorkshopItemDto(
    val idTaller: String,
    val Nombre: String,
)
