data class Coordinate(val x: Int, val y: Int)
data class Node(
    val coordinate: Coordinate,
    val nextCoordinates: List<Coordinate>,
    val depth: Int)

fun main() {
    val input = {}::class.java.getResource("input.txt").readText()

    val map = input.split("\r\n")
    val start = findCoordinateOf('S', map)!!

    //var foo = findPaths(map, emptyList(), start)

    println("Puzzle one: ${findPaths(map, setOf(start), listOf(start), 0)}")
    println("Puzzle two: ")
}

tailrec fun findPaths(map: List<String>, allVisited: Set<Coordinate>, current: List<Coordinate>, level: Int): Int {
    if (current.any { it.isEnd(map) }) {
        return level
    }

    val next = mutableListOf<Coordinate>()
    current.map { coordinate ->
        val up    = Coordinate(coordinate.x, coordinate.y-1)
        val left  = Coordinate(coordinate.x-1, coordinate.y);
        val down  = Coordinate(coordinate.x, coordinate.y+1);
        val right = Coordinate(coordinate.x+1, coordinate.y);

        if (coordinate.canTravelTo(map, up) && !allVisited.contains(up)) {
            next.add(up)
        }
        if (coordinate.canTravelTo(map, left) && !allVisited.contains(left)) {
            next.add(left)
        }
        if (coordinate.canTravelTo(map, down) && !allVisited.contains(down)) {
            next.add(down)
        }
        if (coordinate.canTravelTo(map, right) && !allVisited.contains(right)) {
            next.add(right)
        }
    }

    return findPaths(map, allVisited + current, next,level + 1)
}

fun findCoordinateOf(c: Char, map: List<String>): Coordinate? {
    for (y in map.indices) {
        for (x in map[y].indices) {
            if (map[y][x] == c) {
                return Coordinate(x, y)
            }
        }
    }
    return null
}

fun Coordinate.canTravelTo(map: List<String>, coordinate: Coordinate): Boolean {
    return if (!map.indices.contains(coordinate.y) || !map[coordinate.y].indices.contains(coordinate.x)) {
        false
    } else {
        val currentElevation = when (val currentChar = map[this.y][this.x]) {
            'S' -> 'a'.code
            else -> currentChar.code
        }
        val nextElevation = when (val nextChar = map[coordinate.y][coordinate.x]) {
            'E' -> 'z'.code
            else -> nextChar.code
        }
        nextElevation - currentElevation <= 1
    }
}

fun Coordinate.isEnd(map: List<String>) =
    map[this.y][this.x] == 'E'
