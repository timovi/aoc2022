import org.json.JSONArray

fun main() {
    val input = {}::class.java.getResource("input.txt").readText()

    val pairs = input.split("\r\n\r\n").map { toPair(it) }
    val orderedIndexes1 = pairs.mapIndexed { index, pair ->
        when (compare(pair.first, pair.second) < 1) {
            true -> index + 1
            false -> -1
        }
    }.filter { it != -1 }
    println("Puzzle one: ${orderedIndexes1.sum()}")

    val divider1 = "[[2]]"
    val divider2 = "[[6]]"
    val ordered = ("$input\r\n$divider1\r\n$divider2")
        .split("\r\n")
        .filter { it.any() }
        .map { JSONArray(it) }
        .sortedWith { first, second -> compare(first, second) }
        .map { it.toString() }
    println("Puzzle two: ${(ordered.indexOf(divider1) + 1) * (ordered.indexOf(divider2) + 1)}")
}

fun compare(first: JSONArray, second: JSONArray) : Int {
    if (first.isEmpty && second.isEmpty ) {
        return 0
    } else if (first.isEmpty) {
        return -1
    } else if (second.isEmpty) {
        return 1
    }

    val maybeArray1: JSONArray? = first.optJSONArray(0)
    val maybeValue1: Int = first.optInt(0, -1)

    val maybeArray2: JSONArray? = second.optJSONArray(0)
    val maybeValue2: Int = second.optInt(0, -1)

    if (maybeValue1 != -1 && maybeValue2 != -1 ) {
        val result = maybeValue1 - maybeValue2
        if (result != 0) {
            return result
        }
    } else if (maybeArray1 != null && maybeArray2 != null) {
        val result = compare(maybeArray1, maybeArray2)
        if (result != 0) {
            return result
        }
    } else if (maybeArray1 != null) {
        val result = compare(maybeArray1, JSONArray(listOf(maybeValue2)))
        if (result != 0) {
            return result
        }
    } else if (maybeArray2 != null) {
        val result = compare(JSONArray(listOf(maybeValue1)), maybeArray2)
        if (result != 0) {
            return result
        }
    }

    return compare(JSONArray(first.drop(1)), JSONArray(second.drop(1)))
}

fun toPair(input: String): Pair<JSONArray, JSONArray> {
    val (first, second) = input.split("\r\n")
    return Pair(JSONArray(first), JSONArray(second))
}