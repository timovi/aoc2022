data class Coordinate( val x: Int, val y: Int)

fun main() {
    val input = {}::class.java.getResource("input.txt").readText()
    println("Puzzle one: ${dropSand(buildMap(input, false))}")
    println("Puzzle two: ${dropSand(buildMap(input, true))}")
}

fun buildMap(input: String, hasFloorBeneath: Boolean) : MutableList<MutableList<Char>> {
    val rockCoordinatesFromInput = input
        .split("\r\n")
        .flatMap { rock ->
            val rockEndCoordinates = rock
                .split(" -> ")
                .map {
                    val (first, second) = it.split(",")
                    Coordinate(first.toInt(), second.toInt())
                }
            rockEndCoordinates
                .drop(1)
                .fold(listOf(rockEndCoordinates.first())) { previous, current ->
                    previous + line(previous.last(), current)
                }
        }

    val maxX = 500 * 2
    val maxYFromInput = rockCoordinatesFromInput.maxBy { it.y }.y

    val rockCoordinates = when (hasFloorBeneath) {
        true -> rockCoordinatesFromInput + line(Coordinate(0, maxYFromInput + 2), Coordinate(maxX, maxYFromInput + 2))
        false -> rockCoordinatesFromInput
    }

    val maxY = rockCoordinates.maxBy { it.y }.y

    val map =  mutableListOf<MutableList<Char>>()

    for (y in 0.. maxY) {
        val row = mutableListOf<Char>()
        for (x in 0 .. maxX) {
            when (rockCoordinates.contains(Coordinate(x,y))) {
                true -> row.add('#')
                false -> row.add('.')
            }
        }
        map.add(row)
    }

    return map
}

fun line(start: Coordinate, end: Coordinate) : List<Coordinate> {
    return if (start.x > end.x) {
        ((end.x - start.x) .. 0).map { Coordinate(start.x + it , start.y) }.reversed()
    } else if (start.x < end.x) {
        (0 .. (end.x - start.x)).map { Coordinate(start.x + it , start.y) }
    } else if (start.y > end.y) {
        ((end.y - start.y) .. 0).map { Coordinate(start.x , start.y + it) }.reversed()
    } else if (start.y < end.y) {
        (0 .. (end.y - start.y)).map { Coordinate(start.x , start.y + it) }
    } else {
        return listOf(start, end)
    }
}

fun dropSand(map: MutableList<MutableList<Char>>): Int {
    val start = Coordinate(500, 0)
    var units = 0
    var rest = findRest(map, start)
    while (rest != null) {
        map[rest.y][rest.x] = 'o'
        units++

        if (rest == start) {
            break
        } else {
            rest = findRest(map, start)
        }
    }
    return units
}

fun findRest(map: List<List<Char>>, currentPosition: Coordinate): Coordinate? {
    val down  = Coordinate(currentPosition.x, currentPosition.y + 1)
    val left  = Coordinate(currentPosition.x - 1, currentPosition.y + 1)
    val right = Coordinate(currentPosition.x + 1, currentPosition.y + 1)
    val next  = listOf(down, left, right).firstOrNull { it.isVacant(map) }

    return if (next == null) {
        currentPosition
    } else if (next.isOutOfBounds(map)) {
        null
    } else {
        findRest(map, next)
    }
}

fun Coordinate.isVacant(map: List<List<Char>>): Boolean {
    if (this.isOutOfBounds(map)) {
        return true
    }
    return map[this.y][this.x] == '.'
}

fun Coordinate.isOutOfBounds(map: List<List<Char>>): Boolean {
    return this.x < 0
            || this.y < 0
            || this.x >= map.first().size
            || this.y >= map.size
}