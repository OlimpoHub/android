package com.app.arcabyolimpo.domain.usecase.workshops

import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.model.workshops.Workshop
import com.app.arcabyolimpo.domain.repository.workshops.WorkshopRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetWorkshopsListUseCase
    @Inject
    constructor(
        private val repository: WorkshopRepository
    ) {
    operator fun invoke(): Flow<Result<List<Workshop>>> =
        flow {
            try {
                emit(Result.Loading)
                val workshops = repository.getWorkshopsList()
                emit(Result.Success(workshops))
            } catch (e: Exception) {
                emit(Result.Error(e))
            }
        }
    }