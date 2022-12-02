fun main() {
    println("Part one: ${calculateResult(input, ::pointsForMoveInPartOne, ::pointsForResultInPartOne)}")
    println("Part two: ${calculateResult(input, ::pointsForMoveInPartTwo, ::pointsForResultInPartTwo)}")
}


fun calculateResult(
    input: String,
    pointsForMove: (Pair<Char, Char>) -> Int,
    pointsForResult: (Pair<Char, Char>) -> Int
) =
    input
        .split("\n")
        .map {
            val moves = Pair(it.first(), it.last())
            pointsForMove(moves) + pointsForResult(moves)
        }
        .sum()

fun pointsForMoveInPartOne(move: Pair<Char, Char>): Int =
    when (move.second) {
        'X' -> 1
        'Y' -> 2
        'Z' -> 3
        else -> 0
    }

fun pointsForResultInPartOne(moves: Pair<Char, Char>): Int =
    when (moves) {
        Pair('A', 'Y') -> 6
        Pair('B', 'Z') -> 6
        Pair('C', 'X') -> 6
        Pair('A', 'X') -> 3
        Pair('B', 'Y') -> 3
        Pair('C', 'Z') -> 3
        else -> 0
    }

fun pointsForMoveInPartTwo(move: Pair<Char, Char>): Int =
    when (move) {
        Pair('A','X') -> 3
        Pair('B','X') -> 1
        Pair('C','X') -> 2
        Pair('A','Y') -> 1
        Pair('B','Y') -> 2
        Pair('C','Y') -> 3
        Pair('A','Z') -> 2
        Pair('B','Z') -> 3
        Pair('C','Z') -> 1
        else -> 0
    }

fun pointsForResultInPartTwo(moves: Pair<Char, Char>): Int =
    when (moves.second) {
        'X' -> 0
        'Y' -> 3
        'Z' -> 6
        else -> 0
    }