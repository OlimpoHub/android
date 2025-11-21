package com.app.arcabyolimpo.domain.usecase.supplies

import android.net.Uri
import com.app.arcabyolimpo.domain.model.supplies.SupplyAdd
import com.app.arcabyolimpo.domain.repository.supplies.SupplyRepository
import java.lang.Exception
import javax.inject.Inject

class UpdateOneSupplyUseCase @Inject constructor(
    private val repository: SupplyRepository
) {
    suspend operator fun invoke(
        idSupply: String,
        supply: SupplyAdd,
        image: Uri?
    ): Result<Unit> {
        if (supply.name.isBlank()) {
            return Result.failure(Exception("El nombre no puede estar vacio"))
        }
        if (supply.idWorkshop.isBlank() || supply.idCategory.isBlank()) {
            return Result.failure(Exception("El taller y categoria son obligatorios"))
        }
        return repository.updateSupply(idSupply, supply, image)
    }
}