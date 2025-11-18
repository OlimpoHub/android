package com.app.arcabyolimpo.domain.usecase.supplies

import android.net.Uri
import com.app.arcabyolimpo.domain.model.supplies.SupplyAdd
import com.app.arcabyolimpo.domain.repository.supplies.SupplyRepository
import javax.inject.Inject
/** ---------------------------------------------------------------------------------------------- *
 * AddSupplyUseCase -> Use case that receives the Supply and its image to send it to the db
 *
 * @param repository: SupplyRepository -> repository where the api calls is found
 * ---------------------------------------------------------------------------------------------- */
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