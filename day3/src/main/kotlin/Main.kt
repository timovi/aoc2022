fun main() {
    println("Puzzle one: ${calculatePriorities(input, ::splitToHalvedLines)}")
    println("Puzzle two: ${calculatePriorities(input, ::splitToGroupsOfThreeLines)}" )
}

fun calculatePriorities(
    input: String,
    reorganiseWith: (List<String>) -> List<List<String>>
) =
    input
        .split("\n")
        .let(reorganiseWith)
        .sumOf {
            it.let(::findCommonChar).let(::charPriority) }

fun findCommonChar(compartments: List<String>) =
    compartments
        .drop(1)
        .fold(compartments.first().toSet()) { commonItems, currentItem -> currentItem.toSet().intersect(commonItems) }
        .first()

fun charPriority(chr: Char) =
    if (('a'..'z').contains(chr)) {
        chr.code - 97 + 1
    } else {
        chr.code - 65 + 27
    }

// Puzzle one
fun splitToHalvedLines(input: List<String>) =
    input.map { listOf(it.take(it.length/2), it.takeLast(it.length/2)) }

// Puzzle two
fun splitToGroupsOfThreeLines(input: List<String>) =
    input.chunked(3)