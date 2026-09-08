package de.visualdigits.common.domain.model.common

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.UtcOffset
import kotlinx.datetime.atTime
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.format.DayOfWeekNames
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char
import kotlinx.datetime.offsetAt
import kotlinx.datetime.toInstant
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.time.Instant

object KmpOffsetDateTimeHeuristicDeserializer : KSerializer<KmpOffsetDateTime> {

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        "KmpOffsetDateTime",
        PrimitiveKind.STRING
    )

    private val P_OFFSET_H_M = "[+-]\\d\\d:\\d\\d".toRegex()

    private val P_OFFSET_4_DIGITS = "[+-]\\d\\d\\d\\d".toRegex()

    private val WEEKDAY_NAMES_GERMAN_ABBREVIATED = DayOfWeekNames(
        monday = "Mon",
        tuesday = "Die",
        wednesday = "Mit",
        thursday = "Don",
        friday = "Fre",
        saturday = "Sam",
        sunday = "Son"
    )

    private val MONTH_NAMES_GERMAN_ABBREVIATED = MonthNames(
        january = "Jan",
        february = "Feb",
        march = "Mär",
        april = "Apr",
        may = "Mai",
        june = "Jun",
        july = "Jul",
        august = "Aug",
        september = "Sep",
        october = "Okt",
        november = "Nov",
        december = "Dez"
    )

    private val formatWithoutSeconds = DateTimeComponents.Format {
        year()
        char('-')
        monthNumber()
        char('-')
        day()

        char('T')

        hour()
        char(':')
        minute()

        char('Z')
    }

    private val formatIsoWithoutSeconds = DateTimeComponents.Format {
        // 2026-08-28T11:54+02:00
        year()
        char('-')
        monthNumber()
        char('-')
        day()

        char('T')

        hour()
        char(':')
        minute()

        offsetHours(Padding.NONE)
        char(':')
        offsetMinutesOfHour()
    }

    private val formatWeekDayEnglish = DateTimeComponents.Format {
        dayOfWeek(DayOfWeekNames.ENGLISH_ABBREVIATED)
        char(',')
        char(' ')
        day()
        char(' ')
        monthName(MonthNames.ENGLISH_ABBREVIATED)
        char(' ')
        year()
        char(' ')
        hour()
        char(':')
        minute()
        char(':')
        second()
        char(' ')
        offsetHours(Padding.NONE)
        offsetMinutesOfHour()
    }
    private val formatWeekDayGerman = DateTimeComponents.Format {
        dayOfWeek(WEEKDAY_NAMES_GERMAN_ABBREVIATED)
        char(',')
        char(' ')
        day()
        char(' ')
        monthName(MONTH_NAMES_GERMAN_ABBREVIATED)
        char(' ')
        year()
        char(' ')
        hour()
        char(':')
        minute()
        char(':')
        second()
        char(' ')
        offsetHours(Padding.NONE)
        offsetMinutesOfHour()
    }

    override fun deserialize(decoder: Decoder): KmpOffsetDateTime {
        return parse(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: KmpOffsetDateTime) {
        encoder.encodeString(value.toString())
    }

    fun parse(text: String): KmpOffsetDateTime {
        val parseIsoDateTime = parseIsoDateTime(text)
        val parseIso = parseIso(text)
        val parseOffsetDateTimeWeekdayEnglish = parseOffsetDateTimeWeekdayEnglish(text)
        val parseOffsetDateTimeWeekdayGerman = parseOffsetDateTimeWeekdayGerman(text)
        val parseOffsetDateTimeWithoutSeconds = parseOffsetDateTimeWithoutSeconds(text)
        val parseRfc1123 = parseRfc1123(text)
        val parseIsoWithoutSeconds = parseIsoWithoutSeconds(text)
        val parseLocalDate = parseLocalDate(text)
        val parseLocalDateTime = parseLocalDateTime(text)
        return parseIsoDateTime
            ?: parseIso
            ?: parseOffsetDateTimeWeekdayEnglish
            ?: parseOffsetDateTimeWeekdayGerman
            ?: parseOffsetDateTimeWithoutSeconds
            ?: parseRfc1123
            ?: parseIsoWithoutSeconds
            ?: parseLocalDate
            ?: parseLocalDateTime
            ?: error("Could not parse date time format '$text'")
    }

    private fun parseIsoDateTime(text: String): KmpOffsetDateTime? {
        return parse(text.replaceFirst(" ", "T"), DateTimeComponents.Formats.ISO_DATE_TIME_OFFSET::parse)
    }

    private fun parseIso(dateTimeString: String) : KmpOffsetDateTime? {
        val text = dateTimeString.replaceFirst(" ", "T")
        val resultHM = P_OFFSET_H_M.find(text)
        val result4Digits = P_OFFSET_4_DIGITS.find(text)
        var (input, offset) = if (resultHM != null) {
            val offsetTime = resultHM.value
            val dateTimeString = text
                .replace(offsetTime, "")
                .replaceFirst(" ", "T")
            Pair(dateTimeString, UtcOffset.parse(offsetTime))
        } else if (result4Digits != null) {
            val offsetTime = result4Digits.value
            val dateTimeString = text
                .substring(0, text.indexOf(offsetTime))
                .trim()
                .replaceFirst(" ", "T")
            Pair(dateTimeString, UtcOffset.parse(offsetTime, UtcOffset.Formats.FOUR_DIGITS))
        } else {
            val dateTimeString = text
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
            null
        }
    }

    private fun parseOffsetDateTimeWithoutSeconds(text: String): KmpOffsetDateTime? {
        return parse(text, formatWithoutSeconds::parse)
    }

    private fun parseOffsetDateTimeWeekdayEnglish(text: String): KmpOffsetDateTime? {
        return parse(text, formatWeekDayEnglish::parse)
    }

    private fun parseOffsetDateTimeWeekdayGerman(text: String): KmpOffsetDateTime? {
        return parse(text, formatWeekDayGerman::parse)
    }

    private fun parseIsoWithoutSeconds(text: String): KmpOffsetDateTime? {
        return parse(text, formatIsoWithoutSeconds::parse)
    }

    private fun parseRfc1123(text: String): KmpOffsetDateTime? {
        return parse(text, DateTimeComponents.Formats.RFC_1123::parse)
    }

    private fun parseLocalDate(text: String): KmpOffsetDateTime? {
        return try {
            val localDate = LocalDate.parse(text)
            val localDateTime = localDate.atTime(0, 0)
            val systemTimeZone = TimeZone.currentSystemDefault()
            val instant = localDateTime.toInstant(systemTimeZone)
            val offset = systemTimeZone.offsetAt(instant)
            KmpOffsetDateTime(instant, offset)
        } catch (_: Exception) {
            null
        }
    }

    private fun parseLocalDateTime(text: String): KmpOffsetDateTime? {
        return try {
            val localDateTime = LocalDateTime.parse(text)
            val systemTimeZone = TimeZone.currentSystemDefault()
            val instant = localDateTime.toInstant(systemTimeZone)
            val offset = systemTimeZone.offsetAt(instant)
            KmpOffsetDateTime(instant, offset)
        } catch (_: Exception) {
            null
        }
    }

    private fun parse(text: String, parser: (CharSequence) -> DateTimeComponents): KmpOffsetDateTime? {
        return try {
            val components = parser(text)
            val local = components.toLocalDateTime()
            val offs = components.toUtcOffset()
            val instant = components.toInstantUsingOffset()
            val offset = components.toUtcOffset()
            KmpOffsetDateTime(instant, offset)
        } catch (_: Exception) {
            null
        }
    }
}
