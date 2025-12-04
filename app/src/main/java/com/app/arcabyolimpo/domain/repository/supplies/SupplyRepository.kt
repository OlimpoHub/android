package com.app.arcabyolimpo.domain.repository.supplies

import android.net.Uri
import com.app.arcabyolimpo.data.remote.dto.filter.FilterDto
import com.app.arcabyolimpo.domain.model.filter.FilterData
import com.app.arcabyolimpo.domain.model.supplies.Acquisition
import com.app.arcabyolimpo.domain.model.supplies.Batch
import com.app.arcabyolimpo.domain.model.supplies.RegisterSupplyBatch
import com.app.arcabyolimpo.domain.model.supplies.SuccessMessage
import com.app.arcabyolimpo.domain.model.supplies.Supply
import com.app.arcabyolimpo.domain.model.supplies.SupplyAdd
import com.app.arcabyolimpo.domain.model.supplies.SupplyBatch
import com.app.arcabyolimpo.domain.model.supplies.SupplyBatchExt
import com.app.arcabyolimpo.domain.model.supplies.SupplyBatchList
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

    /**
     * Fetch a single inventory batch by its inventory id (idInventario).
     * Returns a RegisterSupplyBatch-like object with initial values to populate the modify screen.
     */
    suspend fun getSupplyBatchOne(id: String): RegisterSupplyBatch

    /**
     * Filters supplies based on the provided criteria.
     *
     * This function takes a [FilterDto] containing the selected filter parameters
     * (such as category, workshop, status, or search text) and returns a list
     * of domain model [Supply] objects that match those conditions.
     *
     * @param params Object containing the filter values.
     * @return A list of filtered [Supply] items.
     */
    suspend fun filterSupply(params: FilterDto): List<Supply>

    /**
     * Retrieves all metadata required to build the supply filter UI.
     *
     * This function fetches the available filter options (such as categories,
     * workshops, statuses, or other related data) and maps them into the
     * domain model [FilterData].
     *
     * @return [FilterData] containing all available filter configuration values.
     */
    suspend fun getFilterData(): FilterData

    suspend fun getAcquisitionTypes(): List<Acquisition>

    /**
     * Sends a request to delete a specific supply batch.
     *
     * @param idSupply The ID of the supply that owns the batch.
     * @param expirationDate The expiration date of the batch to delete. This value uniquely identifies the batch.
     *
     */
    suspend fun deleteSupplyBatch(
        idSupply: String,
        expirationDate: String,
    )

    /**
     * Retrieves a list of supply batches filtered according to the provided parameters.
     *
     * @param supplyId The ID of the supply whose batches are being filtered.
     * @param params The filter configuration, including selected filter values and sort order.
     * @return A list of batches that match the filtering criteria.
     */
    suspend fun filterSupplyBatch(
        supplyId: String,
        params: FilterDto,
    ): List<Batch>

    /**
     * Fetches all available filter metadata for supply batches.
     *
     * This includes the possible values that can be used to build
     * the filter UI (e.g., acquisition types, expiration options, etc.).
     *
     * @return A [FilterData] object containing the available filter options.
     */
    suspend fun getFilterBatchData(): FilterData

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
        image: Uri?,
    ): Result<Unit>

    suspend fun modifySupplyBatch(
        id: String,
        batch: RegisterSupplyBatch,
    ): SuccessMessage

    suspend fun supplyBatchList(
        expirationDate: String,
        idSupply: String,
    ): SupplyBatchList

    suspend fun updateSupply(
        id: String,
        supply: SupplyAdd,
        image: Uri?,
    ): Result<Unit>
}
