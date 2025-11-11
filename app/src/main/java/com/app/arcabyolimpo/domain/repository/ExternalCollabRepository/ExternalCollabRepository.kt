package com.app.arcabyolimpo.domain.repository.ExternalCollabRepository

import com.app.arcabyolimpo.domain.model.ExternalCollaborator.ExternalCollab

interface ExternalCollabRepository {
    suspend fun getAllCollabs(): Result<List<ExternalCollab>>
    suspend fun getCollabById(id: String): Result<ExternalCollab>
    suspend fun registerCollab(collab: ExternalCollab): Result<ExternalCollab>
    suspend fun updateCollab(collab: ExternalCollab): Result<ExternalCollab>
    suspend fun deleteCollab(id: String): Result<Boolean>
}
