package de.visualdigits.kaisstream.domain.model.geodata

import kotlinx.datetime.TimeZone
import kotlinx.datetime.UtcOffset
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Instant

data class KmpOffsetDateTime(
    val utcInstant: Instant,
    val offset: UtcOffset = UtcOffset.ZERO
) {

    constructor(millis: Long, offsetString: String) : this(
        utcInstant = Instant.fromEpochMilliseconds(millis),
        offset = UtcOffset.parse(offsetString)
    )

    companion object {

        fun now(): KmpOffsetDateTime = KmpOffsetDateTime(utcInstant = Clock.System.now())

        fun fromString(isoDateTime: String) : KmpOffsetDateTime {
            var input = isoDateTime.replaceFirst(" ", "T").substringBefore(" +")
            if (!input.endsWith("Z")) {
                input += "Z"
            }
            return try {
                KmpOffsetDateTime(
                    utcInstant = Instant.parse(input),
                    offset = UtcOffset.ZERO
                )
            } catch (e: Exception) {
                throw IllegalStateException("Could not parse utc time: '$input'", e)
            }
        }
    }

    val localDateTime
        get() = utcInstant.toLocalDateTime(TimeZone.UTC)

    override fun toString(): String {
        return if (offset == UtcOffset.ZERO) {
            utcInstant.toString()
        } else {
            val instantWithoutZ = utcInstant.toString().substringBeforeLast("Z")
            "$instantWithoutZ$offset"
        }
    }
}

fun KmpOffsetDateTime.format(format: DateTimeFormat<DateTimeComponents>): String {
    return this.utcInstant.format(format)
}

fun KmpOffsetDateTime.minus(other: KmpOffsetDateTime): Duration {
    return this.utcInstant.minus(other.utcInstant)
}
