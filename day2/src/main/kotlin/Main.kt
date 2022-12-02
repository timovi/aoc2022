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

fun pointsForMoveInPartOne(round: Pair<Char, Char>): Int =
    when (round.second) {
        'X' -> 1
        'Y' -> 2
        'Z' -> 3
        else -> 0
    }

fun pointsForResultInPartOne(round: Pair<Char, Char>): Int =
    when (round) {
        Pair('A', 'Y') -> 6
        Pair('B', 'Z') -> 6
        Pair('C', 'X') -> 6
        Pair('A', 'X') -> 3
        Pair('B', 'Y') -> 3
        Pair('C', 'Z') -> 3
        else -> 0
    }

fun pointsForMoveInPartTwo(round: Pair<Char, Char>): Int =
    when (round) {
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

fun pointsForResultInPartTwo(round: Pair<Char, Char>): Int =
    when (round.second) {
        'X' -> 0
        'Y' -> 3
        'Z' -> 6
        else -> 0
    }