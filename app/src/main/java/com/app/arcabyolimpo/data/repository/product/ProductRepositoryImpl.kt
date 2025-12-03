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

@Singleton
class ProductRepositoryImpl @Inject constructor(
    private val api: ArcaApi,
    private val preferences: ProductPreferences,
    private val detailPreferences: ProductDetailPreferences,
    @ApplicationContext private val context: Context
) : ProductRepository {

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

    override suspend fun deleteProduct(id: String) {
        val response = api.deleteProduct(id)
        if (!response.isSuccessful) {
            throw HttpException(response)
        }
    }

    override suspend fun getProducts(): List<Product> {
        return api.getProducts().map { it.toDomain() }
    }

    override suspend fun searchProducts(query: String): List<Product> {
        return api.searchProducts(query).map { it.toDomain() }
    }

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

    override suspend fun getProduct(id: String): Result<ProductDetail> {
        return try {
            val productDetailDto: ProductDetailDto = api.getProduct(idProduct = id)
            val productDomain = productDetailDto.toDetailDomain()
            Result.success(productDomain)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateProduct(
        idProduct: String,
        product: ProductUpdate
    ): Result<Unit> {
        return try {
            val imagePart = product.image?.let {
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

            val idWorkshop = product.idWorkshop?.toRequestBody("text/plain".toMediaTypeOrNull())
            val name = product.name.toRequestBody("text/plain".toMediaTypeOrNull())
            val unitaryPrice = product.unitaryPrice.toRequestBody("text/plain".toMediaTypeOrNull())
            val idCategory = product.idCategory?.toRequestBody("text/plain".toMediaTypeOrNull())
            val description = product.description.toRequestBody("text/plain".toMediaTypeOrNull())
            val status = product.status.toString().toRequestBody("text/plain".toMediaTypeOrNull())

            Log.d("ProductRepositoryImpl", "idWorkshop: ${product.idWorkshop}")

            api.updateProduct(
                idProduct = idProduct,
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
}