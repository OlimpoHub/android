package com.app.arcabyolimpo.data.repository.product

import android.content.Context
import com.app.arcabyolimpo.data.remote.api.ArcaApi
import com.app.arcabyolimpo.domain.model.product.ProductAdd
import com.app.arcabyolimpo.domain.repository.product.ProductRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepositoryImpl @Inject constructor(
    private val api: ArcaApi,
    @ApplicationContext private val context: Context
) : ProductRepository {
    override suspend fun addProduct(product: ProductAdd): Result<Unit>{
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
}