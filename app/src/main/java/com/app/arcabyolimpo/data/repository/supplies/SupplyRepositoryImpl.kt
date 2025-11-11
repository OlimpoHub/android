package com.app.arcabyolimpo.data.repository.supplies

import com.app.arcabyolimpo.data.mapper.supplies.toDomain
import com.app.arcabyolimpo.data.remote.api.ArcaApi
import com.app.arcabyolimpo.domain.model.supplies.Supply
import com.app.arcabyolimpo.domain.model.supplies.SupplyBatchExt
import com.app.arcabyolimpo.domain.repository.supplies.SupplyRepository
import javax.inject.Inject

/**
 * Implementation of [SupplyRepository] that manages data retrieval
 * related to supplies from the remote API.
 *
 * This class uses [ArcaApi] to fetch supply data and maps it into
 * domain models to maintain a clean separation between data and domain layers.
 */
class SupplyRepositoryImpl
@Inject
constructor(
    private val api: ArcaApi
) : SupplyRepository {

    /**
     * Retrieves a list of all available supplies from the API.
     *
     * Each item in the response is converted into a [Supply] domain model.
     * Since this endpoint provides limited data, some fields like
     * [Supply.unitMeasure] and [Supply.batch] are initialized as empty.
     *
     * @return A list of [Supply] objects representing the available supplies.
     */
    override suspend fun getSuppliesList(): List<Supply> {
        val response = api.getSuppliesList()
        return response.map { dto ->
            Supply(
                id = dto.id,
                name = dto.name,
                imageUrl = dto.image,
                unitMeasure = "",
                batch = emptyList()
            )
        }

    /**
     * Retrieves detailed information for a specific supply by its [id].
     *
     * This function calls the API to obtain the corresponding [SupplyDto]
     * and converts it into a [Supply] domain model using the [toDomain] mapper.
     *
     * @param id The unique identifier of the supply to retrieve.
     * @return A [Supply] object containing detailed supply information.
     */
    override suspend fun getSupplyById(id: String): Supply {
        return api.getSupply(id).toDomain()
    }
}
