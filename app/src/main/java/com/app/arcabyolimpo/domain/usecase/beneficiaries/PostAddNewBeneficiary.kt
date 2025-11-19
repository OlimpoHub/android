package com.app.arcabyolimpo.domain.usecase.beneficiaries

import com.app.arcabyolimpo.data.remote.dto.beneficiaries.BeneficiaryDto
import com.app.arcabyolimpo.data.remote.dto.workshops.WorkshopDto
import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.model.beneficiaries.Beneficiary
import com.app.arcabyolimpo.domain.model.workshops.Workshop
import com.app.arcabyolimpo.domain.repository.beneficiaries.BeneficiaryRepository
import com.app.arcabyolimpo.domain.repository.workshops.WorkshopRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PostAddNewBeneficiary @Inject constructor(
    private val repository: BeneficiaryRepository
) {
    operator fun invoke(newBeneficiary: BeneficiaryDto): Flow<com.app.arcabyolimpo.domain.common.Result<Beneficiary>> =
        flow {
            try {
                emit(com.app.arcabyolimpo.domain.common.Result.Loading)
                val beneficiary = repository.addBeneficiary(newBeneficiary)
                emit(Result.Success(beneficiary) as Result<Beneficiary>)
            } catch (e: Exception) {
                emit(com.app.arcabyolimpo.domain.common.Result.Error(e))
            }
        }
}