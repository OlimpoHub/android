package com.app.arcabyolimpo.data.remote.api

import com.app.arcabyolimpo.data.remote.dto.auth.LoginRequestDto
import com.app.arcabyolimpo.data.remote.dto.auth.LoginResponseDto
import com.app.arcabyolimpo.data.remote.dto.password.RecoverPasswordDto
import com.app.arcabyolimpo.data.remote.dto.auth.RefreshRequestDto
import com.app.arcabyolimpo.data.remote.dto.auth.RefreshResponseDto
import com.app.arcabyolimpo.data.remote.dto.ExternalCollaborator.ExternalCollabDto
import com.app.arcabyolimpo.data.remote.dto.ExternalCollaborator.RegisterExtCollab.RegisterExternalCollabDto
import com.app.arcabyolimpo.data.remote.dto.ExternalCollaborator.RegisterExtCollab.RegisterResponseDto
import com.app.arcabyolimpo.data.remote.dto.supplies.SuppliesListDto
import com.app.arcabyolimpo.data.remote.dto.supplies.SupplyDto
import com.app.arcabyolimpo.data.remote.dto.password.RecoverPasswordResponseDto
import com.app.arcabyolimpo.data.remote.dto.password.UpdatePasswordDto
import com.app.arcabyolimpo.data.remote.dto.password.UpdatePasswordResponseDto
import com.app.arcabyolimpo.data.remote.dto.password.VerifyTokenDto
import com.app.arcabyolimpo.data.remote.dto.password.VerifyTokenResponseDto
import com.app.arcabyolimpo.data.remote.dto.workshops.AddNewWorkshopDto
import com.app.arcabyolimpo.data.remote.dto.workshops.WorkshopDto
import com.app.arcabyolimpo.data.remote.dto.workshops.WorkshopsListDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

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

    @POST("user/recover-password")
    suspend fun recoverPassword(
        @Body request: RecoverPasswordDto
    ): Response<RecoverPasswordResponseDto>

    @GET("user/verify-token")
    suspend fun verifyToken(
        @Query("token") token: String
    ): Response<VerifyTokenResponseDto>

    @POST("user/update-password")
    suspend fun updatePassword(
        @Body request: UpdatePasswordDto
    ): Response<UpdatePasswordResponseDto>

    @GET("supplies")
    suspend fun getSuppliesList(): List<SuppliesListDto>

    @GET("supplybatch/{id}")
    suspend fun getSupply(@Path("id") id: String): SupplyDto

    @GET("workshop")
    suspend fun getWorkshopsList(): List<WorkshopsListDto>

    @GET("workshop/{id}")
    suspend fun getWorkshop(@Path("id") id: String): WorkshopDto

    @POST("workshop/add")
    suspend fun addWorkshop(
        @Body requestBody: WorkshopDto
    ): AddNewWorkshopDto
}
