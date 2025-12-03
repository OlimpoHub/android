package com.app.arcabyolimpo.domain.usecase.disabilities

import com.app.arcabyolimpo.domain.common.AppError
import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.repository.disability.DisabilityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class DeleteDisabilityUseCase @Inject constructor(
private val repository: DisabilityRepository
) {
    operator fun invoke(id: String): Flow<com.app.arcabyolimpo.domain.common.Result<Unit>> = flow {
        try {
            emit(com.app.arcabyolimpo.domain.common.Result.Loading)
            repository.deleteDisability(id)
            emit(com.app.arcabyolimpo.domain.common.Result.Success(Unit))

        } catch (e: HttpException) {
            emit(com.app.arcabyolimpo.domain.common.Result.Error(AppError.ServerError()))

        } catch (e: IOException) {
            emit(com.app.arcabyolimpo.domain.common.Result.Error(AppError.NetworkError()))

        } catch (e: Exception) {
            emit(Result.Error(AppError.Unknown(e.message ?: "Error desconocido")))
        }
    }
}