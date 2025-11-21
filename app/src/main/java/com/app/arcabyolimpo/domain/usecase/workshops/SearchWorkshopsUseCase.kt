package com.app.arcabyolimpo.domain.usecase.workshops

import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.model.workshops.Workshop
import com.app.arcabyolimpo.domain.repository.workshops.WorkshopRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchWorkshopsUseCase @Inject constructor(
    private val repository: WorkshopRepository
) {
    operator fun invoke(name: String): Flow<Result<List<Workshop>>> = flow {
        emit(Result.Loading)

        if (name.isBlank()) {
            emit(Result.Success(emptyList()))
            return@flow
        }

        try {
            val workshops = repository.searchWorkshop(name)

            if (workshops.isEmpty()) {
                val allWorkshops = repository.getWorkshopsList()
                val normalizedSearch = name.normalizeText()
                val localResults = allWorkshops.filter { workshop ->
                    workshop.nameWorkshop?.normalizeText()?.contains(normalizedSearch, ignoreCase = true) == true
                }
                emit(Result.Success(localResults))
            } else {
                emit(Result.Success(workshops))
            }
        } catch (e: Exception) {
            try {
                if (name.isNotBlank()) {
                    val allWorkshops = repository.getWorkshopsList()
                    val normalizedSearch = name.normalizeText()
                    val localResults = allWorkshops.filter { workshop ->
                        workshop.nameWorkshop?.normalizeText()?.contains(normalizedSearch, ignoreCase = true) == true
                    }
                    emit(Result.Success(localResults))
                } else {
                    emit(Result.Success(emptyList()))
                }
            } catch (fallbackError: Exception) {
                emit(Result.Error(e))
            }
        }
    }
}
private fun String.normalizeText(): String {
    return this.lowercase()
        .replace("á", "a")
        .replace("é", "e")
        .replace("í", "i")
        .replace("ó", "o")
        .replace("ú", "u")
        .replace("ñ", "n")
        .replace("ü", "u")
}
