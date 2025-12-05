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
    /**
     * Retrieves a list of all supplies.
     *
     * @return A list of [Supply] objects.
     */
    suspend fun getSuppliesList(): List<Supply>

    /**
     * Retrieves a single supply by its [id].
     *
     * @param id The unique identifier of the supply.
     * @return A [Supply] object containing the requested supply details.
     */
    suspend fun getSupplyById(id: String): Supply

    /**
     * Registers a new supply batch.
     *
     * @param batch The [RegisterSupplyBatch] object containing the details of the new batch.
     * @return A [SuccessMessage] indicating the success of the operation.
     */
    suspend fun registerSupplyBatch(batch: RegisterSupplyBatch): SuccessMessage

    /**
     * Fetches a single supply batch by its ID.
     *
     * @param id The unique identifier of the supply batch.
     * @return A [SupplyBatch] object containing the requested batch details.
     */
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

    /**
     * Retrieves a list of acquisition types available for supplies.
     *
     * @return A list of [Acquisition] objects representing the available acquisition types.
     */
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

    /**
     * Retrieves a list of all workshops and categories.
     *
     * @return A [WorkshopCategoryList] object containing the list of workshops and categories.
     */
    suspend fun getWorkshopCategoryList(): Result<WorkshopCategoryList>

    /**
     * Adds a new supply.
     *
     * @param supply The [SupplyAdd] object containing the details of the new supply.
     * @param image The URI of the image associated with the supply (optional).
     * @return A [Result] indicating the success or failure of the operation.
     */
    suspend fun addSupply(
        supply: SupplyAdd,
        image: Uri?,
    ): Result<Unit>

    /**
     * Modifies an existing supply batch.
     *
     * @param id The unique identifier of the supply batch to modify.
     * @param batch The [RegisterSupplyBatch] object containing the updated batch details.
     * @return
     */
    suspend fun modifySupplyBatch(
        id: String,
        batch: RegisterSupplyBatch,
    ): SuccessMessage

    /**
     * Retrieves a list of supply batches for a specific date and supply ID.
     *
     * @param expirationDate The date for which to retrieve the supply batches.
     * @param idSupply The ID of the supply for which to retrieve the batches.
     * @return A [SupplyBatchList] object containing the list of batches.
     */
    suspend fun supplyBatchList(
        expirationDate: String,
        idSupply: String,
    ): SupplyBatchList

    /**
     * Updates an existing supply.
     *
     * @param id The unique identifier of the supply to update.
     * @param supply The [SupplyAdd] object containing the updated supply details.
     * @param image The URI of the image associated with the supply (optional).
     * @return A [Result] indicating the success or failure of the operation.
     */
    suspend fun updateSupply(
        id: String,
        supply: SupplyAdd,
        image: Uri?,
    ): Result<Unit>
}
