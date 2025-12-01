package com.app.arcabyolimpo.data.repository.supplies

import android.content.Context
import android.net.Uri
import android.util.Log
import com.app.arcabyolimpo.data.local.supplies.preferences.SupplyLocalDataSource
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

@Singleton
class SupplyRepositoryImpl
@Inject
constructor(
    private val api: ArcaApi,
    private val localDataSource: SupplyLocalDataSource,
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
        // Check if we have valid cached data
        if (localDataSource.isCacheValid()) {
            Log.d("SupplyRepository", "Returning valid cached supplies")
            return localDataSource.getCachedSuppliesList() ?: fetchAndCacheSupplies()
        }

        // Try to fetch fresh data from API
        return try {
            fetchAndCacheSupplies()
        } catch (e: Exception) {
            Log.e("SupplyRepository", "API call failed: ${e.message}")

            // If we have stale cache, return it as fallback
            if (localDataSource.hasCachedData()) {
                Log.d("SupplyRepository", "Returning stale cached supplies as fallback")
                localDataSource.getCachedSuppliesList() ?: throw e
            } else {
                // No cache available, propagate the error
                throw e
            }
        }
    }

    /**
     * Helper function to fetch supplies from API and cache them.
     */
    private suspend fun fetchAndCacheSupplies(): List<Supply> {
        Log.d("SupplyRepository", "Fetching supplies from API")
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

        // Cache the fresh data
        localDataSource.cacheSuppliesList(supplies)
        Log.d("SupplyRepository", "Cached ${supplies.size} supplies")

        return supplies
    }

    /**
     * Forces a refresh of the supplies list from the API.
     * Useful for pull-to-refresh functionality.
     */
    suspend fun refreshSuppliesList(): List<Supply> {
        return fetchAndCacheSupplies()
    }

    /**
     * Clears the local cache.
     * Useful for debugging or when data becomes corrupted.
     */
    fun clearCache() {
        localDataSource.clearCache()
    }

    override suspend fun getSupplyById(id: String): Supply = api.getSupply(id).toDomain()

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

    override suspend fun getFilterData(): FilterData = api.getFilterSupplies().toDomain()

    override suspend fun getSupplyBatchById(id: String): SupplyBatchExt {
        // Check if we have valid cached data for this specific supply
        if (localDataSource.isSupplyDetailCacheValid(id)) {
            Log.d("SupplyRepository", "Returning valid cached supply detail for ID: $id")
            return localDataSource.getCachedSupplyDetail(id)
                ?: fetchAndCacheSupplyDetail(id)
        }

        // Try to fetch fresh data from API
        return try {
            fetchAndCacheSupplyDetail(id)
        } catch (e: Exception) {
            Log.e("SupplyRepository", "API call failed for supply ID $id: ${e.message}")

            // If we have stale cache, return it as fallback
            if (localDataSource.hasSupplyDetailCache(id)) {
                Log.d("SupplyRepository", "Returning stale cached supply detail as fallback for ID: $id")
                localDataSource.getCachedSupplyDetail(id) ?: throw e
            } else {
                // No cache available, propagate the error
                throw e
            }
        }
    }

    /**
     * Helper function to fetch supply detail from API and cache it.
     */
    /**
     * Helper function to fetch supply detail from API and cache it.
     */
    private suspend fun fetchAndCacheSupplyDetail(id: String): SupplyBatchExt {
        Log.d("SupplyRepository", "=== fetchAndCacheSupplyDetail called for ID: $id ===")

        try {
            Log.d("SupplyRepository", "Calling API getSupplyBatchById...")
            val supplyDetail = api.getSupplyBatchById(id).toDomain()

            Log.d("SupplyRepository", "✅ API returned data for: ${supplyDetail.name}")
            Log.d("SupplyRepository", "Supply has ${supplyDetail.batch.size} batches")

            // Cache the fresh data
            Log.d("SupplyRepository", "Now caching the supply detail...")
            localDataSource.cacheSupplyDetail(supplyDetail)
            Log.d("SupplyRepository", "✅ Cache operation completed")

            return supplyDetail
        } catch (e: Exception) {
            Log.e("SupplyRepository", "❌ Error in fetchAndCacheSupplyDetail: ${e.message}")
            e.printStackTrace()
            throw e
        }
    }

    /**
     * Forces a refresh of a specific supply detail from the API.
     * Useful for pull-to-refresh functionality.
     */
    suspend fun refreshSupplyDetail(id: String): SupplyBatchExt {
        return fetchAndCacheSupplyDetail(id)
    }

    /**
     * Clears the cache for a specific supply detail.
     */
    fun clearSupplyDetailCache(id: String) {
        localDataSource.clearSupplyDetail(id)
    }

    override suspend fun getSupplyBatchOne(id: String): RegisterSupplyBatch {
        val dto = api.getSupplyBatchOne(id)
        return dto.toRegister()
    }

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

    override suspend fun getFilterBatchData(): FilterData = api.getFilterSupplyBatch().toDomain()

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
        return responseDto.toDomain()
    }

    override suspend fun getAcquisitionTypes(): List<Acquisition> {
        val response = api.getAcquisitionTypes()
        return response.map { dto ->
            Acquisition(
                id = dto.id,
                description = dto.description,
            )
        }
    }

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
        val result = api.deleteSupplyBatch(body)
    }

    override suspend fun deleteOneSupply(id: String) {
        val body = DeleteDto(id)
        val result = api.deleteOneSupply(body)
        Log.d("Validacion", "Si llego ")
    }

    override suspend fun getWorkshopCategoryList(): Result<WorkshopCategoryList> =
        try {
            val workshopCategoryListDto = api.getWorkshopCategoryList()
            Result.success(workshopCategoryListDto.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }

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
        return responseDto.toDomain()
    }

    override suspend fun supplyBatchList(
        expirationDate: String,
        idSupply: String,
    ): SupplyBatchList {
        Log.d("SupplyRepo", "supplyBatchList request expirationDate=$expirationDate idSupply=$idSupply")
        val response = api.supplyBatchList(expirationDate, idSupply)
        val combinedItems = response
        try {
            Log.d(
                "SupplyRepo",
                "api returned responseSize=${response.size}, combinedItems=${combinedItems.size}, ids=${combinedItems.joinToString {
                    it.id
                }}",
            )
        } catch (t: Throwable) {
            Log.d("SupplyRepo", "api returned responseSize=${response.size}, combinedItems=${combinedItems.size}")
        }
        return SupplyBatchList(batch = combinedItems.map { it.toDomain() })
    }
}