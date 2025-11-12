package com.app.arcabyolimpo.data.repository.supplies

import com.app.arcabyolimpo.data.mapper.supplies.toDomain
import com.app.arcabyolimpo.data.remote.api.ArcaApi
import com.app.arcabyolimpo.data.remote.dto.filter.FilterDto
import com.app.arcabyolimpo.data.remote.dto.supplies.DeleteDto
import com.app.arcabyolimpo.domain.model.filter.FilterData
import com.app.arcabyolimpo.domain.model.supplies.Batch
import com.app.arcabyolimpo.domain.model.supplies.Supply
import com.app.arcabyolimpo.domain.model.supplies.SupplyBatchExt
import com.app.arcabyolimpo.domain.repository.supplies.SupplyRepository
import javax.inject.Inject
import javax.inject.Singleton

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
class SupplyRepositoryImpl @Inject constructor(
        private val api: ArcaApi,
    ) : SupplyRepository {
        override suspend fun getSuppliesList(): List<Supply> {
            val response = api.getSuppliesList()
            return response.map { dto ->
                Supply(
                    id = dto.id,
                    name = dto.name,
                    imageUrl = dto.image,
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
                    imageUrl = dto.image,
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
         * Deletes a specific supply batch identified by its [id].
         *
         * This function calls the remote API to perform the deletion operation.
         * It does not return any data, but will throw an exception if the request fails.
         */
        override suspend fun deleteSupplyBatch(id: String){
            api.deleteSupplyBatch(id)
        }


    // yo hago un Update
        override suspend fun deleteOneSupply(id: String) {
            //Si no devuelvo un valor, ya no pongo el reponse
            val body = DeleteDto(id)
            api.deleteOneSupply(body)
        }
    }
