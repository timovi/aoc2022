data class Coordinate(val x: Int, val y: Int)
data class ScanRange(val y: Int, val xRange: IntRange)
data class Sensor(
    val position: Coordinate,
    val closestBeacon: Coordinate,
    val distance: Int
)

fun main() {
    val input = {}::class.java.getResource("input2.txt").readText()
    val sensors = parseSensors(input)

    val inspectedRow = 2000000
    println("Puzzle one: ${unknownXCoordinatesThatCannotContainBeacon(inspectedRow, sensors).size}")

    val min = 0
    val max = 20



    println("Puzzle two: ")
}

fun parseSensors(input: String) =
    input
        .split("\r\n")
        .map {
            val match =
                Regex("Sensor at x=(-?\\d+), y=(-?\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)").find(it)!!
            val (sensorX, sensorY, beaconX, beaconY) = match.destructured
            val sensorCoordinate = Coordinate(sensorX.toInt(), sensorY.toInt())
            val beaconCoordinate = Coordinate(beaconX.toInt(), beaconY.toInt())
            val distance =
                Math.abs(sensorCoordinate.x - beaconCoordinate.x) + Math.abs(sensorCoordinate.y - beaconCoordinate.y)
            Sensor(sensorCoordinate, beaconCoordinate, distance)
        }

fun unknownXCoordinatesThatCannotContainBeacon(y: Int, sensors: List<Sensor>) : Set<Int> {
    val coveringSensors = sensors.flatMap { it.coveredXCoordinatesIn(y) }.toSet()

    val coveringBeacons = sensors.filter { it.closestBeacon.y == y }.map { it.closestBeacon.x }.toSet()
    return (coveringSensors - coveringBeacons)
}

/*fun xCoordinatesThatCanContainDistressSignal(coordinateRange: IntRange, sensors: List<Sensor>) : Set<Int> {
    for (y in coordinateRange.start .. coordinateRange.endInclusive) {
        val coveringSensors = sensors.map { it.coveredXCoordinatesIn(y) }

        coveringSensors.first().intersect()

        for (x in coordinateRange.start .. coordinateRange.endInclusive)
    }

    val coveringBeacons = sensors.filter { it.closestBeacon.y == y }.map { it.closestBeacon.x }.toSet()
    return (coveringSensors - coveringBeacons)
}*/

fun Sensor.coveredXCoordinatesIn(y: Int): IntRange {
    val yDifference = Math.abs(this.position.y - y)
    if (this.distance < yDifference) {
            return IntRange.EMPTY
    }

    val xDifference = distance - yDifference
    return this.position.x - xDifference .. this.position.x + xDifference
}

fun IntRange.except(other: IntRange) : List<IntRange> {
    return if (this == other ) {
        listOf(IntRange.EMPTY)
    } else if (this.start > other.endInclusive) {
        listOf(this)
    } else if (this.start == other.endInclusive) {
        listOf(IntRange(this.start+1, this.end))
    } else if (this.start < other.endInclusive && this.start >= other.start ) {
        listOf(IntRange(other.endInclusive + 1, this.endInclusive))
    } else if (this.endInclusive >= other.start) {
        listOf(IntRange(this.start, other.start - 1))
    } else {
        listOf(this)
    }
}