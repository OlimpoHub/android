package com.app.arcabyolimpo.data.remote.api

import com.app.arcabyolimpo.data.remote.dto.auth.LoginRequestDto
import com.app.arcabyolimpo.data.remote.dto.auth.LoginResponseDto
import com.app.arcabyolimpo.data.remote.dto.auth.RefreshRequestDto
import com.app.arcabyolimpo.data.remote.dto.auth.RefreshResponseDto
import com.app.arcabyolimpo.data.remote.dto.beneficiaries.AddNewBeneficiaryDto
import com.app.arcabyolimpo.data.remote.dto.beneficiaries.BeneficiariesListDto
import com.app.arcabyolimpo.data.remote.dto.beneficiaries.BeneficiaryDto
import com.app.arcabyolimpo.data.remote.dto.filter.FilterDto
import com.app.arcabyolimpo.data.remote.dto.password.RecoverPasswordDto
import com.app.arcabyolimpo.data.remote.dto.password.RecoverPasswordResponseDto
import com.app.arcabyolimpo.data.remote.dto.password.UpdatePasswordDto
import com.app.arcabyolimpo.data.remote.dto.password.UpdatePasswordResponseDto
import com.app.arcabyolimpo.data.remote.dto.password.VerifyTokenResponseDto
import com.app.arcabyolimpo.data.remote.dto.supplies.DeleteDto
import com.app.arcabyolimpo.data.remote.dto.supplies.DeleteResponseDto
import com.app.arcabyolimpo.data.remote.dto.supplies.GetFiltersDto
import com.app.arcabyolimpo.data.remote.dto.supplies.SuppliesListDto
import com.app.arcabyolimpo.data.remote.dto.supplies.SupplyBatchDto
import com.app.arcabyolimpo.data.remote.dto.supplies.SupplyDto
import com.app.arcabyolimpo.data.remote.dto.supplies.WorkshopCategoryListDto
import com.app.arcabyolimpo.data.remote.dto.user.UserDto
import com.app.arcabyolimpo.data.remote.dto.user.registeruser.RegisterResponseDto
import com.app.arcabyolimpo.data.remote.dto.user.registeruser.RegisterUserDto
import com.app.arcabyolimpo.data.remote.dto.workshops.AddNewWorkshopDto
import com.app.arcabyolimpo.data.remote.dto.workshops.WorkshopDto
import com.app.arcabyolimpo.data.remote.dto.workshops.WorkshopsListDto
import com.app.arcabyolimpo.domain.model.supplies.SupplyBatchExt
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
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

    @GET("user/{id}")
    suspend fun getUserById(
        @Path("id") id: String,
    ): List<UserDto>

    @POST("user/register")
    suspend fun registerUser(
        @Body user: RegisterUserDto,
    ): UserDto

    @POST("user/update")
    suspend fun updateUser(
        @Body user: UserDto,
    ): RegisterResponseDto

    @POST("user/delete/{id}")
    suspend fun deleteUser(
        @Path("id") id: String,
    ): Map<String, Any>

    @POST("user/recover-password")
    suspend fun recoverPassword(
        @Body request: RecoverPasswordDto,
    ): Response<RecoverPasswordResponseDto>

    @GET("user/verify-token")
    suspend fun verifyToken(
        @Query("token") token: String,
    ): Response<VerifyTokenResponseDto>

    @POST("user/update-password")
    suspend fun updatePassword(
        @Body request: UpdatePasswordDto,
    ): Response<UpdatePasswordResponseDto>

    @GET("user")
    suspend fun getAllUsers(): List<UserDto>

    // Supplies --------------------------
    @GET("supplies")
    suspend fun getSuppliesList(): List<SuppliesListDto>

    @GET("supplybatch/{id}")
    suspend fun getSupply(
        @Path("id") id: String,
    ): SupplyDto

    @POST("/supplies/filter")
    suspend fun filterSupplies(
        @Body params: FilterDto,
    ): List<SuppliesListDto>

    @GET("supplies/filter/data")
    suspend fun getFilterSupplies(): GetFiltersDto

    @GET("supplyBatch/{id}")
    suspend fun getSupplyBatchById(
        @Path("id") id: String,
    ): SupplyBatchDto

    @DELETE("supplyBatch/{id}")
    suspend fun deleteSupplyBatch(
        @Path("id") id: String,
    )

    // My route is a soft delete and an update
    @POST("supplies/delete")
    suspend fun deleteOneSupply(
        @Body requestBody: DeleteDto,
        // DeleteResponseDto es para la respuesta , para el snackbar
    ): DeleteResponseDto

    // Workshop ---------------------------
    @GET("workshop")
    suspend fun getWorkshopsList(): List<WorkshopsListDto>

    @GET("workshop/{id}")
    suspend fun getWorkshop(
        @Path("id") id: String,
    ): WorkshopDto

    @POST("workshop/add")
    suspend fun addWorkshop(
        @Body requestBody: WorkshopDto,
    ): AddNewWorkshopDto

    // Beneficiary -------------
    @GET("beneficiary/list")
    suspend fun getBeneficiariesList(): List<BeneficiariesListDto>

    @GET("beneficiary/{id}")
    suspend fun getBeneficiary(
        @Path("id") id: String,
    ): BeneficiaryDto

    @DELETE("beneficiary/{id}")
    suspend fun deleteBeneficiary(
        @Path("id") id: String,
    ): Response<Unit>

    @POST("beneficiary/create")
    suspend fun addBeneficiary(
        @Body requestBody: BeneficiaryDto,
    ): AddNewBeneficiaryDto

    @GET("supplies/workshop/category")
    suspend fun getWorkshopCategoryList(): WorkshopCategoryListDto

    @Multipart
    @POST("supplies/add")
    suspend fun addSupply(
        @Part("idTaller") idWorkshop: RequestBody,
        @Part("nombre") name: RequestBody,
        @Part("unidadMedida") measureUnit: RequestBody,
        @Part("idCategoria") idCategory: RequestBody,
        @Part("status") status: RequestBody,
        @Part imagenInsumo: MultipartBody.Part?
    )
}
