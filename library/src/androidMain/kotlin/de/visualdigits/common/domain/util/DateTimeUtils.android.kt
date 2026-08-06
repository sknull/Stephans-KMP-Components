package de.visualdigits.common.domain.util

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.number
import org.intellij.lang.annotations.Language
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

/*
in IOS:

import platform.Foundation.NSDateFormatter
import platform.Foundation.NSCalendar
import platform.Foundation.NSDateComponents
import platform.Foundation.NSLocale
import platform.Foundation.currentLocale

actual fun LocalDate.formatLocalized(pattern: String, localeCode: String): String {
    val dateFormatter = NSDateFormatter().apply {
        this.dateFormat = pattern
        // Entspricht exakt dem forLanguageTag von Android
        this.locale = NSLocale.localeWithLocaleIdentifier(localeCode)
    }

    // Konvertierung deines KMP LocalDate in eine NSDate-Komponente
    val components = NSDateComponents().apply {
        this.year = this@formatLocalized.year.toLong()
        this.month = this@formatLocalized.monthNumber.toLong()
        this.day = this@formatLocalized.day.toLong()
    }

    val nsDate = NSCalendar.currentCalendar.dateFromComponents(components)
        ?: throw IllegalStateException("Could not create NSDate")

    return dateFormatter.stringFromDate(nsDate)
}
 */
