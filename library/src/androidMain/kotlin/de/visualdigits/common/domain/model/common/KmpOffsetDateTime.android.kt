package de.visualdigits.common.domain.model.common

import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

actual fun KmpOffsetDateTime.formatLocalized(pattern: String): String {
    // 1. Konvertierung in Java-Typen
    val javaInstant = Instant.ofEpochMilli(this.instant.toEpochMilliseconds())
    val javaOffset = ZoneOffset.ofTotalSeconds(this.offset.totalSeconds)
    val javaOffsetDateTime = java.time.OffsetDateTime.ofInstant(javaInstant, javaOffset)

    // 2. Formatierung über die aktuelle Systemsprache des Geräts
    val formatter = DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
    return javaOffsetDateTime.format(formatter)
}

/*
For IOS

import platform.Foundation.NSDateFormatter
import platform.Foundation.NSDate
import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.NSTimeZone
import platform.Foundation.timeZoneForSecondsFromGMT

actual fun KmpOffsetDateTime.formatLocalized(pattern: String): String {
    val formatter = NSDateFormatter().apply {
        this.dateFormat = pattern
        this.locale = NSLocale.currentLocale // Erkennt automatisch Japanisch, Arabisch, etc.
        this.timeZone = NSTimeZone.timeZoneForSecondsFromGMT(offset.totalSeconds.toLong())
    }

    // Konvertierung von Epoch-Millisekunden zu NSDate
    val nsDate = NSDate.dateWithTimeIntervalSince1970(this.instant.toEpochMilliseconds() / 1000.0)
    return formatter.stringFromDate(nsDate)
}
 */
