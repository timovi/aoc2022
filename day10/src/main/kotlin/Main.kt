fun main() {
    val input = {}::class.java.getResource("input.txt").readText()
    val state = input
        .split("\r\n")
        .runningFold(listOf(1)) { currentValue, line ->
            runCommand(line, currentValue.last())
        }.flatten().dropLast(1)

    println("Puzzle one: ${20*state[20-1] + 60*state[60-1] + 100*state[100-1] + 140*state[140-1] + 180*state[180-1] + 220*state[220-1]}")
    println("Puzzle two:")
    state.chunked(40).forEach { printLine(it) }
}

fun runCommand(commandLine: String, currentValue: Int): List<Int> {
    val commandAndValue = commandLine.split (" ")
    return when (commandAndValue.first()) {
        "addx" -> listOf(currentValue, currentValue + commandAndValue.last().toInt())
        else -> listOf(currentValue)
    }
}

fun printLine(line: List<Int>) {
    line.forEachIndexed { index, state ->
        when(index) {
            state-1, state, state+1 -> print("#")
            else -> print(".")
        }
    }
    println()
}