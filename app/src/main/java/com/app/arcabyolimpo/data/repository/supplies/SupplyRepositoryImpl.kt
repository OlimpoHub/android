package com.app.arcabyolimpo.data.repository.supplies

import android.content.Context
import android.net.Uri
import android.util.Log
import com.app.arcabyolimpo.data.local.supplies.preferences.SupplyLocalDataSource
import com.app.arcabyolimpo.data.local.supplybatches.preferences.SupplyBatchesPreferences
import com.app.arcabyolimpo.data.mapper.supplies.toDomain
import com.app.arcabyolimpo.data.mapper.supplies.toRegister
import com.app.arcabyolimpo.data.remote.api.ArcaApi
import com.app.arcabyolimpo.data.remote.dto.filter.FilterDto
import com.app.arcabyolimpo.data.remote.dto.supplies.DeleteDto
import com.app.arcabyolimpo.data.remote.dto.supplies.DeleteSupplyBatchDto
import com.app.arcabyolimpo.data.remote.dto.supplies.FilterRequestDto
import com.app.arcabyolimpo.data.remote.dto.supplies.RegisterSupplyBatchDto
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
import com.app.arcabyolimpo.domain.repository.supplies.SupplyRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of [SupplyRepository] that handles data operations for supplies.
 *
 * @param api The API service used for making network requests.
 * @param localDataSource The local data source for caching and retrieving data.
 * @param batchesPreferences The preferences for caching supply batches.
 * @param context The application context for accessing resources.
 * @return An instance of [SupplyRepository].
 */
@Singleton
class SupplyRepositoryImpl
    @Inject
    constructor(
        private val api: ArcaApi,
        private val localDataSource: SupplyLocalDataSource,
        private val batchesPreferences: SupplyBatchesPreferences,
        @ApplicationContext private val context: Context,
    ) : SupplyRepository {
    /**
     * Gets supplies list with offline-first strategy:
     * 1. Check if valid cache exists -> return cached data
     * 2. Try to fetch from API -> cache and return
     * 3. If API fails and cache exists -> return stale cache
     * 4. If no cache and API fails -> throw exception
     */
    override suspend fun getSuppliesList(): List<Supply> {
        return try {
            val response = api.getSuppliesList()
            val supplies = response.map { dto ->
                Supply(
                    id = dto.id,
                    name = dto.name,
                    imageUrl = dto.image ?: "",
                    unitMeasure = "",
                    batch = emptyList(),
                )
            }

            localDataSource.cacheSuppliesList(supplies)
            supplies

        } catch (e: Exception) {
            localDataSource.getCachedSuppliesList() ?: throw e
        }
    }

    /**
     * Gets a specific supply by its ID.
     *
     * @param id The ID of the supply to retrieve.
     * @return The [Supply] representing the requested supply.
     */
    override suspend fun getSupplyById(id: String): Supply = api.getSupply(id).toDomain()

    /**
     * Helper function to fetch supplies from API and cache them.
     *
     * @return List of [Supply]
     */
    private suspend fun fetchAndCacheSupplies(): List<Supply> {
        Log.d("SupplyRepository", "Fetching supplies from API")
        val response = api.getSuppliesList()
        val supplies =
            response.map { dto ->
                Supply(
                    id = dto.id,
                    name = dto.name,
                    imageUrl = dto.image ?: "",
                    unitMeasure = "",
                    batch = emptyList(),
                )
            }

        // Cache the fresh data
        localDataSource.cacheSuppliesList(supplies)
        Log.d("SupplyRepository", "Cached ${supplies.size} supplies")

        return supplies
    }

    /**
     * Forces a refresh of the supplies list from the API.
     * Useful for pull-to-refresh functionality.
     */
    suspend fun refreshSuppliesList(): List<Supply> = fetchAndCacheSupplies()

    /**
     * Clears the local cache.
     * Useful for debugging or when data becomes corrupted.
     */
    fun clearCache() {
        localDataSource.clearCache()
    }

    /**
     * Filters supplies based on the provided criteria.
     *
     * This function calls the API using the given [FilterDto], receives a list of
     * supply DTOs, and maps each item into the domain model [Supply].
     *
     * Fields not provided by the API (such as unitMeasure or batch)
     * are initialized with default values.
     *
     * @param filters Object containing the filter criteria for supplies.
     * @return A list of [Supply] mapped from the API response.
     */
    override suspend fun filterSupply(filters: FilterDto): List<Supply> {
        val response = api.filterSupplies(filters)
        return response.map { dto ->
            Supply(
                id = dto.id,
                name = dto.name,
                imageUrl = dto.image ?: "",
                unitMeasure = "",
                batch = emptyList(),
            )
        }
    }

    /**
     * Retrieves all available filter data for supplies.
     *
     * This function consumes the API endpoint that returns the metadata needed
     * to build supply filters (such as categories, workshops, statuses, etc.),
     * and converts the response into the domain model [FilterData].
     *
     * @return A [FilterData] object containing the filter configuration.
     */
    override suspend fun getFilterData(): FilterData = api.getFilterSupplies().toDomain()
    /**
     * Obtains a specific supply batch by its ID.
     *
     * @param id The ID of the supply batch to retrieve.
     * @return The [RegisterSupplyBatch]
     */
    override suspend fun getSupplyBatchOne(id: String): RegisterSupplyBatch {
        val dto = api.getSupplyBatchOne(id)
        return dto.toRegister()
    }
    /**
     * Filters supply batches based on the provided criteria.
     *
     * @param idSupply The ID of the supply to filter batches for.
     * @param filters The filter criteria to apply.
     * @return A list of [Batch] representing the filtered supply batches.
     */
    override suspend fun filterSupplyBatch(
        idSupply: String,
        filters: FilterDto,
    ): List<Batch> {
        val body =
            FilterRequestDto(
                idSupply = idSupply,
                filters = filters.filters,
                order = filters.order,
            )

        val response = api.filterSupplyBatch(body)
        return response.map { it.toDomain() }
    }
    /**
     * Obtains filter data for supply batches.
     *
     * @return A [FilterData] object containing the filter configuration.
     */
    override suspend fun getFilterBatchData(): FilterData = api.getFilterSupplyBatch().toDomain()
    /**
     * Registers a new supply batch.
     *
     * @param batch The batch information to register.
     * @return A [SuccessMessage] indicating the result of the registration.
     */
    override suspend fun registerSupplyBatch(batch: RegisterSupplyBatch): SuccessMessage {
        val dto =
            RegisterSupplyBatchDto(
                supplyId = batch.supplyId,
                quantity = batch.quantity,
                expirationDate = batch.expirationDate,
                acquisition = batch.acquisition,
                boughtDate = batch.boughtDate,
            )

        val responseDto = api.registerSupplyBatch(dto)

        localDataSource.clearCache()

        return responseDto.toDomain()
    }
    /**
     * Obtains a list of acquisition types from the API.
     *
     * @return A list of [Acquisition] representing the available acquisition types.
     */
    override suspend fun getAcquisitionTypes(): List<Acquisition> {
        val response = api.getAcquisitionTypes()
        return response.map { dto ->
            Acquisition(
                id = dto.id,
                description = dto.description,
            )
        }
    }
    /**
     * Deletes a supply batch by its expiration date.
     *
     * @param idSupply The ID of the supply to delete.
     * @param expirationDate The expiration date of the batch to delete.
     */
    override suspend fun deleteSupplyBatch(
        idSupply: String,
        expirationDate: String,
    ) {
        val formattedDate =
            if (expirationDate.contains("T")) {
                expirationDate.substring(0, 10)
            } else {
                expirationDate
            }

        val body = DeleteSupplyBatchDto(idSupply, formattedDate)
        localDataSource.clearCache()
        val result = api.deleteSupplyBatch(body)
    }
    /**
     * Deletes a supply by its ID.
     *
     * @param id The ID of the supply to delete.
     */
    override suspend fun deleteOneSupply(id: String) {
        val body = DeleteDto(id)
        localDataSource.clearCache()
        val result = api.deleteOneSupply(body)
    }
    /**
     * Gets workshop and category list from the API.
     *
     * @return A [Result] containing the workshop and category list.
     */
    override suspend fun getWorkshopCategoryList(): Result<WorkshopCategoryList> =
        try {
            val workshopCategoryListDto = api.getWorkshopCategoryList()
            Result.success(workshopCategoryListDto.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    /**
     * Adds a new supply to the system.
     *
     * @param supply The supply information to add.
     * @param image The image URI for the new supply.
     * @return A [SuccessMessage] indicating the result of the addition.
     */
    override suspend fun addSupply(
        supply: SupplyAdd,
        image: Uri?,
    ): Result<Unit> =
        try {
            val imagePart =
                image?.let {
                    val input = context.contentResolver.openInputStream(it)
                    val bytes = input?.readBytes()
                    input?.close()

                    if (bytes != null) {
                        val requestFile =
                            bytes.toRequestBody(
                                context.contentResolver.getType(it)?.toMediaTypeOrNull(),
                            )
                        MultipartBody.Part.createFormData(
                            "imagenInsumo",
                            "image.jpg",
                            requestFile,
                        )
                    } else {
                        null
                    }
                }

            val idWorkshop = supply.idWorkshop.toRequestBody("text/plain".toMediaTypeOrNull())
            val name = supply.name.toRequestBody("text/plain".toMediaTypeOrNull())
            val measureUnit = supply.measureUnit.toRequestBody("text/plain".toMediaTypeOrNull())
            val idCategory = supply.idCategory.toRequestBody("text/plain".toMediaTypeOrNull())
            val status = supply.status.toString().toRequestBody("text/plain".toMediaTypeOrNull())

            api.addSupply(
                idWorkshop = idWorkshop,
                name = name,
                measureUnit = measureUnit,
                idCategory = idCategory,
                status = status,
                imagenInsumo = imagePart,
            )

            localDataSource.clearCache()

            Result.success(Unit)
        } catch (e: HttpException) {
            val error = e.response()?.errorBody()?.string()
            val message =
                try {
                    JSONObject(error ?: "").getString("message")
                } catch (jsonException: Exception) {
                    "Error al agregar insumo: ${e.message()}"
                }
            Result.failure(Exception(message))
        } catch (e: IOException) {
            Result.failure(Exception("No hay conexión a internet"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    /**
     * Function to modify a supply.
     *
     * @param idSupply The ID of the supply to modify.
     * @param supply The updated supply information.
     * @param image The image URI for the updated supply.
     * @return A [SuccessMessage] indicating the result of the modification.
     */
    override suspend fun updateSupply(
        idSupply: String,
        supply: SupplyAdd,
        image: Uri?,
    ): Result<Unit> =
        try {
            val imagePart =
                image?.let {
                    val input = context.contentResolver.openInputStream(it)
                    val bytes = input?.readBytes()
                    input?.close()
                    if (bytes != null) {
                        val requestFile =
                            bytes.toRequestBody(
                                context.contentResolver
                                    .getType(it)
                                    ?.toMediaTypeOrNull(),
                            )
                        MultipartBody.Part.createFormData(
                            "imagenInsumo",
                            "image.jpg",
                            requestFile,
                        )
                    } else {
                        null
                    }
                }

            val idWorkshop = supply.idWorkshop.toRequestBody("text/plain".toMediaTypeOrNull())
            val name = supply.name.toRequestBody("text/plain".toMediaTypeOrNull())
            val measureUnit = supply.measureUnit.toRequestBody("text/plain".toMediaTypeOrNull())
            val idCategory = supply.idCategory.toRequestBody("text/plain".toMediaTypeOrNull())
            val status = supply.status.toString().toRequestBody("text/plain".toMediaTypeOrNull())

            api.updateSupply(
                idSupply = idSupply,
                idWorkshop = idWorkshop,
                name = name,
                measureUnit = measureUnit,
                idCategory = idCategory,
                status = status,
                imagenInsumo = imagePart,
            )

            localDataSource.clearCache()

            Result.success(Unit)
        } catch (e: HttpException) {
            val error = e.response()?.errorBody()?.string()
            val message =
                try {
                    JSONObject(error ?: "").getString("message")
                } catch (jsonException: Exception) {
                    "Error al modificar insumo: ${e.message()}"
                }
            Result.failure(Exception(message))
        } catch (e: IOException) {
            Result.failure(Exception("No hay conexión a internet"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    /**
     * Function to modify a supply batch.
     *
     * @param id The ID of the supply batch to modify.
     * @param batch The updated batch information.
     * @return A [SuccessMessage] indicating the result of the modification.
     */
    override suspend fun modifySupplyBatch(
        id: String,
        batch: RegisterSupplyBatch,
    ): SuccessMessage {
        val dto =
            RegisterSupplyBatchDto(
                supplyId = batch.supplyId,
                quantity = batch.quantity,
                expirationDate = batch.expirationDate,
                acquisition = batch.acquisition,
                boughtDate = batch.boughtDate,
            )
        val responseDto = api.modifySupplyBatch(id, dto)

        localDataSource.clearCache()

        return responseDto.toDomain()
    }
    /**
     * Gets a list of supply batches for a given expiration date and supply ID.
     *
     * @param expirationDate The expiration date for which to fetch supply batches.
     * @param idSupply The ID of the supply for which to fetch batches.
     * @return A [SupplyBatchList] containing the list of batches
    */
    override suspend fun supplyBatchList(
        expirationDate: String,
        idSupply: String,
    ): SupplyBatchList {

        try {
            Log.d(
                "SupplyRepo",
                "supplyBatchList request expirationDate=$expirationDate idSupply=$idSupply"
            )
            val response = api.supplyBatchList(expirationDate, idSupply)

            val combinedItems = response
            val domainList = combinedItems.map { it.toDomain() }

            batchesPreferences.saveSupplyBatchesList(
                expirationDate = expirationDate,
                idSupply = idSupply,
                supplyBatchesList = domainList,
            )

            Log.d(
                "SupplyRepo",
                "API success, saved ${domainList.size} items in cache for expirationDate=$expirationDate, idSupply=$idSupply",
            )
            Log.d(
                "SupplyRepo",
                "api returned responseSize=${response.size}, combinedItems=${combinedItems.size}, ids=${
                    combinedItems.joinToString {
                        it.id
                    }
                }",
            )

            return SupplyBatchList(batch = domainList)
        } catch (e: Exception) {
            Log.e("SupplyRepo", "API error: ${e.message}. Trying fallback cache")

            batchesPreferences.getSupplyBatchCache(expirationDate, idSupply)?.let { expiredCache ->
                Log.d(
                    "SupplyRepo",
                    "Using EXPIRED cache for expirationDate=$expirationDate, idSupply=$idSupply"
                )
                return SupplyBatchList(batch = expiredCache.supplyBatchesList)
            }

            throw e
        }
    }

    /**
     * Gets a specific supply batch by its ID.
     *
     * @param id The ID of the supply batch to retrieve.
     * @return The [SupplyBatchExt] representing the requested supply batch.
     */
    override suspend fun getSupplyBatchById(id: String): SupplyBatchExt {

        // Try to fetch fresh data from API
        return try {
            Log.d("SupplyRepository", "Fetching supply batch detail from API for ID: $id")
            val supplyBatchExt = api.getSupplyBatchById(id).toDomain()

            // Cache the fresh data
            localDataSource.cacheSupplyDetail(supplyBatchExt)
            Log.d("SupplyRepository", "Cached supply batch detail for ID: $id")

            supplyBatchExt
        } catch (e: Exception) {
            Log.e("SupplyRepository", "API call failed for supply batch ID $id: ${e.message}")

            // If we have stale cache, return it as fallback
            if (localDataSource.hasSupplyDetailCache(id)) {
                Log.d(
                    "SupplyRepository",
                    "Returning stale cached supply batch detail as fallback for ID: $id"
                )
                val cachedDetail = localDataSource.getCachedSupplyDetail(id)
                if (cachedDetail != null) {
                    return cachedDetail
                }
            }

            // Try to fetch fresh data from API
            return try {
                Log.d("SupplyRepository", "Fetching supply batch detail from API for ID: $id")
                val supplyBatchExt = api.getSupplyBatchById(id).toDomain()

                // Cache the fresh data
                localDataSource.cacheSupplyDetail(supplyBatchExt)
                Log.d("SupplyRepository", "Cached supply batch detail for ID: $id")

                supplyBatchExt
            } catch (e: Exception) {
                Log.e("SupplyRepository", "API call failed for supply batch ID $id: ${e.message}")

                // If we have stale cache, return it as fallback
                if (localDataSource.hasSupplyDetailCache(id)) {
                    Log.d(
                        "SupplyRepository",
                        "Returning stale cached supply batch detail as fallback for ID: $id"
                    )
                    val cachedDetail = localDataSource.getCachedSupplyDetail(id)
                    if (cachedDetail != null) {
                        return cachedDetail
                    }
                }

                // No cache available, propagate the error
                throw e
            }
        }
    }
}
