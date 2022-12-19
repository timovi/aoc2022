data class Coordinate(val x: Int, val y: Int)

val scanRanges = mutableMapOf<Int, MutableList<IntRange>>()
val beacons = mutableMapOf<Int, MutableList<Int>>()

fun main() {
    val input = {}::class.java.getResource("input.txt").readText()
    val inspectedRow = 2000000
    parseSensors(input, inspectedRow, inspectedRow)

    println("Puzzle one: ${unknownXCoordinatesThatCannotContainBeacon(inspectedRow).size}")

    scanRanges.clear()
    beacons.clear()

    val min = 0
    val max = 4000000
    parseSensors(input, min, max)
    val signal = coordinateThatContainsDistressSignal(min..max)
    println("Puzzle two: ${signal!!.x.toLong() * 4000000 + signal!!.y.toLong()}")
}

fun parseSensors(input: String, minYForScanning: Int, maxYForScanning: Int) =
    input
        .split("\r\n")
        .forEach {
            val match =
                Regex("Sensor at x=(-?\\d+), y=(-?\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)").find(it)!!
            val (sensorX, sensorY, beaconX, beaconY) = match.destructured
            val sensorCoordinate = Coordinate(sensorX.toInt(), sensorY.toInt())
            val beaconCoordinate = Coordinate(beaconX.toInt(), beaconY.toInt())
            val distance =
                Math.abs(sensorCoordinate.x - beaconCoordinate.x) + Math.abs(sensorCoordinate.y - beaconCoordinate.y)
            addToScanRanges(sensorCoordinate, distance, minYForScanning, maxYForScanning)
            addToBeacons(beaconCoordinate)
        }

fun addToScanRanges(coordinate: Coordinate, distance: Int, minY: Int, maxY: Int) {
    val minYForRange = Math.max(minY, coordinate.y - distance)
    val maxYForRange = Math.min(maxY, coordinate.y + distance)
    for (y in minYForRange .. maxYForRange) {
        val xRange = coveredXCoordinatesIn(coordinate, distance, y)

        if (!xRange.isEmpty()) {
            if (scanRanges.contains(y)) {
                scanRanges[y]?.add(xRange)
            } else {
                scanRanges[y] = mutableListOf(xRange)
            }
        }
    }
}

fun addToBeacons(coordinate: Coordinate) {
    if (beacons.contains(coordinate.y)) {
        beacons[coordinate.y]?.add(coordinate.x)
    } else {
        beacons[coordinate.y] = mutableListOf(coordinate.x)

    }
}

fun unknownXCoordinatesThatCannotContainBeacon(y: Int) : Set<Int> {
    val coveringSensors = scanRanges[y]!!.flatten().toSet()
    val coveringBeacons = beacons[y]!!.toSet()
    return (coveringSensors - coveringBeacons)
}

fun coordinateThatContainsDistressSignal(coordinateRange: IntRange) : Coordinate? {
    for (y in coordinateRange.first.. coordinateRange.last) {
        val coveringSensors = scanRanges[y]!!.map { it.clamp(coordinateRange.first, coordinateRange.last) }
        val endpoints = coveringSensors.flatMap { setOf(it.first, it.last) }.toSet().sorted()

        if (endpoints.first() != coordinateRange.first) {
            return Coordinate(coordinateRange.first, y)
        } else if (endpoints.last() != coordinateRange.last) {
            return Coordinate(coordinateRange.last, y)
        } else {
            val notKnownXCoordinate = endpoints
                .drop(1)
                .dropLast(1)
                .map { it + 1 }
                .firstOrNull {endpoint ->
                    coveringSensors.none { it.contains(endpoint) }
                }

            if (notKnownXCoordinate != null) {
                return Coordinate(notKnownXCoordinate, y)
            }
        }
    }
    return null
}

fun coveredXCoordinatesIn(position: Coordinate, distance: Int, y: Int): IntRange {
    val yDifference = Math.abs(position.y - y)
    if (distance < yDifference) {
            return IntRange.EMPTY
    }

    val xDifference = distance - yDifference
    return position.x - xDifference .. position.x + xDifference
}

fun IntRange.clamp(min: Int, max: Int): IntRange {
    return IntRange(
        Math.max(min, this.first),
        Math.min(max, this.last))
}