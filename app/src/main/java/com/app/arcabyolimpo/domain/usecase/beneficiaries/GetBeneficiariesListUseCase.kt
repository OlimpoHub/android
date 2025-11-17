package com.app.arcabyolimpo.domain.usecase.beneficiaries

import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.model.beneficiaries.Beneficiary
import com.app.arcabyolimpo.domain.repository.beneficiaries.BeneficiaryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetBeneficiariesListUseCase
    @Inject
    constructor(
        private val repository: BeneficiaryRepository
    ) {
    operator fun invoke(): Flow<Result<List<Beneficiary>>> =
        flow {
            try {
                emit(Result.Loading)
                val beneficiaries = repository.getBeneficiariesList()
                emit(Result.Success(beneficiaries))
            } catch (e: HttpException) {
                emit(Result.Error(e))
            } catch (e: IOException) {
                emit(Result.Error(e))
            }
        }
}