package com.app.arcabyolimpo.domain.usecase.beneficiaries

import com.app.arcabyolimpo.data.remote.dto.filter.FilterDto
import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.model.beneficiaries.Beneficiary
import com.app.arcabyolimpo.domain.model.supplies.Supply
import com.app.arcabyolimpo.domain.repository.beneficiaries.BeneficiaryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Use case responsible for filtering supplies based on the provided parameters.
 *
 * This class interacts with the [SupplyRepository] to request filtered supplies
 * from the data layer according to the filters and order defined in [FilterDto].
 * It exposes both a suspend function for direct calls and an `invoke` operator
 * that returns a [Flow] to handle asynchronous UI state updates (Loading, Success, Error).
 *
 * @property repository The repository used to fetch filtered supplies from the data source.
 */
class FilterBeneficiariesUseCase
    @Inject
    constructor(
        private val repository: BeneficiaryRepository,
    ) {
        suspend fun filterBeneficiary(params: FilterDto): List<Beneficiary> = repository.filterBeneficiary(params)

        operator fun invoke(params: FilterDto): Flow<com.app.arcabyolimpo.domain.common.Result<List<Beneficiary>>> =
            flow {
                try {
                    emit(com.app.arcabyolimpo.domain.common.Result.Loading)
                    val supplies = repository.filterBeneficiary(params)
                    emit(
                        com.app.arcabyolimpo.domain.common.Result
                            .Success(supplies),
                    )
                } catch (e: Exception) {
                    emit(Result.Error(e))
                }
            }
    }
