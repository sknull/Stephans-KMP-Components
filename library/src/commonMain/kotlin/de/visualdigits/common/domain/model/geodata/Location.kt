package de.visualdigits.common.domain.model.geodata

import androidx.compose.ui.geometry.Offset
import co.touchlab.kermit.Logger
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


data class Location(
    val latitude: Double,
    val longitude: Double,
) {

    companion object {

        private const val RADIUS_EARTH_METERS = 6371000.0 // Erdradius in Metern
    }

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
     *
     * @param other The location to locate the distance to.
     */
    fun distanceTo(
        other: Location
    ): Double {

        val dLat = Math.toRadians(other.latitude - this.latitude)
        val dLon = Math.toRadians(other.longitude - this.longitude)

        val a = sin(dLat / 2).pow(2) +
                cos(Math.toRadians(this.latitude)) * cos(Math.toRadians(other.latitude)) *
                sin(dLon / 2).pow(2)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return RADIUS_EARTH_METERS * c
    }

    /**
     * Calculates the bearing (initial heading) from this location to another location in degrees (0–360).
     * 0° = North, 90° = East, 180° = South, 270° = West.
     *
     * @param other The location to calculate the bearing to.
    */
    fun bearingTo(other: Location): Double {
        val lat1 = Math.toRadians(this.latitude)
        val lat2 = Math.toRadians(other.latitude)
        val dLon = Math.toRadians(other.longitude - this.longitude)

        val y = sin(dLon) * cos(lat2)
        val x = cos(lat1) * sin(lat2) - sin(lat1) * cos(lat2) * cos(dLon)

        val radiansBearing = atan2(y, x)

        val degreesBearing = Math.toDegrees(radiansBearing)
        return (degreesBearing + 360.0) % 360.0
    }

    /**
     * Converts distance and bearing from this location (treated as the center) to another one
     * into an X/Y offset for the Compose canvas.
     *
     * @param other The location to calculate the offset for.
     * @param radarRadiusPx The radius of your radar circle on the screen in pixels.
     * @param maxRadarDistanceMeters The maximum distance displayed by your inner geofence (e.g., 5000 meters).
     * @param center The center of your canvas (offset(width/2, height/2)).
     */
    fun calculateRadarOffset(
        other: Location,
        radarRadiusPx: Float,
        maxRadarDistanceMeters: Double,
        center: Offset
    ): Offset {
        val distance = distanceTo(other)
        return if (distance <= maxRadarDistanceMeters) {
            calculateRadarOffset(distance, bearingTo(other), maxRadarDistanceMeters, radarRadiusPx, center)
        } else {
            Offset.Unspecified
        }
    }

    /**
     * Converts distance and bearing from this location (treated as the center) to another one
     * into an X/Y offset for the Compose canvas.
     *
     * @param distance The distance in meters from the center coordinates.
     * @param bearing The bearing in degrees (0° north) from the center coordinates.
     * @param radarRadiusPx The radius of your radar circle on the screen in pixels.
     * @param maxRadarDistanceMeters The maximum distance displayed by your inner geofence (e.g., 5000 meters).
     * @param center The center of your canvas (offset(width/2, height/2)).
     */
    fun calculateRadarOffset(
        distance: Double,
        bearing: Double,
        maxRadarDistanceMeters: Double,
        radarRadiusPx: Float,
        center: Offset
    ): Offset {
        // 1. Scale the distance relative to the maximum radar radius
        // Ships outside the maximum radius are drawn at the edge of the radar
        val clampedDistance = distance.coerceAtMost(maxRadarDistanceMeters)
        val distanceFraction = clampedDistance / maxRadarDistanceMeters
        val distanceFromCenterPx = radarRadiusPx * distanceFraction

        // 2. Adjust the angle (0° should be at the top, clockwise)
        val angleRad = Math.toRadians(bearing - 90.0)

        // 3. Calculate the X and Y deviations
        val x = center.x + (distanceFromCenterPx * cos(angleRad)).toFloat()
        val y = center.y + (distanceFromCenterPx * sin(angleRad)).toFloat()

        return Offset(x, y)
    }

    fun isInBoundingBox(boundingBox: BoundingBox): Boolean {
        // North (topLeft) is the MAXIMUM latitude, south (bottomRight) is the MINIMUM
        val isWithinLatitude = latitude <= boundingBox.topLeft.latitude &&
                latitude >= boundingBox.bottomRight.latitude

        // West (topLeft) is the MINIMUM longitude, East (bottomRight) is the MAXIMUM
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
                latitude = roundCoordinate(latitude + latOffset).coerceIn(-90.0, 90.0), // Norden +
                longitude = roundCoordinate(longitude - lonOffset).coerceIn(-180.0, 180.0) // Westen -
            ),
            bottomRight = Location(
                latitude = roundCoordinate(latitude - latOffset).coerceIn(-90.0, 90.0), // Süden -
                longitude = roundCoordinate(longitude + lonOffset).coerceIn(-180.0, 180.0) // Osten +
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

        val secondsNotTruncated = (minutesNotTruncated - minutes) * 60.0
        val seconds = round(secondsNotTruncated * 100.0) / 100.0

        return "$degrees°$minutes'$seconds\"$direction"
    }
}

// Regex searches for: Grad°, Minuten', Sekunden" and directions (N, S, E, W)
private val regex = """(\d+)°(\d+)'([\d.]+)"([NSEW])""".toRegex()

val COORDINATES_DEFAULT = listOf(53.545977, 9.9680454)

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
    } else if ((this.contains("N") || this.contains("S")) && (this.contains("E") || this.contains("W"))) {
        val parts = this.trim().split("\\s+".toRegex())
        if (parts.size == 2) {
            try {
                val latStr = parts[0]
                val lonStr = parts[1]

                // parse latitude (i.e. "4230N")
                val latDeg = latStr.substring(0, 2).toDouble()
                val latMin = latStr.substring(2, 4).toDouble()
                val latDir = latStr.last()
                var latitude = latDeg + (latMin / 60.0)
                if (latDir == 'S') latitude = -latitude

                // parse longitude (i.e. "00131E")
                val lonDeg = lonStr.substring(0, 3).toDouble()
                val lonMin = lonStr.substring(3, 5).toDouble()
                val lonDir = lonStr.last()
                var longitude = lonDeg + (lonMin / 60.0)
                if (lonDir == 'W') longitude = -longitude

                Location(latitude, longitude)
            } catch (_: Exception) {
                null
            }
        } else {
            null
        }
    } else if (!this.isBlank()) {
        val parts = try {
            if (this.contains(",")) {
                this.trim().split(",".toRegex()).map { it.trim().toDouble() }
            } else {
                this.trim().split(" +".toRegex()).map { it.trim().toDouble() }
            }
        } catch (_: Exception) {
            Logger.e("Invalid format for double: $this - falling back to Hamburg Harbor" )
            COORDINATES_DEFAULT
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
