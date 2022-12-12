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

    var nodes = mutableMapOf<Coordinate, Node>()
    nodes.putAll(mapToNodes(map, setOf('S')).map { it.coordinate to it })
    println("Puzzle one: ${findPaths(nodes, nodes.values.filter { it.distance == 0 })}")

    nodes = mutableMapOf()
    nodes.putAll(mapToNodes(map, setOf('S','a')).map { it.coordinate to it })
    val start = nodes.values.filter { it.distance == 0 }
    println("Puzzle two: ${findPaths(nodes, nodes.values.filter { it.distance == 0 })}")
}

fun mapToNodes(map: List<String>, initialChars: Set<Char>) =
    map.flatMapIndexed() { y, row ->
        row.mapIndexed { x, char ->
            val distance = when (initialChars.contains(char)) {
                true -> 0
                false -> 999999
            }
            Coordinate(x, y).toNode(map, distance)
        }
    }

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

fun Coordinate.toNode(map: List<String>, distance: Int) : Node {
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

    return Node(map[this.y][this.x],this, next, distance, false)
}

fun Node.isEnd() = this.char == 'E'
