import java.util.Deque

data class Coordinate(val x: Int, val y: Int)
data class Node(
    val char: Char,
    val coordinate: Coordinate,
    val neighbours: List<Coordinate>,
    val distance: Int,
    val visited: Boolean)

fun main() {
    val input = {}::class.java.getResource("input.txt").readText()

    val map = input.split("\r\n")

    val allNodes = mutableMapOf<Coordinate, Node>()
    allNodes.putAll(mapToNodes(map).map { it.coordinate to it })
    val start = allNodes.values.first { it.char == 'S' }

    println("Puzzle one: ${findPaths(allNodes, listOf(start))}")
    println("Puzzle two: ")
}

fun mapToNodes(map: List<String>) =
    map.flatMapIndexed() { y, row -> row.mapIndexed { x, _ -> Coordinate(x, y).toNode(map) }}

fun findPaths(allNodes: MutableMap<Coordinate, Node>, start: List<Node>) : Int {
    var current = start.first()
    val unvisited = mutableListOf<Node>()
    unvisited.addAll(allNodes.values)

    while (unvisited.any()) {
        current.neighbours.forEach { neighbour ->
            val node = allNodes[neighbour]!!
            val newDistance = Math.min(current.distance + 1, node.distance)
            val newNode = Node(node.char, node.coordinate, node.neighbours, newDistance, false)
            allNodes[neighbour] = newNode
            if (unvisited.contains(node)) {
                unvisited.remove(node)
                unvisited.add(newNode)
            }
        }
        if (current.isEnd()) {
            return current.distance
        } else {
            unvisited.remove(current)
            unvisited.sortBy { it.distance }
            current = unvisited.first()
        }
    }
    return 0
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

fun Coordinate.toNode(map: List<String>) : Node {
    val next = mutableListOf<Coordinate>()

    val up    = Coordinate(this.x, this.y-1)
    val left  = Coordinate(this.x-1, this.y);
    val down  = Coordinate(this.x, this.y+1);
    val right = Coordinate(this.x+1, this.y);

    if (this.canTravelTo(map, up)) {
        next.add(up)
    }
    if (this.canTravelTo(map, left)) {
        next.add(left)
    }
    if (this.canTravelTo(map, down)) {
        next.add(down)
    }
    if (this.canTravelTo(map, right)) {
        next.add(right)
    }

    val char = map[this.y][this.x]
    val distance = when(char) {
        'S' -> 0
        else -> 999999
    }
    return Node(char,this, next, distance, false)
}

fun Node.isEnd() = this.char == 'E'
