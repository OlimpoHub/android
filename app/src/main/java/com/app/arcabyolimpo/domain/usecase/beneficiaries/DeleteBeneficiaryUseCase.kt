package com.app.arcabyolimpo.domain.usecase.beneficiaries

import com.app.arcabyolimpo.domain.common.AppError
import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.repository.beneficiaries.BeneficiaryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * Use case to disable a beneficiary in the system.
 *
 * Injects [BeneficiaryRepository] to comunicate with the data cape.
 */
class DeleteBeneficiaryUseCase @Inject constructor(
    private val repository: BeneficiaryRepository
) {
    /**
     * Overload to operator 'invoke' so the class can be called as a function.
     * @param id The ID of the beneficiary to delete.
     * @return A Flow that emits the result of the operation.
     */
     operator fun invoke(id: String): Flow<Result<Unit>> = flow {
         try {
            emit(Result.Loading)
             repository.deleteBeneficiary(id)
             emit(Result.Success(Unit))

        } catch (e: HttpException) {
            emit(Result.Error(AppError.ServerError()))

        } catch (e: IOException) {
            emit(Result.Error(AppError.NetworkError()))

        } catch (e: Exception) {
            emit(Result.Error(AppError.Unknown(e.message ?: "Error desconocido")))
        }
    }
}