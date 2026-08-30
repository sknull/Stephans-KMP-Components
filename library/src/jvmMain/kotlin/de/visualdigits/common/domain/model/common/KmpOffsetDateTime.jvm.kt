package de.visualdigits.common.domain.model.common

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

actual fun KmpOffsetDateTime.formatLocalized(pattern: String): String {
    val javaInstant = Instant.ofEpochMilli(this.instant.toEpochMilliseconds())
    val javaOffset = ZoneOffset.ofTotalSeconds(this.offset.totalSeconds)
    val javaOffsetDateTime = java.time.OffsetDateTime.ofInstant(javaInstant, javaOffset)

    return javaOffsetDateTime.format(DateTimeFormatter.ofPattern(pattern, Locale.getDefault()))
}

actual fun LocalDateTime.formatLocalized(pattern: String): String {
    return this.toJavaLocalDateTime().format(DateTimeFormatter.ofPattern(pattern, Locale.getDefault()))
}
