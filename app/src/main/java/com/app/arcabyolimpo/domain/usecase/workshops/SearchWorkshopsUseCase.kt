package com.app.arcabyolimpo.domain.usecase.workshops

import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.model.workshops.Workshop
import com.app.arcabyolimpo.domain.repository.workshops.WorkshopRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Use case responsible for searching workshops by name.
 *
 * This class attempts to retrieve workshops that match the search query,
 * first using the backend and falling back to a local search if needed.
 * It exposes state changes using a [Result] flow to allow the UI to react
 * to loading, success, or error events.
 *
 * [Result.Loading] while the search is in progress.
 * [Result.Success] when results (remote or local) are found.
 * [Result.Error] if an exception occurs during both remote and fallback search.
 *
 * @param name Name or partial name to search for.
 * @return A [Flow] emitting the list of matching [Workshop] objects.
 */
class SearchWorkshopsUseCase
@Inject
constructor(
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
