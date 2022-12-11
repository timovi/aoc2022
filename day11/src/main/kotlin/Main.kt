data class Monkey (
    val items: List<Long>,
    val operation: String,
    val dividedBy: Int,
    val throwsTo: Pair<Int, Int>,
    val inspectedCount: Int
)

fun main() {
    val input = {}::class.java.getResource("input.txt").readText()
    val initialState = parseInitialState(input)

    val endState1 = (1..20)
        .scan(initialState) { state, _ ->
            processRound({ level -> level / 3 }, state) }
        .last()
        .sortedByDescending { it.inspectedCount }
    println("Puzzle one: ${endState1[0].inspectedCount * endState1[1].inspectedCount}")

    val maxWorryLevel = initialState.fold(1) { previous, current -> previous * current.dividedBy }
    val endState2 = (1..10000)
        .scan(initialState) { state, _ ->
            processRound({ level -> level % maxWorryLevel }, state) }
        .last()
        .sortedByDescending { it.inspectedCount }

    println("Puzzle two: ${endState2[0].inspectedCount.toLong() * endState2[1].inspectedCount.toLong()}")
}

fun parseInitialState(input: String) =
    input
        .split("\r\n")
        .filter { it.isNotEmpty() && !it.startsWith("Monkey") }
        .chunked(5)
        .map { (startingItems, operation, test, ifTrue, ifFalse) ->
            Monkey(
                startingItems.drop(18).split(", ").map { it.toLong() },
                operation.drop(23),
                test.drop(21).toInt(),
                Pair(
                    ifTrue.drop(29).toInt(),
                    ifFalse.drop(30).toInt()),
                0)
        }

fun processRound(handleWorryLevel: (Long) -> Long, monkeys: List<Monkey>) =
    monkeys.scanIndexed(monkeys) { index, currentState, _ ->
        processTurn(handleWorryLevel, currentState, index)
    }.last()

fun processTurn(handleWorryLevel: (Long) -> Long, monkeys: List<Monkey>, currentMonkeyIndex: Int): List<Monkey> {
    val monkey = monkeys[currentMonkeyIndex]
    val newState = mutableListOf<Monkey>()
    newState.addAll(monkeys)

    monkey.items.map {
        val newItemValue = handleWorryLevel(testWorryLevel(monkey.operation, it))
        val newMonkeyIndex = when (newItemValue % monkey.dividedBy) {
            0L -> monkey.throwsTo.first
            else -> monkey.throwsTo.second
        }
        newState[currentMonkeyIndex] = newState[currentMonkeyIndex].markFirstItemInspected()
        newState[newMonkeyIndex] = newState[newMonkeyIndex].addItem(newItemValue)
    }

    return newState
}

fun testWorryLevel(operation: String, item: Long): Long {
    val (operator, operand) = operation.split(" ")
    val operandValue = when (operand) {
        "old" -> item
        else -> operand.toLong()
    }
    return when (operator) {
        "*" -> item * operandValue
        else -> item + operandValue
    }
}

fun Monkey.markFirstItemInspected() =
    Monkey(this.items.drop(1), this.operation, this.dividedBy, this.throwsTo, this.inspectedCount + 1)

fun Monkey.addItem(item: Long) =
    Monkey(this.items + item, this.operation, this.dividedBy, this.throwsTo, this.inspectedCount)
