package com.app.arcabyolimpo.data.local.supplybatches.preferences

/**
 * Defines constants used for managing the supply batches cache in SharedPreferences.
 *
 * This object centralizes the keys for the preference file name, the cached data,
 * the last update timestamp, and the cache's expiration time.
 *
 * @property PREF_NAME The name of the SharedPreferences file where supply batch data is stored.
 * @property KEY_BATCHES_LIST The base key for storing the JSON-serialized list of supply batches.
 * @property KEY_LAST_UPDATE The base key for storing the timestamp of the last cache update.
 * @property CACHE_DURATION The duration in milliseconds for which the cache is considered valid (5 minutes).
 */
object SupplyBatchesConstants {
    const val PREF_NAME = "supply_batches_preferences"
    const val KEY_BATCHES_LIST = "supply_batches_list"
    const val KEY_LAST_UPDATE = "last_update"
    const val CACHE_DURATION = 5 * 60 * 1000
}
