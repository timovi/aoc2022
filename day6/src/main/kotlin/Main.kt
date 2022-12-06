fun main() {
    val input = {}::class.java.getResource("input.txt").readText()
    println("Puzzle one: ${indexOfFirstUniqueString(4, input)}")
    println("Puzzle two: ${indexOfFirstUniqueString(14, input)}")
}

fun indexOfFirstUniqueString(windowSize: Int, input: String) =
    windowSize + input
        .windowed(windowSize, 1)
        .indexOfFirst { it.toCharArray().toSet().size == windowSize }