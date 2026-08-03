package de.visualdigits.common.domain.model.common

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class KmpOffsetDateTimeTest {

    @Test
    fun testZuluTime() {
        val dateTimeString = "2026-08-03T11:08:31.280404600Z"
        val dateTime = KmpOffsetDateTime.fromString(dateTimeString)
        assertEquals("2026-08-03T11:08:31.280404600Z", dateTime.toString())
    }

    @Test
    fun testZuluTimMissingZ() {
        val dateTimeString = "2026-08-03T11:08:31.280404600"
        val dateTime = KmpOffsetDateTime.fromString(dateTimeString)
        assertEquals("2026-08-03T11:08:31.280404600Z", dateTime.toString())
    }

    @Test
    fun testNonZuluTimePositiveOffset() {
        val dateTimeString = "2026-08-15T00:00:00.000000000+02:00"
        val dateTime = KmpOffsetDateTime.fromString(dateTimeString)
        assertEquals("2026-08-15T00:00:00+02:00", dateTime.toString())
    }

    @Test
    fun testNonZuluTimeNegativeOffset() {
        val dateTimeString = "2026-08-15T00:00:00.000000000-02:00"
        val dateTime = KmpOffsetDateTime.fromString(dateTimeString)
        assertEquals("2026-08-15T00:00:00-02:00", dateTime.toString())
    }

    @Test
    fun testZuluTimeWithoutT() {
        val dateTimeString = "2026-08-03 11:08:31.280404600Z"
        val dateTime = KmpOffsetDateTime.fromString(dateTimeString)
        assertEquals("2026-08-03T11:08:31.280404600Z", dateTime.toString())
    }

    @Test
    fun testZuluTimMissingZWithoutT() {
        val dateTimeString = "2026-08-03 11:08:31.280404600"
        val dateTime = KmpOffsetDateTime.fromString(dateTimeString)
        assertEquals("2026-08-03T11:08:31.280404600Z", dateTime.toString())
    }

    @Test
    fun testNonZuluTimePositiveOffsetWithoutT() {
        val dateTimeString = "2026-08-15 00:00:00.000000000+02:00"
        val dateTime = KmpOffsetDateTime.fromString(dateTimeString)
        assertEquals("2026-08-15T00:00:00+02:00", dateTime.toString())
    }

    @Test
    fun testNonZuluTimeNegativeOffsetWithoutT() {
        val dateTimeString = "2026-08-15 00:00:00.000000000-02:00"
        val dateTime = KmpOffsetDateTime.fromString(dateTimeString)
        assertEquals("2026-08-15T00:00:00-02:00", dateTime.toString())
    }
}
