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
