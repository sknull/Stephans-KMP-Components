package de.visualdigits.common.domain.model.geodata

data class BoundingBox(
    val topLeft: Location,
    val bottomRight: Location
) {

    override fun toString(): String {
        return "$topLeft / $bottomRight"
    }

    fun toList(): List<List<List<Double>>> =
        listOf(listOf(listOf(topLeft.latitude, topLeft.longitude), listOf(bottomRight.latitude, bottomRight.longitude)))
}
