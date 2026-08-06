package de.visualdigits.common.domain.util

import androidx.compose.runtime.snapshots.toInt
import de.visualdigits.common.domain.model.common.KmpOffsetDateTime
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.UtcOffset
import kotlin.time.Clock

const val MILLIS_TO_EPOCH_DAYS = 1000L * 60 * 60 * 24



expect fun LocalDate.formatLocalized(pattern: String, languageTag: String? = null): String

fun LocalDate.Companion.now() = LocalDate.fromEpochDays(Clock.System.now().toEpochMilliseconds() / MILLIS_TO_EPOCH_DAYS)

fun LocalDate.format(pattern: String) = formatLocalized(pattern)

fun LocalDate.toEpochMilliseconds(): Long = toEpochDays() * MILLIS_TO_EPOCH_DAYS


expect fun LocalTime.formatLocalized(pattern: String, languageTag: String? = null): String

fun LocalTime.Companion.now(offset: UtcOffset = UtcOffset.ZERO): LocalTime {
    val now = KmpOffsetDateTime.now(offset)
    val nowMillis = now.toEpochMilliseconds()
    val midNightMillis = KmpOffsetDateTime(now.year, now.month, now.day, offset = offset).toEpochMilliseconds()

    return LocalTime.fromMillisecondOfDay((nowMillis - midNightMillis).toInt())
}

fun LocalTime.format(pattern: String) = formatLocalized(pattern)
