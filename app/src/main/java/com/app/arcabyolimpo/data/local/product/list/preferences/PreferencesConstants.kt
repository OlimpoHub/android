package com.app.arcabyolimpo.data.local.product.list.preferences

/**
 * Contains constant values used for managing product-related preferences
 * and cache configuration within [ProductPreferences].
 *
 * These constants define the SharedPreferences file name, the keys used to
 * store cached data, and the duration for which the cache remains valid.
 */
object  ProductPreferencesConstants {
    const val PREF_NAME = "product_preferences"
    const val KEY_PRODUCT_CACHE = "product_cache"
    const val KEY_LAST_UPDATE = "last_update"
    const val CACHE_DURATION = 5 * 60 * 1000
}
