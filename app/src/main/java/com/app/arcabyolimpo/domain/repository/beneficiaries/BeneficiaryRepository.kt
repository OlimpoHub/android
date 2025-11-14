package com.app.arcabyolimpo.domain.repository.beneficiaries

import com.app.arcabyolimpo.data.remote.dto.beneficiaries.BeneficiaryDto
import com.app.arcabyolimpo.domain.model.beneficiaries.Beneficiary

interface BeneficiaryRepository{
    suspend fun getBeneficiariesList(): List<Beneficiary>

    suspend fun getBeneficiaryById(id: String): Beneficiary

    suspend fun deleteBeneficiary(id: String)

    suspend fun addBeneficiary(newBeneficiary: BeneficiaryDto): Beneficiary
}
