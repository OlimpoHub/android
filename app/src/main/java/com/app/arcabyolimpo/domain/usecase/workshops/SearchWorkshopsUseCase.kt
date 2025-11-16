package com.app.arcabyolimpo.domain.usecase.workshops

import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.model.workshops.Workshop
import com.app.arcabyolimpo.domain.repository.workshops.WorkshopRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchWorkshopsUseCase @Inject constructor(
    private val repository: WorkshopRepository
) {
    operator fun invoke(name: String): Flow<Result<List<Workshop>>> = flow {
        emit(Result.Loading)

        try {
            val workshops = repository.searchWorkshop(name)

            if (workshops.isEmpty() && name.isNotBlank()) {
                val allWorkshops = repository.getWorkshopsList()
                val localResults = allWorkshops.filter { workshop ->
                    workshop.nameWorkshop?.contains(name, ignoreCase = true) == true
                }
                emit(Result.Success(localResults))
            } else {
                emit(Result.Success(workshops))
            }
        } catch (e: Exception) {
            try {
                val allWorkshops = repository.getWorkshopsList()
                val localResults = allWorkshops.filter { workshop ->
                    workshop.nameWorkshop?.contains(name, ignoreCase = true) == true
                }
                emit(Result.Success(localResults))
            } catch (fallbackError: Exception) {
                emit(Result.Error(e))
            }
        }
    }
}
