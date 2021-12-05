import kotlin.math.abs

data class Point(var x: Int, var y: Int) {
    operator fun plusAssign(other: Point) {
        x += other.x
        y += other.y
    }
}

fun main() {
    fun parsePoint(it: String): Point {
        val values = it.split(',')
        return Point(values[0].toInt(), values[1].toInt())
    }

    fun onlyHorizontalAndVerticalLines(it: Pair<Point, Point>) = it.first.x == it.second.x || it.first.y == it.second.y

    fun visit(
        curr: Point,
        visitedOnce: HashSet<Point>,
        visitedTwice: HashSet<Point>
    ) {
        if (visitedOnce.contains(curr)) {
            visitedTwice.add(curr.copy())
        } else {
            visitedOnce.add(curr.copy())
        }
    }

    fun solve(input: List<String>, filterCondition: (value: Pair<Point, Point>) -> Boolean): Int {
        val visitedOnce = HashSet<Point>()
        val visitedTwice = HashSet<Point>()
        val intervals = input.map { it.split(" -> ") }
            .map { Pair(parsePoint(it[0]), parsePoint(it[1])) }
            .filter { filterCondition(it) }
        for (interval in intervals) {
            val (start, end) = interval
            val diff = Pair(end.x - start.x, end.y - start.y)
            val max = maxOf(abs(diff.first), abs(diff.second))
            val diffNormalized = Point(diff.first / max, diff.second / max)
            val curr = start
            while (curr != end) {
                visit(curr, visitedOnce, visitedTwice)
                curr += diffNormalized
            }
            visit(curr, visitedOnce, visitedTwice)
        }

        return visitedTwice.size
    }

    fun part1(input: List<String>): Int {
        return solve(input, ::onlyHorizontalAndVerticalLines)
    }

    fun part2(input: List<String>): Int {
        return solve(input) { true }
    }

    val testInput = readInput("input_test")
    check(part1(testInput) == 5)
    check(part2(testInput) == 12)
    val input = readInput("input")
    println(part1(input))
    println(part2(input))
}
