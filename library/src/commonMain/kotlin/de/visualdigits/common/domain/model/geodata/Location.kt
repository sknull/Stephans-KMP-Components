package de.visualdigits.common.domain.model.geodata

import androidx.compose.ui.geometry.Offset
import co.touchlab.kermit.Severity
import de.visualdigits.common.domain.model.errorhandling.LogMessage.Companion.log
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.round
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt


// Regex searches for: Grad°, Minuten', Sekunden" and directions (N, S, E, W)
private val regex = """(\d+)°(\d+)'([\d.]+)"([NSEW])""".toRegex()

data class Location(
    val latitude: Double,
    val longitude: Double,
) {

    override fun toString(): String {
        return toDmsString()
    }

    /**
     * Converts the given pair (lat,lon) to a DMS formatted string.
     */
    fun toDmsString(): String {
        val latitude = this.latitude
        val longitude = this.longitude

        val latDms = convertToDms(latitude, isLatitude = true)
        val lonDms = convertToDms(longitude, isLatitude = false)

        return "$latDms $lonDms"
    }

    /**
     * Calculates the exact distance from this location to the given other location in meters.
     */
    fun distanceTo(
        other: Location
    ): Double {
        val earthRadiusMeters = 6371000.0 // Erdradius in Metern

        val dLat = Math.toRadians(other.latitude - this.latitude)
        val dLon = Math.toRadians(other.longitude - this.longitude)

        val a = sin(dLat / 2).pow(2) +
                cos(Math.toRadians(this.latitude)) * cos(Math.toRadians(other.latitude)) *
                sin(dLon / 2).pow(2)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return earthRadiusMeters * c
    }

    /**
    * Berechnet die Peilung (Anfangskurs) von dieser Location zu einer anderen Location in Grad (0..360).
    * 0° = Nord, 90° = Ost, 180° = Süd, 270° = West.
    */
    fun bearingTo(other: Location): Double {
        val lat1 = Math.toRadians(this.latitude)
        val lat2 = Math.toRadians(other.latitude)
        val dLon = Math.toRadians(other.longitude - this.longitude)

        val y = sin(dLon) * cos(lat2)
        val x = cos(lat1) * sin(lat2) - sin(lat1) * cos(lat2) * cos(dLon)

        val radiansBearing = atan2(y, x)

        // Umrechnung in Grad und Normalisierung auf 0..360 Grad
        val degreesBearing = Math.toDegrees(radiansBearing)
        return (degreesBearing + 360.0) % 360.0
    }

    /**
     * Rechnet Entfernung und Peilung in ein X/Y-Offset für die Compose-Canvas um.
     * @param distance Die berechnete Entfernung zum Schiff in Metern.
     * @param bearing Die berechnete Peilung zum Schiff in Grad (0..360).
     * @param radarRadiusPx Der Radius deines Radarkreises auf dem Bildschirm in Pixeln.
     * @param maxRadarDistanceMeters Die maximale Entfernung, die dein innerer Geofence anzeigt (z.B. 5000 Meter).
     * @param center Das Zentrum deiner Canvas (Offset(width/2, height/2)).
     */
    fun calculateRadarOffset(
        other: Location,
        radarRadiusPx: Float,
        maxRadarDistanceMeters: Double,
        center: Offset
    ): Offset {
        val distance = distanceTo(other)
        return if (distance <= maxRadarDistanceMeters) {
            val bearing = bearingTo(other)

            // 1. Skaliere die Entfernung relativ zum maximalen Radar-Radius
            // Schiffe außerhalb des maximalen Radius werden am Rand des Radars gezeichnet
            val clampedDistance = distance.coerceAtMost(maxRadarDistanceMeters)
            val distanceFraction = clampedDistance / maxRadarDistanceMeters
            val distanceFromCenterPx = radarRadiusPx * distanceFraction

            // 2. Winkel anpassen (0° soll oben sein, im Uhrzeigersinn)
            val angleRad = Math.toRadians(bearing - 90.0)

            // 3. X- und Y-Abweichung berechnen
            val x = center.x + (distanceFromCenterPx * cos(angleRad)).toFloat()
            val y = center.y + (distanceFromCenterPx * sin(angleRad)).toFloat()

            Offset(x, y)
        } else {
            Offset.Unspecified
        }
    }

    fun isInBoundingBox(boundingBox: BoundingBox): Boolean {
        // Norden (topLeft) ist der MAXIMALE Breitengrad, Süden (bottomRight) der MINIMALE
        val isWithinLatitude = latitude <= boundingBox.topLeft.latitude &&
                latitude >= boundingBox.bottomRight.latitude

        // Westen (topLeft) ist der MINIMALE Längengrad, Osten (bottomRight) der MAXIMALE
        val isWithinLongitude = longitude >= boundingBox.topLeft.longitude &&
                longitude <= boundingBox.bottomRight.longitude

        return isWithinLatitude && isWithinLongitude
    }

    /**
     * Spans a square around this location having side lengths of the given radius in meters.
     *
     * @param radiusInMeters The radius (distance from the center to the edges of the square) in meterns.
     */
    fun calculateBoundingBox(
        radiusInMeters: Double
    ): BoundingBox {
        val earthRadius = 6371000.0 // Erdradius in Metern

        // degree shift for the latitude - same on the whole earth
        val latOffset = (radiusInMeters / earthRadius) * (180.0 / PI)

        // degree shift for the longitude - affected by the latitude
        val latRadians = Math.toRadians(latitude)
        val lonOffset = (radiusInMeters / (earthRadius * cos(latRadians))) * (180.0 / PI)

        // calculate coordinates and round to 6 digits
        fun roundCoordinate(value: Double) = (value * 1000000.0).roundToInt() / 1000000.0

        return BoundingBox(
            topLeft = Location(
                latitude = roundCoordinate(latitude + latOffset), // Norden +
                longitude = roundCoordinate(longitude - lonOffset) // Westen -
            ),
            bottomRight = Location(
                latitude = roundCoordinate(latitude - latOffset), // Süden -
                longitude = roundCoordinate(longitude + lonOffset) // Osten +
            )
        )
    }

    /**
     * Converts the given degree value into a DMS string.
     */
    private fun convertToDms(value: Double, isLatitude: Boolean): String {
        val direction = if (isLatitude) {
            if (value >= 0) "N" else "S"
        } else {
            if (value >= 0) "E" else "W"
        }

        val absolute = abs(value)
        val degrees = floor(absolute).toInt()

        val minutesNotTruncated = (absolute - degrees) * 60.0
        val minutes = floor(minutesNotTruncated).toInt()

        // Sekunden berechnen und auf zwei Nachkommastellen runden
        val secondsNotTruncated = (minutesNotTruncated - minutes) * 60.0
        val seconds = round(secondsNotTruncated * 100.0) / 100.0

        // KMP-sichere Formatierung ohne String.format()
        return "$degrees°$minutes'$seconds\"$direction"
    }
}

const val COORDINATES_DEFAULT = "53.545977 9.9680454"
val COORDINATES_DEFAULT_DOUBLE = listOf(53.545977, 9.9680454)

/**
 * Converts the given DMS formatted string into a pair (lat,lon)
 */
fun String.toLocation(): Location? {
    return if (this.contains("°") && this.contains("'") && this.contains("\"")) {
        val matches = regex.findAll(this).toList()

        if (matches.size != 2) {
            throw IllegalArgumentException("Invalid format. Expects two DMS strings or decimal values.")
        }

        Location(
            latitude = calculateDecimal(matches[0]),
            longitude = calculateDecimal(matches[1])
        )
    } else if (!this.isBlank()) {
        val parts = try {
            if (this.contains(",")) {
                this.trim().split(",".toRegex()).map { it.trim().toDouble() }
            } else {
                this.trim().split(" +".toRegex()).map { it.trim().toDouble() }
            }
        } catch (_: Exception) {
            log(Severity.Error, "Invalid format for double: $this - falling back to Hamburg Harbor", withTag = "AIS" )
            COORDINATES_DEFAULT_DOUBLE
        }
        if (parts.size == 2) {
            Location(parts[0], parts[1])
        } else {
            throw IllegalArgumentException("Invalid format. Expects two DMS strings decimal values.")
        }
    } else {
        null
    }
}

/**
 * Converts the given matchresult containing (degrees, minutes, seconds, direction) into a degree value as Double.
 */
private fun calculateDecimal(matchResult: MatchResult): Double {
    val (degrees, minutes, seconds, direction) = matchResult.destructured

    // make negative for south or west
    val factor = if (direction == "S" || direction == "W") -1 else 1
    val decimal = factor * (degrees.toDouble() + (minutes.toDouble() / 60.0) + (seconds.toDouble() / 3600.0))

    // round to 6 digits (standard for GPS coordinates)
    return round(decimal * 1000000.0) / 1000000.0
}
