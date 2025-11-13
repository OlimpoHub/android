package com.app.arcabyolimpo.domain.repository.supplies

import android.net.Uri
import com.app.arcabyolimpo.data.remote.dto.filter.FilterDto
import com.app.arcabyolimpo.domain.model.filter.FilterData
import com.app.arcabyolimpo.domain.model.supplies.Supply
import com.app.arcabyolimpo.domain.model.supplies.SupplyAdd
import com.app.arcabyolimpo.domain.model.supplies.SupplyBatchExt
import com.app.arcabyolimpo.domain.model.supplies.WorkshopCategoryList

/**
 * Retrieves detailed information for a specific supply by its [id].
 *
 * @param id The unique identifier of the supply.
 * @return A [Supply] object containing complete supply details.
 * @throws Exception If the supply cannot be found or fetched.
 */
interface SupplyRepository {
    suspend fun getSuppliesList(): List<Supply>

    suspend fun getSupplyById(id: String): Supply

    suspend fun getSupplyBatchById(id: String): SupplyBatchExt

    suspend fun filterSupply(params: FilterDto): List<Supply>

    suspend fun getFilterData(): FilterData

    suspend fun getWorkshopCategoryList(): Result<WorkshopCategoryList>

    suspend fun addSupply(
        supply: SupplyAdd,
        image: Uri?
    ): Result<Unit>
}
