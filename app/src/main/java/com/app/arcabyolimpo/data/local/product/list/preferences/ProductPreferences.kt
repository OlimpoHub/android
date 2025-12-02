package com.app.arcabyolimpo.data.local.product.list.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.app.arcabyolimpo.domain.model.product.Product
import com.app.arcabyolimpo.data.local.product.list.model.ProductCache
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manages product-related caching using [SharedPreferences].
 *
 * This class provides a simple local caching mechanism to store and retrieve
 * product data, helping reduce unnecessary API calls and improving app
 * performance.
 *
 * The cache stores:
 * - A serialized list of [Product] objects (JSON using [Gson])
 * - The timestamp of the last update
 *
 * The cache is considered valid as long as the elapsed time since the last
 * update does not exceed [ProductPreferencesConstants.CACHE_DURATION].
 *
 * This class is annotated with [Singleton] so that Hilt provides a single
 * instance throughout the entire app lifecycle.
 *
 * @property gson The Gson serializer/deserializer used for handling JSON data.
 */
@Singleton
class ProductPreferences @Inject constructor(
    @ApplicationContext context: Context,
    private val gson: Gson,
) {

    /**
     * SharedPreferences storage used to persist the product cache and metadata.
     */
    private val prefs: SharedPreferences =
        context.getSharedPreferences(
            ProductPreferencesConstants.PREF_NAME,
            Context.MODE_PRIVATE,
        )

    /**
     * Saves the provided list of products to local storage.
     *
     * The list is serialized as JSON and stored along with a timestamp
     * representing when the cache was last updated.
     *
     * @param productList The list of [Product] objects to store.
     */
    fun saveProductList(
        productList: List<Product>,
    ) {
        prefs.edit {
            putString(
                ProductPreferencesConstants.KEY_PRODUCT_CACHE,
                gson.toJson(productList)
            )
            putLong(
                ProductPreferencesConstants.KEY_LAST_UPDATE,
                System.currentTimeMillis()
            )
        }
    }

    /**
     * Retrieves the cached product list from local storage.
     *
     * If no cache exists, or if the cache is improperly stored, this method
     * returns `null`.
     *
     * @return A [ProductCache] containing the cached product list and timestamp,
     *         or `null` if no valid cache exists.
     */
    fun getProductCache(): ProductCache? {
        val json = prefs.getString(ProductPreferencesConstants.KEY_PRODUCT_CACHE, null)
        val lastUpdate = prefs.getLong(ProductPreferencesConstants.KEY_LAST_UPDATE, 0)

        if (json.isNullOrEmpty() || lastUpdate == 0L) return null

        val type = object : TypeToken<List<Product>>() {}.type
        val productList: List<Product> = gson.fromJson(json, type)

        return ProductCache(
            productList = productList,
            lastUpdate = lastUpdate,
        )
    }

    /**
     * Determines whether the cached data is still valid based on
     * the elapsed time since it was last updated.
     *
     * The cache is considered valid if the time difference between the
     * current system time and the last update time is less than
     * [ProductPreferencesConstants.CACHE_DURATION].
     *
     * @return `true` if the cache is still valid, or `false` otherwise.
     */
    fun isCacheValid(): Boolean {
        val lastUpdate = prefs.getLong(ProductPreferencesConstants.KEY_LAST_UPDATE, 0)
        if (lastUpdate == 0L) return false

        return System.currentTimeMillis() - lastUpdate <
                ProductPreferencesConstants.CACHE_DURATION
    }

}
