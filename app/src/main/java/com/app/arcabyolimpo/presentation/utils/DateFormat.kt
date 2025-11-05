package com.app.arcabyolimpo.presentation.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.TimeZone

/**
 *Converts a string in `dd/MM/yyyy` format to epoch millis in
 **midnight UTC**. Returns null if not parsed.
 *
 * @receiver date as text (e.g., "10/12/2025")
 * @param dateFormatter SimpleDateFormat configured by the caller (UTC, non-forgiving).
 */
fun String.toUtcMidnightOrNull(dateFormatter: SimpleDateFormat): Long? =
    try {
        val parsed = dateFormatter.parse(this) ?: return null
        val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
            time = parsed
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        cal.timeInMillis
    } catch (_: ParseException) {
        null
    }
