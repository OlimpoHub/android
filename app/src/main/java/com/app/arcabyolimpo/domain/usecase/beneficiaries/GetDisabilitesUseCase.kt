package com.app.arcabyolimpo.domain.usecase.beneficiaries

import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.model.filter.FilterData
import com.app.arcabyolimpo.domain.repository.beneficiaries.BeneficiaryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Use case responsible for fetching the available filter options for supplies.
 *
 * This use case:
 * 1. Emits a loading state while the data is being retrieved.
 * 2. Requests the filter configuration from the repository (categories, types, units, etc.).
 * 3. Emits the filter data wrapped in a Success result when the request completes.
 * 4. Emits an Error result if something goes wrong during the fetch.
 *
 * @property repository The data source used to retrieve filter-related information.
 *
 * @return A Flow that emits loading, success, or error states containing the filter data.
 */
class GetDisabilitesUseCase
    @Inject
    constructor(
        private val repository: BeneficiaryRepository,
    ) {
        operator fun invoke(): Flow<Result<FilterData>> =
            flow {
                try {
                    emit(Result.Loading)
                    val filter = repository.getDisabilitiesData()
                    emit(Result.Success(filter))
                } catch (e: Exception) {
                    emit(Result.Error(e))
                }
            }
    }
