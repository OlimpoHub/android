package com.app.arcabyolimpo.domain.usecase.workshops

import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.model.workshops.Workshop
import com.app.arcabyolimpo.domain.repository.workshops.WorkshopRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetWorkshopUseCase
    @Inject
    constructor(
        private val repository: WorkshopRepository
    ) {
    operator fun invoke(id: String): Flow<Result<Workshop>> =
        flow {
            try {
                emit(Result.Loading)
                val workshop = repository.getWorkshopsById(id)
                if (workshop != null) {
                    emit(Result.Success(workshop))
                }
                else{
                    emit(Result.Error(Exception("Taller no encontrado con ID: $id")))
                }


            } catch (e: Exception) {
                emit(Result.Error(e))
            }
        }
    }
