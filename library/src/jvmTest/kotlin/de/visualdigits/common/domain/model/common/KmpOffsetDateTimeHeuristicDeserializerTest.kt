package de.visualdigits.common.domain.model.common

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class KmpOffsetDateTimeHeuristicDeserializerTest {

    @Test
    fun testEnglishWeekday() {
        val dateTimeString = "Tue, 11 Aug 2026 12:31:13 +0200"
        val dateTime = KmpOffsetDateTimeHeuristicDeserializer.parse(dateTimeString)
        assertEquals("2026-08-11T12:31:13+02:00", dateTime.toString())

        val formatted = dateTime.format("yyyy-MM-dd HH:mm:ss")
        assertEquals("2026-08-11 12:31:13", formatted)
    }

    @Test
    fun testEnglishWeekday2() {
        val dateTimeString = "Fri, 28 Aug 2026 20:06:33 +0200"
        val dateTime = KmpOffsetDateTimeHeuristicDeserializer.parse(dateTimeString)
        assertEquals("2026-08-28T20:06:33+02:00", dateTime.toString())

        val formatted = dateTime.format("yyyy-MM-dd HH:mm:ss")
        assertEquals("2026-08-28 20:06:33", formatted)
    }

    @Test
    fun testAisDateTime() {
        val dateTimeString = "2026-08-03 14:00:20.154920998 +0000 UTC"
        val dateTime = KmpOffsetDateTimeHeuristicDeserializer.parse(dateTimeString)
        assertEquals("2026-08-03T14:00:20.154920998Z", dateTime.toString())
    }

    @Test
    fun testZuluTime() {
        val dateTimeString = "2026-08-03T11:08:31.280404600Z"
        val dateTime = KmpOffsetDateTimeHeuristicDeserializer.parse(dateTimeString)
        assertEquals("2026-08-03T11:08:31.280404600Z", dateTime.toString())
    }

    @Test
    fun testZuluTimMissingZ() {
        val dateTimeString = "2026-08-03T11:08:31.280404600"
        val dateTime = KmpOffsetDateTimeHeuristicDeserializer.parse(dateTimeString)
        assertEquals("2026-08-03T11:08:31.280404600Z", dateTime.toString())
    }

    @Test
    fun testNonZuluTimePositiveOffset() {
        val dateTimeString = "2026-08-15T00:00:00.000000000+02:00"
        val dateTime = KmpOffsetDateTimeHeuristicDeserializer.parse(dateTimeString)
        assertEquals("2026-08-15T00:00:00+02:00", dateTime.toString())
    }

    @Test
    fun testNonZuluTimeNegativeOffset() {
        val dateTimeString = "2026-08-15T00:00:00.000000000-02:00"
        val dateTime = KmpOffsetDateTimeHeuristicDeserializer.parse(dateTimeString)
        assertEquals("2026-08-15T00:00:00-02:00", dateTime.toString())
    }

    @Test
    fun testZuluTimeWithoutT() {
        val dateTimeString = "2026-08-03 11:08:31.280404600Z"
        val dateTime = KmpOffsetDateTimeHeuristicDeserializer.parse(dateTimeString)
        assertEquals("2026-08-03T11:08:31.280404600Z", dateTime.toString())
    }

    @Test
    fun testZuluTimMissingZWithoutT() {
        val dateTimeString = "2026-08-03 11:08:31.280404600"
        val dateTime = KmpOffsetDateTimeHeuristicDeserializer.parse(dateTimeString)
        assertEquals("2026-08-03T11:08:31.280404600Z", dateTime.toString())
    }

    @Test
    fun testNonZuluTimePositiveOffsetWithoutT() {
        val dateTimeString = "2026-08-15 00:00:00.000000000+02:00"
        val dateTime = KmpOffsetDateTimeHeuristicDeserializer.parse(dateTimeString)
        assertEquals("2026-08-15T00:00:00+02:00", dateTime.toString())
    }

    @Test
    fun testNonZuluTimeNegativeOffsetWithoutT() {
        val dateTimeString = "2026-08-15 00:00:00.000000000-02:00"
        val dateTime = KmpOffsetDateTimeHeuristicDeserializer.parse(dateTimeString)
        assertEquals("2026-08-15T00:00:00-02:00", dateTime.toString())
    }
}
