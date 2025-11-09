package com.app.arcabyolimpo.domain.usecase.workshops

import com.app.arcabyolimpo.data.remote.dto.workshops.WorkshopDto
import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.model.workshops.Workshop
import com.app.arcabyolimpo.domain.repository.workshops.WorkshopRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PostAddNewWorkshop @Inject constructor(
    private val repository: WorkshopRepository
) {
    operator fun invoke(newWorkshop: WorkshopDto): Flow<Result<Workshop>> =
        flow {
            try {
                emit(Result.Loading)
                val workshop = repository.addWorkshop(newWorkshop)
                emit(Result.Success(workshop))
            } catch (e: Exception) {
                emit(Result.Error(e))
            }
        }
}