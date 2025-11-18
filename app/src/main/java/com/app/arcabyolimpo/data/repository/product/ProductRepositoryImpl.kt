package com.app.arcabyolimpo.data.repository.product

import android.content.Context
import com.app.arcabyolimpo.data.mapper.product.toDomain
import com.app.arcabyolimpo.data.remote.api.ArcaApi
import com.app.arcabyolimpo.domain.model.product.Product
import com.app.arcabyolimpo.domain.model.product.ProductAdd
import com.app.arcabyolimpo.domain.repository.product.ProductRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

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
            val imagePart = product.image.let {
                val input = context.contentResolver.openInputStream(it)
                val bytes = input?.readBytes()
                input?.close()

                if (bytes != null) {
                    val requestFile = bytes.toRequestBody(
                        context.contentResolver.getType(it)?.toMediaTypeOrNull()
                    )
                    MultipartBody.Part.createFormData(
                        "image",
                        "image.jpg",
                        requestFile
                    )
                } else null
            }

            val idWorkshop = product.idWorkshop.toRequestBody("text/plain".toMediaTypeOrNull())
            val name = product.name.toRequestBody("text/plain".toMediaTypeOrNull())
            val unitaryPrice = product.unitaryPrice.toRequestBody("text/plain".toMediaTypeOrNull())
            val idCategory = product.idCategory.toRequestBody("text/plain".toMediaTypeOrNull())
            val description = product.description.toRequestBody("text/plain".toMediaTypeOrNull())
            val status = product.status.toString().toRequestBody("text/plain".toMediaTypeOrNull())

            api.addProduct(
                idWorkshop = idWorkshop,
                name = name,
                unitaryPrice = unitaryPrice,
                idCategory = idCategory,
                description = description,
                status = status,
                image = imagePart,
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
     * Fetches the full list of products from the API.
     */
    override suspend fun getProducts(): List<Product> {
        return api.getProducts().map { it.toDomain() }
    }

    /**
     * searchProducts.
     * Searches products in the API using GET /product/search?q=...
     */
    override suspend fun searchProducts(query: String): List<Product> {
        return api.searchProducts(query).map { it.toDomain() }
    }

}