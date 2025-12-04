package com.app.arcabyolimpo.data.local.product.detail.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.app.arcabyolimpo.data.local.product.detail.model.ProductDetailCache
import com.app.arcabyolimpo.domain.model.product.Product
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handles the local caching of product detail information using SharedPreferences.
 * Supports saving, retrieving, and validating cached product details.
 *
 * @property prefs SharedPreferences instance where product details are stored.
 * @property gson Gson instance used to serialize/deserialize the Product objects.
 */
@Singleton
class ProductDetailPreferences @Inject constructor(
    @ApplicationContext context: Context,
    private val gson: Gson,
) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences(
            ProductDetailPreferencesConstants.PREF_NAME,
            Context.MODE_PRIVATE,
        )

    /**
     * Saves a product detail to SharedPreferences, storing both the serialized product
     * and the timestamp of the cache update.
     *
     * @param id The unique product ID used as a dynamic key suffix.
     * @param productDetail The product detail object to be cached.
     */
    fun saveProductDetail(
        id: String,
        productDetail: Product,
    ) {
        prefs.edit {
            putString(
                ProductDetailPreferencesConstants.KEY_PRODUCT_DETAIL + id,
                gson.toJson(productDetail)
            )
            putLong(
                ProductDetailPreferencesConstants.KEY_LAST_UPDATE + id,
                System.currentTimeMillis()
            )
        }
    }

    /**
     * Retrieves a cached product detail from SharedPreferences.
     *
     * @param id The product ID used to fetch the correct cached entry.
     * @return A [ProductDetailCache] object containing the product detail and its last update time,
     * or `null` if no valid cache is found.
     */
    fun getProductDetailCache(id: String): ProductDetailCache?{
        val json = prefs.getString(
            ProductDetailPreferencesConstants.KEY_PRODUCT_DETAIL + id,
            null
        )
        val lastUpdate = prefs.getLong(
            ProductDetailPreferencesConstants.KEY_LAST_UPDATE + id,
            0
        )

        if (json.isNullOrEmpty() || lastUpdate == 0L) return null

        val type = object : TypeToken<Product>() {}.type
        val productDetail: Product = gson.fromJson(json, type)

        return ProductDetailCache(
            productDetail = productDetail,
            lastUpdate = lastUpdate,
        )
    }

    /**
     * Checks whether the cached product detail for a given ID is still valid.
     * A cache is considered valid if:
     * - It exists
     * - Its last update is within the allowed CACHE_DURATION
     *
     * @param id The product ID whose cache validity is checked.
     * @return `true` if the cache is valid; `false` otherwise.
     */
    fun isCacheValid(id: String): Boolean {
        val lastUpdate = prefs.getLong(
            ProductDetailPreferencesConstants.KEY_LAST_UPDATE + id,
            0
        )
        if (lastUpdate == 0L) return false

        return System.currentTimeMillis() - lastUpdate <
                ProductDetailPreferencesConstants.CACHE_DURATION
    }

    /**
     * Clears all cached product data and metadata.
     *
     * This completely resets the cache, removing the stored product list and
     * last update timestamp.
     */
    fun clearCache() {
        prefs.edit().clear().apply()
    }
}