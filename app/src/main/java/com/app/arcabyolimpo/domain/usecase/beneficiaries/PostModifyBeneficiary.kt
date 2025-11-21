package com.app.arcabyolimpo.domain.usecase.beneficiaries

import com.app.arcabyolimpo.data.remote.dto.beneficiaries.BeneficiaryDto
import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.model.beneficiaries.Beneficiary
import com.app.arcabyolimpo.domain.repository.beneficiaries.BeneficiaryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PostModifyBeneficiary @Inject constructor(
    private val repository: BeneficiaryRepository
){
    operator fun invoke(modifiedBeneficiary: BeneficiaryDto): Flow<com.app.arcabyolimpo.domain.common.Result<Beneficiary>> =
        flow {
            try {
                emit(com.app.arcabyolimpo.domain.common.Result.Loading)
                val beneficiary = repository.modifyBeneficiary(modifiedBeneficiary)
                emit(com.app.arcabyolimpo.domain.common.Result.Success(beneficiary))
            } catch (e: Exception) {
                emit(Result.Error(e))
            }
        }
}