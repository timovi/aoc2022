typealias Position = Pair<Int, Int>
typealias Rope = List<Position>
typealias Instruction = Pair<String, Int>

fun main() {
    val input = {}::class.java.getResource("input.txt").readText()
    val instructions = input
        .split("\r\n")
        .map { line ->
            val (first, second) = line.split (" ")
            Instruction(first, second.toInt())
        }

    println("Puzzle one: ${recordRopeMoves(instructions, 2).map { it.last() }.toSet().size}")
    println("Puzzle two: ${recordRopeMoves(instructions, 10).map { it.last() }.toSet().size}")
}

fun recordRopeMoves(instructions: List<Instruction>, ropeLength: Int): List<Rope> {
    val initialPositions = (1..ropeLength).map { Position(0, 0) }
    return instructions
        .fold(listOf(initialPositions)) { ropePositions, instruction ->
            ropePositions + recordMoves(ropePositions.last(), instruction)
        }.drop(1)
}

fun recordMoves(rope: Rope, instruction: Instruction) =
    (1..instruction.second)
        .fold(listOf(rope)) { previousRopePositions, _ ->
            previousRopePositions + listOf(previousRopePositions.last().move(instruction.first)) }
        .drop(1)

fun Rope.move(direction: String) : Rope {
    val newHeadPosition = this.first().moveTo(direction)
    return this
        .drop(1)
        .runningFold(Pair(this.first(), newHeadPosition)) { previousKnotPositions, currentKnotPosition ->
            Pair(
                currentKnotPosition,
                currentKnotPosition
                    .moveTowards(previousKnotPositions.second))
        }
        .map { it.second }
}

fun Position.moveTo(direction: String) =
    when (direction) {
        "U" -> Position(this.first, this.second + 1)
        "D" -> Position(this.first, this.second - 1)
        "L" -> Position(this.first - 1, this.second)
        "R" -> Position(this.first + 1, this.second)
        else -> this
    }

fun Position.moveTowards(position: Position) =
    when (Pair(position.first - this.first, position.second - this.second)) {
        Pair( 2, 0)                           -> Pair(this.first + 1, this.second)
        Pair( 2, 1), Pair( 1, 2), Pair( 2, 2) -> Pair(this.first + 1, this.second + 1)
        Pair( 0, 2)                           -> Pair(this.first,     this.second + 1)
        Pair(-1, 2), Pair(-2, 1), Pair(-2, 2) -> Pair(this.first - 1, this.second + 1)
        Pair(-2, 0)                           -> Pair(this.first - 1, this.second)
        Pair(-2,-1), Pair(-1,-2), Pair(-2,-2) -> Pair(this.first - 1, this.second - 1)
        Pair( 0,-2)                           -> Pair(this.first,     this.second - 1)
        Pair( 1,-2), Pair( 2,-1), Pair( 2,-2) -> Pair(this.first + 1, this.second - 1)
        else -> this
    }