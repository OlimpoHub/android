package com.app.arcabyolimpo.data.local.supplies.preferences

/**
 * Constants for supply caching mechanism.
 */
object SupplyConstants {
    const val PREFS_NAME = "supply_preferences"
    const val KEY_SUPPLIES_LIST = "supplies_list"
    const val KEY_LAST_CACHE_TIME = "last_cache_time"

    // Cache validity duration: 1 hour (in milliseconds)
    // Adjust this based on how often your data changes
    const val CACHE_VALIDITY_DURATION = 60 * 60 * 1000L // 1 hour

    // Alternative durations you can use:
    // const val CACHE_VALIDITY_DURATION = 5 * 60 * 1000L // 5 minutes
    // const val CACHE_VALIDITY_DURATION = 24 * 60 * 60 * 1000L // 24 hours
}