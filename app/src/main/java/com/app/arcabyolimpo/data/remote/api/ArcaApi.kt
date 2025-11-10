package com.app.arcabyolimpo.data.remote.api

import com.app.arcabyolimpo.data.remote.dto.auth.LoginRequestDto
import com.app.arcabyolimpo.data.remote.dto.auth.LoginResponseDto
import com.app.arcabyolimpo.data.remote.dto.auth.RefreshRequestDto
import com.app.arcabyolimpo.data.remote.dto.auth.RefreshResponseDto
import com.app.arcabyolimpo.data.remote.dto.ExternalCollaborator.ExternalCollabDto
import com.app.arcabyolimpo.data.remote.dto.ExternalCollaborator.RegisterExtCollab.RegisterExternalCollabDto
import com.app.arcabyolimpo.data.remote.dto.ExternalCollaborator.RegisterExtCollab.RegisterResponseDto
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.*

/**
 * Defines the remote API endpoints.
 *
 * This interface is implemented automatically by Retrofit to handle
 * network communication with the backend API service.
 */
interface ArcaApi {
    @POST("user/login")
    suspend fun login(
        @Body request: LoginRequestDto,
    ): LoginResponseDto

    @POST("user/refresh")
    suspend fun refresh(
        @Body request: RefreshRequestDto,
    ): RefreshResponseDto

    @GET("externalCollabs/")
    suspend fun getAllCollabs(): List<ExternalCollabDto>

    @GET("externalCollabs/{id}")
    suspend fun getCollabById(@Path("id") id: String): List<ExternalCollabDto>

    @POST("externalCollabs/register")
    suspend fun registerCollab(@Body collab: RegisterExternalCollabDto): ExternalCollabDto

    @POST("externalCollabs/update")
    suspend fun updateCollab(@Body collab: ExternalCollabDto): RegisterResponseDto

    @POST("externalCollabs/deleteExternalCollab")
    suspend fun deleteCollab(@Body data: Map<String, Int>): Map<String, Any>
}
