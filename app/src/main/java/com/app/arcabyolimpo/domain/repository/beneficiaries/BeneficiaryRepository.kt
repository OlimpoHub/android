package com.app.arcabyolimpo.domain.repository.beneficiaries

import com.app.arcabyolimpo.data.remote.dto.filter.FilterDto
import com.app.arcabyolimpo.data.remote.dto.beneficiaries.BeneficiaryDto
import com.app.arcabyolimpo.domain.model.beneficiaries.Beneficiary
import com.app.arcabyolimpo.domain.model.filter.FilterData

interface BeneficiaryRepository {
    suspend fun getBeneficiariesList(): List<Beneficiary>

    suspend fun getBeneficiaryById(id: String): Beneficiary

    suspend fun deleteBeneficiary(id: String)

    suspend fun getDisabilitiesData(): FilterData

    suspend fun filterBeneficiary(params: FilterDto): List<Beneficiary>
    suspend fun searchBeneficiaries(query: String): List<Beneficiary>

    suspend fun addBeneficiary(newBeneficiary: BeneficiaryDto): Beneficiary
}
