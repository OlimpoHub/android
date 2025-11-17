package com.app.arcabyolimpo.domain.repository.supplies

import android.net.Uri
import com.app.arcabyolimpo.data.remote.dto.filter.FilterDto
import com.app.arcabyolimpo.domain.model.filter.FilterData
import com.app.arcabyolimpo.domain.model.supplies.Acquisition
import com.app.arcabyolimpo.domain.model.supplies.RegisterSupplyBatch
import com.app.arcabyolimpo.domain.model.supplies.SuccessMessage
import com.app.arcabyolimpo.domain.model.supplies.SupplyBatch
import com.app.arcabyolimpo.domain.model.supplies.Batch
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

    suspend fun registerSupplyBatch(batch: RegisterSupplyBatch): SuccessMessage

    suspend fun getSupplyBatchById(id: String): SupplyBatchExt

    suspend fun filterSupply(params: FilterDto): List<Supply>

    suspend fun getFilterData(): FilterData

    suspend fun getAcquisitionTypes(): List<Acquisition>
    
    suspend fun deleteSupplyBatch(id: String)

    /**
     * Deletes a single supply identified by its [id].
     *
     * A simple function so that the domain or presentation layer
     * can request the deletion of an Supply.
     *
     * @param id Unique identifier of the supply to be deleted.
     */
    suspend fun deleteOneSupply(id: String)
    suspend fun getWorkshopCategoryList(): Result<WorkshopCategoryList>

    suspend fun addSupply(
        supply: SupplyAdd,
        image: Uri?
    ): Result<Unit>
}
