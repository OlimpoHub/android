package com.app.arcabyolimpo.domain.usecase.workshops

import android.util.Log
import com.app.arcabyolimpo.data.remote.dto.workshops.WorkshopDto
import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.model.workshops.Workshop
import com.app.arcabyolimpo.domain.repository.workshops.WorkshopRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PostModifyWorkshop @Inject constructor(
    private val repository: WorkshopRepository
){
    operator fun invoke(modifiedWorkshop: WorkshopDto): Flow<com.app.arcabyolimpo.domain.common.Result<Workshop>> =
        flow {
            try {
                emit(com.app.arcabyolimpo.domain.common.Result.Loading)
                Log.d("WORKSHOP_DEBUG", "Fecha: ${modifiedWorkshop}")
                val workshop = repository.modifyWorkshop(modifiedWorkshop)
                emit(com.app.arcabyolimpo.domain.common.Result.Success(workshop))
            } catch (e: Exception) {
                emit(Result.Error(e))
            }
        }
}