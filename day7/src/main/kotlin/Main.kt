data class Node(
    val name: String,
    val size: Int,
    val parent: Node?,
    val children: MutableList<Node>
)

fun main() {
    val input = {}::class.java.getResource("input.txt").readText()
    val root = parseVolume(input)

    val dirNamesAndSizes = flatten(root).filter { it.children.any() }.map { Pair(it.name, sizeOf(it)) }
    println("Puzzle one: ${ dirNamesAndSizes.filter { it.second <= 100000 }.sumOf { it.second }}")

    val spacedNeededToFreeUp = -1 * (70000000 - 30000000 - dirNamesAndSizes.first().second)
    println("Puzzle two: ${ dirNamesAndSizes.filter { it.second >= spacedNeededToFreeUp }.minBy { it.second }.second }")
}

fun parseVolume(input: String) : Node {
    var root = Node("/", 0, null, mutableListOf())
    input
        .split("\r\n")
        .drop(1)
        .fold(root) { currentNode, line -> parseLine(line, currentNode) }
    return root
}

fun parseLine(line: String, current: Node): Node {
    val output = line.split(" ")

    if (isInput(line) && output[1] == "cd") {
        return cd(current, output[2])
    }

    if (!isInput(line)) {
        current.children.add(addNode(current, output[0], output[1]))
    }

    return current
}

fun isInput(line: String) = line.startsWith("$")

fun cd(current: Node, to: String) =
    when (to) {
        ".." -> current.parent ?: current
        else -> current.children.first { it.name == to }
    }

fun addNode(parent: Node, dirOrFilesize: String, name: String) =
    when (dirOrFilesize) {
        "dir" -> Node(name, 0, parent, mutableListOf())
        else -> Node(name, dirOrFilesize.toInt(), parent, mutableListOf())
    }

fun flatten(node: Node): List<Node> {
    return if (node.children.isEmpty()) {
        listOf(node)
    } else {
        listOf(node) + node.children.flatMap { flatten(it) }
    }
}

fun sizeOf(node: Node): Int {
    return if (node.children.isEmpty()) {
        node.size
    } else {
        node.children.sumOf { sizeOf(it) }
    }
}