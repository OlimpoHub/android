package com.app.arcabyolimpo.data.repository.beneficiaries

import com.app.arcabyolimpo.data.mapper.beneficiaries.toDomain
import com.app.arcabyolimpo.data.mapper.supplies.toDomain
import com.app.arcabyolimpo.data.mapper.workshops.toDomain
import com.app.arcabyolimpo.data.remote.api.ArcaApi
import com.app.arcabyolimpo.domain.model.beneficiaries.Beneficiary
import com.app.arcabyolimpo.domain.repository.beneficiaries.BeneficiaryRepository
import com.app.arcabyolimpo.data.remote.dto.beneficiaries.BeneficiaryDto
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * Implementation of [BeneficiaryRepository] that manages data retrieval
 * related to beneficiaries from the remote API.
 *
 * This class uses [ArcaApi] to fetch beneficiary data and maps it into
 * domain models to maintain a clean separation between data and domain layers.
 */
class BeneficiaryRepositoryImpl
    @Inject constructor(
        private val api: ArcaApi
    ) : BeneficiaryRepository {

     /**
      * Retrieves a list of all available beneficiaries from the API.
      *
      * Each item in the response is converted into a [Beneficiary] domain model.
      * Since this endpoint provides limited data, some fields like
      * [Beneficiary.status] or [Beneficiary.birthdate] are initialized as empty.
      *
      * @return A list of [Beneficiary] objects representing the available supplies.
      */
        override suspend fun getBeneficiariesList(): List<Beneficiary> {
            val response = api.getBeneficiariesList()
            return response.map { dto ->
                Beneficiary(
                    id = dto.id,
                    name = dto.name,
                    birthdate = "",
                    emergencyNumber = "",
                    emergencyName = "",
                    emergencyRelation = "",
                    details = "",
                    entryDate = "",
                    image = dto.image,
                    disabilities = "",
                    status = 0
                )
            }
        }
    /**
     * Retrieves detailed information for a specific beneficiary by its [id].
     *
     * This function calls the API to obtain the corresponding [BeneficiaryDto]
     * and converts it into a [Beneficiary] domain model using the [toDomain] mapper.
     *
     * @param id The unique identifier of the beneficiary to retrieve.
     * @return A [Beneficiary] object containing detailed workshop information.
     */
        override suspend fun getBeneficiaryById(id: String): Beneficiary {
            return api.getBeneficiary(id).toDomain()
        }
    /**
     * Function that calls the API to soft delete a specific beneficiary by its [id].
     *
     * @param id The unique identifier of the beneficiary to delete.
     *
     */
        override suspend fun deleteBeneficiary(id: String) {
            try {
                val response = api.deleteBeneficiary(id)
                if (!response.isSuccessful) {
                    throw HttpException(response)
                }
            } catch (e: HttpException) {
                throw e
            } catch (e: IOException) {
                throw e
            }
        }
}