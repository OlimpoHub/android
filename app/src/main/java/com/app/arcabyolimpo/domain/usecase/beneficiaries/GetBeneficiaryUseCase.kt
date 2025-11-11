package com.app.arcabyolimpo.domain.usecase.beneficiaries

import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.model.beneficiaries.Beneficiary
import com.app.arcabyolimpo.domain.repository.beneficiaries.BeneficiaryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetBeneficiaryUseCase
    @Inject
    constructor(
        private val repository: BeneficiaryRepository
    ) {
    operator fun invoke(id: String): Flow<Result<Beneficiary>> =
        flow {
            try {
                emit(Result.Loading)
                val beneficiary = repository.getBeneficiaryById(id)
                emit(Result.Success(beneficiary))
            } catch (e: Exception) {
                emit(Result.Error(e))
            }
        }
}