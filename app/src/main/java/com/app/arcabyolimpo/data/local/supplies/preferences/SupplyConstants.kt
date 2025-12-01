package com.app.arcabyolimpo.data.local.supplies.preferences

/**
 * Constants for supply caching mechanism.
 */
object SupplyConstants {
    const val PREFS_NAME = "supply_preferences"
    const val KEY_SUPPLIES_LIST = "supplies_list"
    const val KEY_LAST_CACHE_TIME = "last_cache_time"

    const val KEY_SUPPLY_DETAIL_PREFIX = "supply_detail_"
    const val KEY_SUPPLY_DETAIL_TIME_PREFIX = "supply_detail_time_"

    // Cache validity duration: 1 hour (in milliseconds)
    const val CACHE_VALIDITY_DURATION = 60 * 60 * 1000L // 1 hour


}