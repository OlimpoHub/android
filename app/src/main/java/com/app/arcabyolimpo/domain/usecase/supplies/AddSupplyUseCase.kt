package com.app.arcabyolimpo.domain.usecase.supplies

import android.net.Uri
import com.app.arcabyolimpo.domain.model.supplies.SupplyAdd
import com.app.arcabyolimpo.domain.repository.supplies.SupplyRepository
import javax.inject.Inject

class AddSupplyUseCase @Inject constructor(
    private val repository: SupplyRepository
) {
    suspend operator fun invoke(
        supply: SupplyAdd,
        image: Uri?
    ): Result<Unit> {
        if (supply.name.isBlank() || supply.idWorkshop.isBlank() || supply.idCategory.isBlank()) {
            return Result.failure<Unit>(Exception("Completa todos los campos"))
        }
        return repository.addSupply(supply, image)
    }
}