package com.app.arcabyolimpo.data.remote.dto.supplies

import com.google.gson.annotations.SerializedName

/**
 * Data class used to fetch the minimum information attributes of a workshop like ID and name,
 * Because those are needed for the Supply Add view, specially in the dropdown
 */
data class WorkshopInfoDto(
    @SerializedName("idTaller") val id: String,
    @SerializedName("name") val name: String,
)
/**
 * Data class used to fetch the minimum information attributes of a category like ID and type,
 * Because those are needed for the Supply Add view, specially in the dropdown
 */
data class CategoryInfoDto(
    @SerializedName("idCategory") val id: String,
    @SerializedName("categoryType") val name: String,
)
/**
 * Data class used to fetch all both workshop and categories that are in the db to used them easily
 * in the Supply Add View
 */
data class WorkshopCategoryListDto(
    @SerializedName("categories") val categories: List<CategoryInfoDto>,
    @SerializedName("workshops") val workshops: List<WorkshopInfoDto>,
)