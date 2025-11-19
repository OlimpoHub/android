package com.app.arcabyolimpo.data.repository.supplies

import android.content.Context
import android.net.Uri
import android.util.Log
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
import kotlin.String

/**
 * Retrieves detailed information for a specific supply by its [id].
 *
 * This function calls the API to obtain the corresponding [SupplyDto]
 * and converts it into a [Supply] domain model using the [toDomain] mapper.
 *
 * @param id The unique identifier of the supply to retrieve.
 * @return A [Supply] object containing detailed supply information.
 */

@Singleton
class SupplyRepositoryImpl
    @Inject
    constructor(
        private val api: ArcaApi,
        @ApplicationContext private val context: Context,
    ) : SupplyRepository {
        override suspend fun getSuppliesList(): List<Supply> {
            val response = api.getSuppliesList()
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

        /** -------------------------------------------------------------------------------------- *
         * getSupplyBatchById -> calls the API to fetch a supply batch by its ID.
         * --------------------------------------------------------------------------------------- */
        override suspend fun getSupplyBatchById(id: String): SupplyBatchExt = api.getSupplyBatchById(id).toDomain()

        /**
         * Fetch a single inventory batch by its inventory id and map to domain RegisterSupplyBatch.
         */
        override suspend fun getSupplyBatchOne(id: String): RegisterSupplyBatch {
            val dto = api.getSupplyBatchOne(id)
            return dto.toRegister()
        }

        /**
         * Sends a filter request to the API and maps the response to a list of [Batch].
         *
         * @param idSupply The ID of the supply being filtered.
         * @param filters The filter data selected by the user.
         * @return A list of filtered batches.
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
         * Fetches available filter metadata and maps it to [FilterData].
         *
         * @return The available filter categories and values.
         */
        override suspend fun getFilterBatchData(): FilterData = api.getFilterSupplyBatch().toDomain()

        /**
         * Register a new supply batch in the backend.
         *
         * Steps performed:
         * 1. Map the domain model [RegisterSupplyBatch] into the DTO expected by the API
         *    ([RegisterSupplyBatchDto]). The DTO uses the JSON keys required by the
         *    backend (see RegisterSupplyBatchDto @SerializedName annotations).
         * 2. Call the Retrofit API `api.registerSupplyBatch(dto)` to create the batch.
         * 3. Convert the response DTO into the domain model and return it.
         */
        override suspend fun registerSupplyBatch(batch: RegisterSupplyBatch): SuccessMessage {
            // Build DTO matching API contract
            val dto =
                RegisterSupplyBatchDto(
                    supplyId = batch.supplyId,
                    quantity = batch.quantity,
                    expirationDate = batch.expirationDate,
                    acquisition = batch.acquisition,
                    boughtDate = batch.boughtDate,
                )

            // Execute network request to register the batch
            val responseDto = api.registerSupplyBatch(dto)

            // Convert response DTO into domain model and return it
            return responseDto.toDomain()
        }

        /**
         * Retrieves the list of acquisition types.
         *
         * @return A list of [Acquisition] objects representing different acquisition types.
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
         * Deletes a specific supply batch identified by its expiration date.
         *
         * This function calls the remote API to perform the deletion operation.
         *
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
            val result = api.deleteSupplyBatch(body)
        }

        /**
         * Performs a soft delete of a supply identified by its [id].
         *
         * This method builds the [DeleteDto] request body and calls the
         * corresponding API endpoint (`supplies/delete`).
         *
         * @param id Unique identifier of the supply to be soft-deleted.
         * @throws Exception If the network request fails or the server
         *         returns an error.
         */
        override suspend fun deleteOneSupply(id: String) {
            // Build the request body with the supply ID expected by the API
            val body = DeleteDto(id)
            // If you want to check the status of an error
            // val body = DeleteDto("i33")
            // Call the remote API to perform the soft delete operation
            val result = api.deleteOneSupply(body)
            Log.d("Validacion", "Si llego ")
        }

        /** -------------------------------------------------------------------------------------- *
         * getWorkshopCategoryList -> calls the API to fetch every workshop and category id's and
         * names.
         * -------------------------------------------------------------------------------------- */
        override suspend fun getWorkshopCategoryList(): Result<WorkshopCategoryList> =
            try {
                val workshopCategoryListDto = api.getWorkshopCategoryList()
                Result.success(workshopCategoryListDto.toDomain())
            } catch (e: Exception) {
                Result.failure(e)
            }

        /** -------------------------------------------------------------------------------------- *
         * addSupply -> calls the API to add a new Supply and handles the image to rename it
         * -------------------------------------------------------------------------------------- */
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
                // 1. Capturamos errores HTTP (400, 404, 500)
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

        /**
         *
         */
        override suspend fun supplyBatchList(
            expirationDate: String,
            idSupply: String,
        ): SupplyBatchList {
            Log.d("SupplyRepo", "supplyBatchList request expirationDate=$expirationDate idSupply=$idSupply")
            val response = api.supplyBatchList(expirationDate, idSupply)
            // Retrofit now returns a flat list of SupplyBatchItemDto for this endpoint.
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
