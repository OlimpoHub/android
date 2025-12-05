package com.app.arcabyolimpo.domain.model.supplies
/**
 * Data class for adding a supply, neither ID or image are needed, but this class is used so kotlin
 * can handle the post easier
 */
data class SupplyAdd(
    val idWorkshop: String,
    val name: String,
    val measureUnit: String,
    val idCategory: String,
    val status: Int,
)
/**
 * Resulting data after the mapper transforms the Dto of the WorkshopInfo into a kotlin class
 */
data class WorkshopInfo(
    val idWorkshop: String,
    val name: String,
)
/**
 * Resulting data after the mapper transforms the Dto of the CategoryInfo into a kotlin class
 */
data class CategoryInfo(
    val idCategory: String,
    val type: String,
)
/**
 * Resulting data after the mapper transforms the Dto of the WorkshopCategoryList into a kotlin class
 * resulting in having a list with id and name of the workshop and category
 */
data class WorkshopCategoryList(
    val categories: List<CategoryInfo>,
    val workshops: List<WorkshopInfo>,
)