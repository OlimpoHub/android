package com.app.arcabyolimpo.data.repository.beneficiaries

import com.app.arcabyolimpo.data.mapper.beneficiaries.toDomain
import com.app.arcabyolimpo.data.mapper.supplies.toDomain
import com.app.arcabyolimpo.data.mapper.workshops.toDomain
import com.app.arcabyolimpo.data.remote.api.ArcaApi
import com.app.arcabyolimpo.data.remote.dto.beneficiaries.BeneficiaryDto
import com.app.arcabyolimpo.domain.model.beneficiaries.Beneficiary
import com.app.arcabyolimpo.domain.repository.beneficiaries.BeneficiaryRepository
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
    @Inject
    constructor(
        private val api: ArcaApi,
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
                val fullName = listOfNotNull(
                    dto.firstName,
                    dto.paternalName,
                    dto.maternalName
                ).joinToString(" ").trim()

                Beneficiary(
                    id = dto.id.orEmpty(),
                    name = fullName.ifEmpty{ "Nombre no disponible" },
                    birthdate = "",
                    emergencyNumber = "",
                    emergencyName = "",
                    emergencyRelation = "",
                    details = "",
                    entryDate = "",
                    image = dto.image.orEmpty(),
                    disabilities = "",
                    status = 0,
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
        override suspend fun getBeneficiaryById(id: String): Beneficiary = api.getBeneficiary(id).toDomain()

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

        /**
         * Retrieves a list of beneficiaries matching the search query.
         *
         * This function calls the API's search endpoint and maps the resulting
         * list of [BeneficiaryDto] objects to domain [Beneficiary] models
         * using the existing [toDomain] mapper
         *
         * @param query The search term
         * @return A list of [Beneficiary] domain models.
         */
        override suspend fun searchBeneficiaries(query: String): List<Beneficiary> {
            return try {
                val dtoList = api.searchBeneficiaries(query)
                dtoList.map { it.toDomain() }
            } catch (e: HttpException) {
                throw e
            } catch (e: IOException) {
                throw e
            }
        }
}
