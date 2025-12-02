package com.app.arcabyolimpo.data.repository.product

import android.content.Context
import android.util.Log
import com.app.arcabyolimpo.data.local.product.detail.preferences.ProductDetailPreferences
import com.app.arcabyolimpo.data.local.product.list.preferences.ProductPreferences
import com.app.arcabyolimpo.data.mapper.product.toDomain
import com.app.arcabyolimpo.data.remote.api.ArcaApi
import com.app.arcabyolimpo.data.remote.dto.product.ProductDto
import com.app.arcabyolimpo.domain.model.product.ProductAdd
import com.app.arcabyolimpo.domain.repository.product.ProductRepository
import com.app.arcabyolimpo.domain.model.product.Product
import com.app.arcabyolimpo.data.mapper.product.toDetailDomain
import com.app.arcabyolimpo.data.remote.dto.product.ProductDetailDto
import com.app.arcabyolimpo.domain.model.product.ProductDetail
import com.app.arcabyolimpo.domain.model.product.ProductUpdate
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


/**
 * Implementation for the repository for product management, which
 * communicates with the remote API to perform CRUD operations.
 *
 * It uses dependency injection to gain access to [ArcaApi] and the
 * application [Context].
 */
@Singleton
class ProductRepositoryImpl @Inject constructor(
    private val api: ArcaApi,
    private val preferences: ProductPreferences,
    private val detailPreferences: ProductDetailPreferences,
    @ApplicationContext private val context: Context
) : ProductRepository {
    /**
     * addProduct.
     * Adds a new product to the system by communicating with the API.
     *
     * It builds a [MultipartBody] request body to send the product information,
     * including the image, to the server.
     *
     * @param product The [ProductAdd] object containing the details of the product to be added.
     * @return A [Result] indicating whether the operation was successful or if an error occurred.
     */

    override suspend fun addProduct(product: ProductAdd): Result<Unit> {
        return try {
           val imageUrlRequestBody = product.image.toRequestBody("text/plain".toMediaTypeOrNull())
            val idWorkshop = product.idWorkshop.toRequestBody("text/plain".toMediaTypeOrNull())
            val name = product.name.toRequestBody("text/plain".toMediaTypeOrNull())
            val unitaryPrice = product.unitaryPrice.toRequestBody("text/plain".toMediaTypeOrNull())
            val idCategory = product.idCategory.toRequestBody("text/plain".toMediaTypeOrNull())
            val description = product.description.toRequestBody("text/plain".toMediaTypeOrNull())
            val status = product.status.toRequestBody("text/plain".toMediaTypeOrNull())

            api.addProduct(
                idWorkshop = idWorkshop,
                name = name,
                unitaryPrice = unitaryPrice,
                idCategory = idCategory,
                description = description,
                status = status,
                image = imageUrlRequestBody,
            )
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * deleteProduct.
     * Deletes a product by its id by calling the API.
     *
     * @param id The id of the product to delete.
     * @throws HttpException if the response is not successful.
     */
    override suspend fun deleteProduct(id: String) {
        val response = api.deleteProduct(id)
        if (!response.isSuccessful) {
            throw HttpException(response)
        }
    }
    /**
     * getProducts.
     * Retrieves the complete list of products, using a cache-first strategy to
     * improve performance and reduce unnecessary network calls.
     * @return A list of [Product] representing the available products.
     * @throws Exception If the API request fails and no valid cache exists.
     */
    override suspend fun getProducts(): List<Product> {
        if (preferences.isCacheValid()) {
            val cachedData = preferences.getProductCache()
            if (cachedData != null) {
                return cachedData.productList
            }
        }

        return try{
            val remoteList = api.getProducts().map { it.toDomain() }
            preferences.saveProductList(remoteList)
            remoteList
        } catch (e: Exception) {
            val cachedData = preferences.getProductCache()
            if (cachedData != null) {
                cachedData.productList
            } else {
                throw e
            }
        }
    }

    /**
     * searchProducts.
     * Searches products in the API using GET /product/search?q=...
     */
    override suspend fun searchProducts(query: String): List<Product> {
        return api.searchProducts(query).map { it.toDomain() }
    }

    /**
     * getProductById.
     * Retrieves a product by its ID, using a cache-first strategy.
     *
     * Flow emissions sequence:
     * 1. Emits [Result.Loading]
     * 2. If a valid cache exists → emits [Result.Success] with cached data
     * 3. Attempts to fetch fresh data from the API:
     *      - If successful → saves new data in cache and emits [Result.Success]
     *      - If API fails:
     *          * If cache exists → emits cached [Result.Success]
     *          * Otherwise → emits [Result.Error]
     */
    override fun getProductById(
        productId: String
    ): Flow<com.app.arcabyolimpo.domain.common.Result<Product>> = flow {
        emit(com.app.arcabyolimpo.domain.common.Result.Loading)

        if(detailPreferences.isCacheValid(productId)){
            detailPreferences.getProductDetailCache(productId)?.let { cache ->
                emit(com.app.arcabyolimpo.domain.common.Result.Success(cache.productDetail))
            }
        }

        try {
            val productDto = api.getProductById(productId)

            if (productDto == null) {
                emit(com.app.arcabyolimpo.domain.common.Result.Error(
                    Exception("Producto no encontrado")
                ))
                return@flow
            }
            val productDetail = productDto.toDomain()

            detailPreferences.saveProductDetail(
                id = productId,
                productDetail = productDetail
            )

            emit(com.app.arcabyolimpo.domain.common.Result.Success(productDetail))

        } catch (e: Exception) {
            detailPreferences.getProductDetailCache(productId)?.let { cache ->
                emit(com.app.arcabyolimpo.domain.common.Result.Success(cache.productDetail))
                return@flow
            }

            emit(com.app.arcabyolimpo.domain.common.Result.Error(e))
        }
    }

    /**
     * Retrieves a detailed product from the remote API by its unique identifier.
     *
     * This function calls [ArcaApi.getProduct], handles network errors, and maps
     * the resulting [ProductDto] to the [Product] domain model.
     *
     * @param id The unique identifier of the product to retrieve.
     * @return A [Result] wrapping the [Product] domain model upon success.
     */
    override suspend fun getProduct(id: String): Result<ProductDetail> {
        return try {
            val productDetailDto: ProductDetailDto = api.getProduct(idProduct = id)

            val productDomain = productDetailDto.toDetailDomain()

            Result.success(productDomain)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // package com.app.arcabyolimpo.data.repository.product
// ...

    override suspend fun updateProduct(
        idProduct: String,
        product: ProductUpdate
    ): Result<Unit> {
        return try {
            val idWorkshopPart = product.idWorkshop!!.toRequestBody("text/plain".toMediaTypeOrNull())
            val namePart = product.name.toRequestBody("text/plain".toMediaTypeOrNull())
            val unitaryPricePart = product.unitaryPrice.toRequestBody("text/plain".toMediaTypeOrNull())
            val idCategoryPart = product.idCategory!!.toRequestBody("text/plain".toMediaTypeOrNull())
            val descriptionPart = product.description.toRequestBody("text/plain".toMediaTypeOrNull())
            val statusPart = product.status.toRequestBody("text/plain".toMediaTypeOrNull())
            val imagePart = product.image!!.toRequestBody("text/plain".toMediaTypeOrNull())

            api.updateProduct(
                idProduct = idProduct,
                idWorkshop = idWorkshopPart,
                name = namePart,
                unitaryPrice = unitaryPricePart,
                idCategory = idCategoryPart,
                description = descriptionPart,
                status = statusPart,
                image = imagePart
            )

            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("ProductRepositoryImpl", "Error al actualizar producto: ${e.message}", e)
            Result.failure(e)
        }
    }
}