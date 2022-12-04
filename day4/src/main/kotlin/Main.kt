fun main() {
    println("Puzzle one: ${calculateOverlappingPairs(input, ::containsAllOrIsContainedFullyIn)}")
    println("Puzzle two: ${calculateOverlappingPairs(input, ::overlaps)}")
}

fun calculateOverlappingPairs(
    input: String,
    predicate: (IntRange, IntRange) -> Boolean
) =
    input
        .split("\n")
        .map { toRanges(it) }
        .filter { predicate(it.first, it.second) }
        .size

fun toRanges(row: String): Pair<IntRange, IntRange> {
    val (first, second) = row.split(",")
    return Pair(toRange(first), toRange(second))
}

fun toRange(rangeStr: String): IntRange {
    val (first, second) = rangeStr.split("-").map { it.toInt() }
    return first .. second
}

// Puzzle one
fun containsAllOrIsContainedFullyIn(first: IntRange, second: IntRange) =
    (first.contains(second.first) && first.contains(second.last))
            || (second.contains(first.first) && second.contains(first.last))

// Puzzle two
fun overlaps(first: IntRange, second: IntRange) =
    first.intersect(second).isNotEmpty()