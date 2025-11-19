import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.format.TextStyle
import java.util.Locale

fun formatExpirationDate(rawDate: String?): String {
    if (rawDate.isNullOrBlank()) return ""

    val locale = Locale("es", "ES")

    val date = try {
        LocalDate.parse(rawDate)
    } catch (e1: Exception) {
        try {
            LocalDateTime.parse(rawDate).toLocalDate()
        } catch (e2: Exception) {
            try {
                OffsetDateTime.parse(rawDate).toLocalDate()
            } catch (e3: Exception) {
                rawDate.substringBefore('T').let {
                    try { LocalDate.parse(it) } catch (_: Exception) { null }
                }
            }
        }
    } ?: return rawDate

    val month = date.month.getDisplayName(TextStyle.FULL, locale)
        .replaceFirstChar { ch ->
            if (ch.isLowerCase()) {
                ch.titlecase(locale)
            } else {
                ch.toString()
            }
        }

    return String.format(locale, "%02d/%s/%04d", date.dayOfMonth, month, date.year)
}