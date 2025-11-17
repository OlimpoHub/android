package com.app.arcabyolimpo.data.remote.api

import com.app.arcabyolimpo.data.remote.dto.auth.LoginRequestDto
import com.app.arcabyolimpo.data.remote.dto.auth.LoginResponseDto
import com.app.arcabyolimpo.data.remote.dto.auth.RefreshRequestDto
import com.app.arcabyolimpo.data.remote.dto.auth.RefreshResponseDto
import com.app.arcabyolimpo.data.remote.dto.beneficiaries.BeneficiariesListDto
import com.app.arcabyolimpo.data.remote.dto.beneficiaries.BeneficiaryDto
import com.app.arcabyolimpo.data.remote.dto.filter.FilterDto
import com.app.arcabyolimpo.data.remote.dto.password.RecoverPasswordDto
import com.app.arcabyolimpo.data.remote.dto.password.RecoverPasswordResponseDto
import com.app.arcabyolimpo.data.remote.dto.password.UpdatePasswordDto
import com.app.arcabyolimpo.data.remote.dto.password.UpdatePasswordResponseDto
import com.app.arcabyolimpo.data.remote.dto.password.VerifyTokenResponseDto
import com.app.arcabyolimpo.data.remote.dto.productbatches.ProductBatchDto
import com.app.arcabyolimpo.data.remote.dto.product.ProductDto
import com.app.arcabyolimpo.data.remote.dto.product.ProductRegisterInfoDto
import com.app.arcabyolimpo.data.remote.dto.productbatches.ProductBatchModifyDto
import com.app.arcabyolimpo.data.remote.dto.productbatches.ProductBatchRegisterDto
import com.app.arcabyolimpo.data.remote.dto.supplies.AcquisitionDto
import com.app.arcabyolimpo.data.remote.dto.supplies.DeleteDto
import com.app.arcabyolimpo.data.remote.dto.supplies.DeleteResponseDto
import com.app.arcabyolimpo.data.remote.dto.supplies.GetFiltersDto
import com.app.arcabyolimpo.data.remote.dto.supplies.RegisterSupplyBatchDto
import com.app.arcabyolimpo.data.remote.dto.supplies.SuccessMessageDto
import com.app.arcabyolimpo.data.remote.dto.supplies.SuppliesListDto
import com.app.arcabyolimpo.data.remote.dto.supplies.SupplyBatchDto
import com.app.arcabyolimpo.data.remote.dto.supplies.SupplyDto
import com.app.arcabyolimpo.data.remote.dto.supplies.WorkshopCategoryListDto
import com.app.arcabyolimpo.data.remote.dto.user.UserDto
import com.app.arcabyolimpo.data.remote.dto.user.registeruser.RegisterResponseDto
import com.app.arcabyolimpo.data.remote.dto.user.registeruser.RegisterUserDto
import com.app.arcabyolimpo.data.remote.dto.workshops.AddNewWorkshopDto
import com.app.arcabyolimpo.data.remote.dto.workshops.DeleteResponseWorkshopDto
import com.app.arcabyolimpo.data.remote.dto.workshops.DeleteWorkshopDto
import com.app.arcabyolimpo.data.remote.dto.workshops.WorkshopDto
import com.app.arcabyolimpo.data.remote.dto.workshops.WorkshopsListDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
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

    @POST("supplyBatch/addBatch")
    suspend fun registerSupplyBatch(
        @Body request: RegisterSupplyBatchDto,
    ): SuccessMessageDto

    @GET("supplyBatch/acquisition/types")
    suspend fun getAcquisitionTypes(): List<AcquisitionDto>

    @DELETE("supplyBatch/{id}")
    suspend fun deleteSupplyBatch(
        @Path("id") id: String,
    )

    /**
     * Deletes a single supply from the backend.
     *
     * This endpoint receives a [DeleteDto] with the information needed
     * to identify which supply should be removed ( its ID).
     *
     * @param requestBody Data transfer object that contains the supply
     * information required by the API to perform the delete operation.
     * @return [DeleteResponseDto] containing the result of the delete
     * operation, such as a success flag and/or a confirmation message.
     */
    @POST("supplies/delete")
    suspend fun deleteOneSupply(
        @Body requestBody: DeleteDto,
        // DeleteResponseDto is for the response, for the snackbar
    ): DeleteResponseDto

    // Workshop ---------------------------

    @GET("workshop")
    suspend fun getWorkshopsList(): List<WorkshopsListDto>

    @GET("workshop/search")
    suspend fun searchWorkshops(
        @Query("nameWorkshop") name: String,
    ): List<WorkshopDto>

    @GET("workshop/{id}")
    suspend fun getWorkshop(
        @Path("id") id: String,
    ): WorkshopDto

    @POST("workshop/add")
    suspend fun addWorkshop(
        @Body requestBody: WorkshopDto,
    ): AddNewWorkshopDto

    /**
     * Deletes a single Workshop from the backend.
     *
     * This endpoint receives a [DeleteWorkshopDto] with the information needed
     * to identify which supply should be removed ( its ID).
     *
     * @param requestBody Data transfer object that contains the supply
     * information required by the API to perform the delete operation.
     * @return [DeleteResponseWorkshopDto] containing the result of the delete
     * operation, such as a success flag and/or a confirmation message.
     */
    @POST("workshop/delete")
    suspend fun deleteWorkshops(
        @Body requestBody: DeleteWorkshopDto,
        // DeleteResponseDto is for the response, for the snackbar
    ): DeleteResponseWorkshopDto



    // Beneficiary -------------
    @GET("beneficiary/list")
    suspend fun getBeneficiariesList(): List<BeneficiariesListDto>

    @GET("beneficiary/{id}")
    suspend fun getBeneficiary(
        @Path("id") id: String,
    ): BeneficiaryDto

    /**
     * Makes a soft delete to the selected beneficiary.
     */
    @DELETE("beneficiary/{id}")
    suspend fun deleteBeneficiary(
        @Path("id") id: String,
    ): Response<Unit>

    @GET("beneficiary/search")
    suspend fun searchBeneficiaries(
        @Query("term") searchTerm: String,
    ): List<BeneficiaryDto>

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
        @Part imagenInsumo: MultipartBody.Part?,
    )

    @GET("productBatch/")
    suspend fun getProductBatches(): List<ProductBatchDto>

    @GET("productBatch/{id}")
    suspend fun getProductBatch(
        @Path("id") id: String,
    ): ProductBatchDto

    @POST("productBatch/")
    suspend fun addProductBatch(
        @Body batch: ProductBatchRegisterDto,
    )

    @PUT("productBatch/{id}")
    suspend fun modifyProductBatch(
        @Path("id") id: String,
        @Body batch: ProductBatchModifyDto,
    )

    // Products --------------------------

    @Multipart
    @POST("product/add")
    suspend fun addProduct(
        @Part("idTaller") idWorkshop: RequestBody,
        @Part("Nombre") name: RequestBody,
        @Part("PrecioUnitario") unitaryPrice: RequestBody,
        @Part("idCategoria") idCategory: RequestBody,
        @Part("Descripcion") description: RequestBody,
        @Part("Disponible") status: RequestBody,
        @Part image: MultipartBody.Part?,
    )
    @DELETE("product/{idProduct}")
    suspend fun deleteProduct(
        @Path("idProduct") idProduct: String,
    ): Response<Unit>

    @GET("product/")
    suspend fun getProducts(): List<ProductDto>

    @GET("product/search")
    suspend fun searchProducts(
        @Query("q") query: String,
    ): List<ProductDto>

    @GET("product/add")
    suspend fun getProductFilters(): ProductRegisterInfoDto

    @GET("product/disponible")
    suspend fun getProductsByAvailability(
        @Query("disponible") disponible: Int,
    ): List<ProductDto>


    @GET("product/workshop")
    suspend fun getProductsByWorkshop(
        @Query("idTaller") idTaller: String,
    ): List<ProductDto>

    @GET("product/order")
    suspend fun getProductsOrderedByPrice(
        @Query("orderBy") orderBy: String,
        @Query("direction") direction: String,
    ): List<ProductDto>
}
