fun main() {
    println("Puzzle one: ${calculatePriorities(input, ::splitToHalvedLines)}")
    println("Puzzle two: ${calculatePriorities(input, ::splitToGroupsOfThreeLines)}" )
}

fun calculatePriorities(
    input: String,
    splitWith: (String) -> List<List<String>>
) = splitWith(input).sumOf { charPriority(findCommonChar(it)) }

fun findCommonChar(compartments: List<String>)  =
    compartments
        .drop(1)
        .fold(compartments.first()) { commonItems, currentItem -> findCommonChars(commonItems, currentItem) }
        .first()

fun findCommonChars(str1: String, str2: String) =
    str1.filter { str2.contains(it) }

fun charPriority(chr: Char) =
    if (('a'..'z').contains(chr)) {
        chr.code - 97 + 1
    } else {
        chr.code - 65 + 27
    }

// Puzzle one
fun splitToHalvedLines(input: String) =
    input.split("\n").map { it.splitInHalf() }

fun String.splitInHalf() =
    listOf(this.take(this.length/2), this.takeLast(this.length/2))

// Puzzle two
fun splitToGroupsOfThreeLines(input: String) =
    input.split("\n").chunked(3)