package com.app.arcabyolimpo.domain.usecase.beneficiaries

import com.app.arcabyolimpo.domain.model.beneficiaries.Beneficiary
import com.app.arcabyolimpo.domain.repository.beneficiaries.BeneficiaryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.app.arcabyolimpo.domain.common.Result
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * Use case to search beneficiaries by name.
 *
 * This class encapsulates the business logic of retrieving a list of beneficiaries
 * that match a specific search query, delegating the data retrieval
 * to the [BeneficiaryRepository].
 */
class SearchBeneficiariesUseCase @Inject constructor(
    private val repository: BeneficiaryRepository,
) {
    /**
     * Invokes the use case.
     *
     * @param query The search query.
     * @return A list of matching [Beneficiary] models.
     */
    operator fun invoke(query: String): Flow<Result<List<Beneficiary>>> = flow {
        try {
            emit(Result.Loading)
            val beneficiaries = repository.searchBeneficiaries(query)
            emit(Result.Success(beneficiaries))
        } catch (e: HttpException) {
            emit(Result.Error(e))
        } catch (e: IOException) {
            emit(Result.Error(e))
        }
    }
}