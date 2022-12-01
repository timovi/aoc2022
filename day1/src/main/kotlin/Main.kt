fun main() {
    val totals = calculateTotals(input)
    println("Part one: ${totals.first()}")
    println("Part two: ${totals.take(3).sum()}")
}

fun calculateTotals(input: String) =
    input.split("\n")
        .fold(listOf(0)) { elves, caloriesStr ->
            when (caloriesStr) {
                "" -> elves + 0
                else -> {
                    val currentCalories = elves.last() + caloriesStr.toInt()
                    elves.dropLast(1) + currentCalories
                }
            }
        }
        .sortedDescending()