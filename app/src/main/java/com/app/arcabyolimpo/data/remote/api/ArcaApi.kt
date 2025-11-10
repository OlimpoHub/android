package com.app.arcabyolimpo.data.remote.api

import com.app.arcabyolimpo.data.remote.dto.auth.LoginRequestDto
import com.app.arcabyolimpo.data.remote.dto.auth.LoginResponseDto
import com.app.arcabyolimpo.data.remote.dto.auth.RefreshRequestDto
import com.app.arcabyolimpo.data.remote.dto.auth.RefreshResponseDto
import com.app.arcabyolimpo.data.remote.dto.password.RecoverPasswordDto
import com.app.arcabyolimpo.data.remote.dto.password.RecoverPasswordResponseDto
import com.app.arcabyolimpo.data.remote.dto.password.UpdatePasswordDto
import com.app.arcabyolimpo.data.remote.dto.password.UpdatePasswordResponseDto
import com.app.arcabyolimpo.data.remote.dto.password.VerifyTokenDto
import com.app.arcabyolimpo.data.remote.dto.password.VerifyTokenResponseDto
import com.app.arcabyolimpo.data.remote.dto.supplies.SuppliesListDto
import com.app.arcabyolimpo.data.remote.dto.supplies.SupplyDto
import okhttp3.Response
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

    @POST("user/recover-password")
    suspend fun recoverPassword(
        @Body request: RecoverPasswordDto,
    ): RecoverPasswordResponseDto

    @GET("user/verify-token")
    suspend fun verifyToken(
        @Query("token") token: String,
    ): VerifyTokenResponseDto

    @POST("user/update-password")
    suspend fun updatePassword(
        @Body request: UpdatePasswordDto,
    ): UpdatePasswordResponseDto

    @GET("supplies")
    suspend fun getSuppliesList(): List<SuppliesListDto>

    @GET("supplyBatch/{id}")
    suspend fun getSupply(
        @Path("id") id: String,
    ): SupplyDto
}
