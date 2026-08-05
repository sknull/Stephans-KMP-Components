package de.visualdigits.common.domain.model.common

import androidx.compose.runtime.Immutable
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.UtcOffset
import kotlinx.datetime.asTimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.offsetIn
import kotlinx.datetime.parse
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Instant

expect fun KmpOffsetDateTime.formatLocalized(pattern: String): String

@Immutable
data class KmpOffsetDateTime(
    val instant: Instant = Clock.System.now(),
    val offset: UtcOffset = UtcOffset.ZERO
) : Comparable<KmpOffsetDateTime> {

    constructor(millis: Long, offsetString: String) : this(
        millis = millis,
        offset = UtcOffset.parse(offsetString)
    )

    constructor(millis: Long, offset: UtcOffset) : this(
        instant = Instant.fromEpochMilliseconds(millis),
        offset = offset
    )

    constructor(
        input: String,
        format: DateTimeFormat<DateTimeComponents>,
        offset: UtcOffset = UtcOffset.ZERO
    ): this(
        instant = Instant.parse(input, format),
        offset = offset
    )

    constructor(
        localDateTime: LocalDateTime,
        offset: UtcOffset = UtcOffset.ZERO
    ): this(
        instant = localDateTime.toInstant(offset),
        offset = offset
    )

    companion object {

        val MIN: KmpOffsetDateTime = KmpOffsetDateTime(
            instant = Instant.DISTANT_PAST,
            offset = UtcOffset(hours = 18) // Maximales positives Java-Offset für die absolut früheste lokale Zeit
        )

        val MAX: KmpOffsetDateTime = KmpOffsetDateTime(
            instant = Instant.DISTANT_FUTURE,
            offset = UtcOffset(hours = -18) // Maximales negatives Java-Offset für die absolut späteste lokale Zeit
        )

        fun now(): KmpOffsetDateTime = KmpOffsetDateTime(instant = Clock.System.now())

        fun systemDefaultOffset(): UtcOffset {
            val currentInstant = Clock.System.now() // Current time in timeline
            val systemTimeZone = TimeZone.currentSystemDefault()

            // Determines the offset for this moment in time
            return currentInstant.offsetIn(systemTimeZone)
        }

        val P_OFFSET_H_M = "[+-]\\d\\d:\\d\\d".toRegex()
        val P_OFFSET_4_DIGITS = "[+-]\\d\\d\\d\\d".toRegex()

        fun fromString(isoDateTime: String) : KmpOffsetDateTime {
            val resultHM = P_OFFSET_H_M.find(isoDateTime)
            val result4Digits = P_OFFSET_4_DIGITS.find(isoDateTime)
            var (input, offset) = if (resultHM != null) {
                val offsetTime = resultHM.value
                val dateTimeString = isoDateTime
                    .replace(offsetTime, "")
                    .replaceFirst(" ", "T")
                Pair(dateTimeString, UtcOffset.parse(offsetTime))
            } else if (result4Digits != null) {
                val offsetTime = result4Digits.value
                val dateTimeString = isoDateTime
                    .substring(0, isoDateTime.indexOf(offsetTime))
                    .trim()
                    .replaceFirst(" ", "T")
                Pair(dateTimeString, UtcOffset.parse(offsetTime, UtcOffset.Formats.FOUR_DIGITS))
            } else {
                val dateTimeString = isoDateTime
                    .replaceFirst(" ", "T")
                Pair(dateTimeString, UtcOffset.ZERO)
            }
            if (!input.endsWith("Z")) {
                input += "Z"
            }
            return try {
                KmpOffsetDateTime(
                    instant = Instant.parse(input),
                    offset = offset
                )
            } catch (e: Exception) {
                throw IllegalStateException("Could not parse utc time: '$input'", e)
            }
        }
    }

    fun toLocalDateTime(): LocalDateTime = instant.toLocalDateTime(offset.asTimeZone())

    fun toLocalDateTimeInSystemTimezone(): LocalDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())

    fun toInstant(): Instant = instant

    fun format(pattern: String): String {
        return this.formatLocalized(pattern)
    }

    fun format(format: DateTimeFormat<DateTimeComponents>): String {
        return this.instant.format(format)
    }

    fun minus(other: KmpOffsetDateTime): Duration {
        return this.instant.minus(other.instant)
    }

    fun isBefore(other: KmpOffsetDateTime): Boolean {
        return this.instant < other.instant
    }

    fun minus(duration: Duration): KmpOffsetDateTime {
        return KmpOffsetDateTime(this.instant.minus(duration))
    }

    fun withLocalDate(newDate: LocalDate): KmpOffsetDateTime {
        val currentLocalTime = this.toLocalDateTime().time
        val newLocalDateTime = LocalDateTime(newDate, currentLocalTime)

        return KmpOffsetDateTime(
            instant = newLocalDateTime.toInstant(this.offset),
            offset = this.offset
        )
    }

    fun withLocalTime(newTime: LocalTime): KmpOffsetDateTime {
        val currentLocalDate = this.toLocalDateTime().date
        val newLocalDateTime = LocalDateTime(currentLocalDate, newTime)

        return KmpOffsetDateTime(
            instant = newLocalDateTime.toInstant(this.offset),
            offset = this.offset
        )
    }

    fun withTime(hour: Int, minute: Int): KmpOffsetDateTime {
        val currentLocalDate = this.toLocalDateTime().date
        val newLocalDateTime = LocalDateTime(currentLocalDate, LocalTime(hour,  minute, 0))

        return KmpOffsetDateTime(
            instant = newLocalDateTime.toInstant(this.offset),
            offset = this.offset
        )
    }

    val localDate: LocalDate
        get() = toLocalDateTime().date

    val year: Int
        get() = localDate.year

    val month: Int
        get() = localDate.month.ordinal

    val day: Int
        get() = localDate.day

    val localTime: LocalTime
        get() = toLocalDateTime().time

    val hour: Int
        get() = localTime.hour

    val minute: Int
        get() = localTime.minute

    val second: Int
        get() = localTime.second

    val millisecond: Int
        get() = localTime.nanosecond / 1000

    override fun toString(): String {
        return if (offset == UtcOffset.ZERO) {
            instant.toString()
        } else {
            val instantWithoutZ = instant.toString().substringBeforeLast("Z")
            "$instantWithoutZ$offset"
        }
    }

    override fun compareTo(other: KmpOffsetDateTime): Int {
        return compareBy<KmpOffsetDateTime>({ it.instant }).compare(this, other)
    }
}

fun LocalDateTime.format(pattern: String): String {
    @OptIn(FormatStringsInDatetimeFormats::class)
    val dateTimeFormat = LocalDateTime.Format {
        byUnicodePattern(pattern)
    }
    return dateTimeFormat.format(this)
}
