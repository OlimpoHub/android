package com.app.arcabyolimpo.data.repository.beneficiaries

import com.app.arcabyolimpo.data.mapper.beneficiaries.toDomain
import com.app.arcabyolimpo.data.remote.api.ArcaApi
import com.app.arcabyolimpo.domain.model.beneficiaries.Beneficiary
import com.app.arcabyolimpo.domain.repository.beneficiaries.BeneficiaryRepository
import javax.inject.Inject

class BeneficiaryRepositoryImpl
    @Inject constructor(
        private val api: ArcaApi
    ) : BeneficiaryRepository {
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
    override suspend fun getBeneficiaryById(id: String): Beneficiary {
        return api.getBeneficiary(id).toDomain()
    }
}