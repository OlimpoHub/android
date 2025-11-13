package com.app.arcabyolimpo.domain.repository.beneficiaries

import com.app.arcabyolimpo.domain.model.beneficiaries.Beneficiary
import com.app.arcabyolimpo.domain.model.filter.FilterData

interface BeneficiaryRepository {
    suspend fun getBeneficiariesList(): List<Beneficiary>

    suspend fun getBeneficiaryById(id: String): Beneficiary

    suspend fun deleteBeneficiary(id: String)

    suspend fun getDisabilitiesData(): FilterData
}
