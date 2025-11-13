package com.app.arcabyolimpo.data.mapper.supplies

import com.app.arcabyolimpo.data.remote.dto.supplies.CategoryInfoDto
import com.app.arcabyolimpo.data.remote.dto.supplies.WorkshopCategoryListDto
import com.app.arcabyolimpo.data.remote.dto.supplies.WorkshopInfoDto
import com.app.arcabyolimpo.domain.model.supplies.CategoryInfo
import com.app.arcabyolimpo.domain.model.supplies.WorkshopCategoryList
import com.app.arcabyolimpo.domain.model.supplies.WorkshopInfo

/** ---------------------------------------------------------------------------------------------- *
 * Helper function that maps the Dto of WorkshopInfo to return it into a kotlin object
 *
 * @return WorkshopInfo
 * ---------------------------------------------------------------------------------------------- */
fun WorkshopInfoDto.toDomain(): WorkshopInfo = WorkshopInfo(idWorkshop = id, name = name)
/** ---------------------------------------------------------------------------------------------- *
 * Helper function that maps the Dto of CategoryInfo to return it into a kotlin object
 *
 * @return CategoryInfo
 * ---------------------------------------------------------------------------------------------- */
fun CategoryInfoDto.toDomain(): CategoryInfo = CategoryInfo(idCategory = id, type = name)
/** ---------------------------------------------------------------------------------------------- *
 * Helper function that maps the Dto of WorkshopInfo to return it into a kotlin object
 *
 * @return WorkshopInfo
 * ---------------------------------------------------------------------------------------------- */
fun WorkshopCategoryListDto.toDomain(): WorkshopCategoryList = WorkshopCategoryList(
    categories = categories.map { it.toDomain() },
    workshops = workshops.map { it.toDomain() }
)