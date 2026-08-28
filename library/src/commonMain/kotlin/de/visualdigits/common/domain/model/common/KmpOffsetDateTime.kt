package de.visualdigits.common.domain.model.common

import androidx.compose.runtime.Immutable
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.Month
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
import kotlinx.serialization.Serializable
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Instant

expect fun KmpOffsetDateTime.formatLocalized(pattern: String): String

@Serializable(with = KmpOffsetDateTimeHeuristicDeserializer::class)
@Immutable
data class KmpOffsetDateTime(
    val instant: Instant = Clock.System.now(),
    val offset: UtcOffset = UtcOffset.ZERO
) : Comparable<KmpOffsetDateTime> {

    constructor(epochMilliseconds: Long, offsetString: String) : this(
        epochMilliseconds = epochMilliseconds,
        offset = UtcOffset.parse(offsetString)
    )

    constructor(epochMilliseconds: Long, offset: UtcOffset = UtcOffset.ZERO) : this(
        instant = Instant.fromEpochMilliseconds(epochMilliseconds),
        offset = offset
    )

    constructor(epochSeconds: Int, offsetString: String) : this(
        epochMilliseconds = epochSeconds * 1000L,
        offsetString = offsetString
    )

    constructor(epochSeconds: Int, offset: UtcOffset = UtcOffset.ZERO) : this(
        epochMilliseconds = epochSeconds * 1000L,
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

    constructor(
        year: Int,
        month: Month,
        dayOfMonth: Int,
        hour: Int = 0,
        minute: Int = 0,
        second: Int = 0,
        offset: UtcOffset = UtcOffset.ZERO
    ): this(
        localDateTime =  LocalDateTime(
            year = year,
            month = month,
            day = dayOfMonth.coerceIn(1, 31),
            hour = hour.coerceIn(0, 23),
            minute = minute.coerceIn(0, 59),
            second = second.coerceIn(0, 59)
        ),
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

        val OFFSET_SYSTEM_DEFAULT: UtcOffset = Clock.System.now().offsetIn(TimeZone.currentSystemDefault())

        val P_OFFSET_H_M = "[+-]\\d\\d:\\d\\d".toRegex()
        val P_OFFSET_4_DIGITS = "[+-]\\d\\d\\d\\d".toRegex()

        fun now(offset: UtcOffset = UtcOffset.ZERO): KmpOffsetDateTime = KmpOffsetDateTime(
            instant = Clock.System.now(),
            offset = offset
        )

        fun fromString(text: String) : KmpOffsetDateTime {
            return KmpOffsetDateTimeHeuristicDeserializer.parse(text)
        }
    }

    fun toLocalDateTime(): LocalDateTime = instant.toLocalDateTime(offset.asTimeZone())

    fun toLocalDateTimeInSystemTimezone(): LocalDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())

    fun toInstant(): Instant = instant

    fun toEpochMilliseconds(): Long = instant.toEpochMilliseconds()

    fun toEpochSeconds(): Long = toEpochMilliseconds() / 1000

    fun format(pattern: String): String {
        return this.formatLocalized(pattern)
    }

    fun format(format: DateTimeFormat<DateTimeComponents>): String {
        return this.instant.format(format)
    }

    operator fun minus(other: KmpOffsetDateTime): Duration {
        return this.instant.minus(other.instant)
    }

    override operator fun compareTo(other: KmpOffsetDateTime): Int {
        return compareBy<KmpOffsetDateTime> { it.instant.toEpochMilliseconds() }.compare(this, other)
    }

    operator fun minus(duration: Duration): KmpOffsetDateTime {
        return KmpOffsetDateTime(this.instant.minus(duration))
    }

    operator fun plus(duration: Duration): KmpOffsetDateTime {
        return KmpOffsetDateTime(this.instant.plus(duration))
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

    val month: Month
        get() = localDate.month

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
            val instantWithoutZ = toLocalDateTime().format(LocalDateTime.Formats.ISO)
            "$instantWithoutZ$offset"
        }
    }
}

fun LocalDateTime.format(pattern: String): String {
    @OptIn(FormatStringsInDatetimeFormats::class)
    val dateTimeFormat = LocalDateTime.Format {
        byUnicodePattern(pattern)
    }
    return dateTimeFormat.format(this)
}
