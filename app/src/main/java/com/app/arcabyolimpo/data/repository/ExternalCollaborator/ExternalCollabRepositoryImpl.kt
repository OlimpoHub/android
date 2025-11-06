package com.app.arcabyolimpo.data.repository.ExternalCollaborator

import com.app.arcabyolimpo.data.mapper.ExternalCollaborator.toDomain
import com.app.arcabyolimpo.data.mapper.ExternalCollaborator.toDto
import com.app.arcabyolimpo.data.remote.api.ArcaApi
import com.app.arcabyolimpo.domain.model.ExternalCollaborator.ExternalCollab
import com.app.arcabyolimpo.domain.repository.ExternalCollabRepository.ExternalCollabRepository
import javax.inject.Inject

class ExternalCollabRepositoryImpl @Inject constructor(
    private val api: ArcaApi
) : ExternalCollabRepository {

    override suspend fun getAllCollabs(): Result<List<ExternalCollab>> {
        return try {
            val response = api.getAllCollabs()
            Result.success(response.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCollabById(id: String): Result<ExternalCollab> {
        return try {
            val response = api.getCollabById(id)
            if (response.isNotEmpty()) {
                Result.success(response.first().toDomain())
            } else {
                Result.failure(Exception("Collaborator not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun registerCollab(collab: ExternalCollab): Result<ExternalCollab> {
        return try {
            val response = api.registerCollab(collab.toDto())
            Result.success(response.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateCollab(collab: ExternalCollab): Result<ExternalCollab> {
        return try {
            val response = api.updateCollab(collab.toDto())
            Result.success(response.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteCollab(id: Int): Result<Boolean> {
        return try {
            api.deleteCollab(mapOf("id" to id))
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}