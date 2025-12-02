package com.app.arcabyolimpo.data.local.product.detail.preferences

/**
 * Constants used for storing and retrieving cached product detail information
 * in SharedPreferences.
 *
 * @property PREF_NAME Name of the SharedPreferences file containing product detail cache.
 * @property KEY_PRODUCT_DETAIL Base key used to store the JSON representation of a product detail.
 * Each product detail is saved using this key plus its product ID.
 *
 * @property KEY_LAST_UPDATE Base key used to store the timestamp of the last update for a given product detail.
 * Each timestamp is saved using this key plus its product ID.
 *
 * @property CACHE_DURATION Time (in milliseconds) during which the cache is considered valid.
 * Default is 5 minutes.
 */
object ProductDetailPreferencesConstants {
    const val PREF_NAME = "product_detail_preferences"
    const val KEY_PRODUCT_DETAIL = "product_detail_"
    const val KEY_LAST_UPDATE = "product_detail_last_update_"
    const val CACHE_DURATION = 5 * 60 * 1000
}