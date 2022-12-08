data class TreeWithVisibility (
    val height: Int,
    val north: List<Int>,
    val south: List<Int>,
    val west: List<Int>,
    val east: List<Int>
)

fun main() {
    val input = {}::class.java.getResource("input.txt").readText()
    val treeVisibilities = parseTreesVisibleFrom(input)

    println("Puzzle one: ${treeVisibilities.count { isVisible(it) }}")
    println("Puzzle two: ${treeVisibilities.maxOf { scenicScoreOf(it) }}")
}

fun parseTreesVisibleFrom(input: String) : List<TreeWithVisibility> {
    val trees = input.split("\r\n").map { row -> row.map { it.digitToInt() } }
    return trees.mapIndexed { y, row ->
            row.mapIndexed { x, _ -> treeWithVisibility(y, x, trees) }
        }.flatten()
}

fun treeWithVisibility(y: Int, x: Int, trees: List<List<Int>>) : TreeWithVisibility {
    val row = trees[y]
    val col = trees.map {it[x]}
    return TreeWithVisibility(
        trees[y][x], col.take(y), col.drop(y + 1), row.take(x), row.drop(x + 1))
}

fun isVisible(tree: TreeWithVisibility) =
    ((tree.north.maxOrNull() ?: -1) < tree.height
    || (tree.west.maxOrNull() ?: -1) < tree.height
    || (tree.south.maxOrNull() ?: -1) < tree.height
    || (tree.east.maxOrNull() ?: -1) < tree.height)

fun scenicScoreOf(tree: TreeWithVisibility) =
    viewingDistance(tree.height, tree.north.reversed()) *
    viewingDistance(tree.height, tree.west.reversed()) *
    viewingDistance(tree.height, tree.south) *
    viewingDistance(tree.height, tree.east)

fun viewingDistance(height: Int, trees: List<Int>) : Int {
    val blockingTreeIndex = trees.indexOfFirst { it >= height }
    return when {
        trees.isEmpty() -> 0
        blockingTreeIndex == -1 -> trees.size
        else -> blockingTreeIndex + 1
    }
}