package de.visualdigits.common.domain.util

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.number
import java.time.format.DateTimeFormatter
import java.util.Locale

actual fun LocalDate.formatLocalized(pattern: String, languageTag: String?): String {
    val javaDate = java.time.LocalDate.of(year, month.number, day)
    val formatter = DateTimeFormatter.ofPattern(pattern).withLocale(locale(languageTag))
    return javaDate.format(formatter)
}

actual fun LocalTime.formatLocalized(pattern: String, languageTag: String?): String {
    val javaTime = java.time.LocalTime.of(hour, minute, second)
    val formatter = DateTimeFormatter.ofPattern(pattern).withLocale(locale(languageTag))
    return javaTime.format(formatter)
}

private fun locale(languageTag: String?): Locale? =
    languageTag?.let { lt -> Locale.forLanguageTag(lt) } ?: Locale.getDefault()
