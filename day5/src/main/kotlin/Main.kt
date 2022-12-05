import java.util.*

typealias Instructions = List<Triple<Int, Int, Int>>
typealias Stacks = List<Deque<Char>>

fun main() {
    val input = {}::class.java.getResource("input.txt").readText()
    println("Puzzle one: ${moveCrates(input, ::usingCrateMover9000)}")
    println("Puzzle two: ${moveCrates(input, ::usingCrateMover9001)}")
}

fun moveCrates(
    input: String,
    moveStacksWith: (Instructions, Stacks) -> Unit
) : String {
    val lines = input.split("\r\n")
    val stacks = lines.takeWhile { it.isNotEmpty() }.let(::toStacks)
    val instructions = lines.takeLastWhile { it.isNotEmpty() }.map { parseInstruction(it) }

    moveStacksWith(instructions, stacks)
    return String(stacks.map { it.peek() }.toCharArray())
}

fun toStacks(rows: List<String>) : List<Deque<Char>> {
    var stacknumbers = rows.last()
    var stackIndices = ('1' .. '9')
        .map { stacknumbers.indexOf(it) }
        .filterNot { it == -1 }
    var stacks = stackIndices.map { ArrayDeque<Char>() }

    rows.dropLast(1)
        .reversed()
        .forEach { row ->
            stackIndices.forEachIndexed { index, indexOfChar ->
                if (row.getOrElse(indexOfChar) {' '}.isLetter()) {
                    stacks[index].push(row[indexOfChar])
                }
            }
        }

    return stacks
}

fun parseInstruction(instruction: String) : Triple<Int, Int, Int> {
    val match = Regex("move (\\d+) from (\\d+) to (\\d+)").find(instruction)!!
    val (times, from, to) = match.destructured
    return Triple(times.toInt(), from.toInt() - 1, to.toInt() - 1)
}

// Puzzle one
fun usingCrateMover9000(instructions: Instructions, stacks: Stacks) {
    instructions
        .forEach { (times, from, to) ->
            repeat(times) {
                val char = stacks[from].pop()
                stacks[to].push(char)
            }
        }
}

// Puzzle two
fun usingCrateMover9001(instructions: Instructions, stacks: Stacks) {
    instructions
        .forEach { (times, from, to) ->
            val crates = (1..times)
                .map { stacks[from].pop() }
                .reversed()
            crates
                .forEach { stacks[to].push(it) }
        }
}