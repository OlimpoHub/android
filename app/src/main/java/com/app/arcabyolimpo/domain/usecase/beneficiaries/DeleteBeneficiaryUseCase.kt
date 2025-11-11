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
 * Caso de Uso para inactivar (soft delete) un beneficiario.
 *
 * Inyecta [BeneficiaryRepository] para comunicarse con la capa de datos.
 */
class DeleteBeneficiaryUseCase @Inject constructor(
    private val repository: BeneficiaryRepository
) {
    /**
     * Sobrecarga el operador 'invoke' para que la clase pueda ser llamada como una funcion.
     * @param id El ID del beneficiario a inactivar.
     * @return Un Flow que emite el estado de la operacion.
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