import kotlin.math.abs

const val FLOOR_SIZE = 1000

data class Point(val x: Int, val y: Int)

fun main() {
    fun parsePoint(it: String): Point {
        val values = it.split(',')
        return Point(values[0].toInt(), values[1].toInt())
    }

    fun onlyHorizontalAndVerticalLines(it: Pair<Point, Point>) = it.first.x == it.second.x || it.first.y == it.second.y

    fun solve(input: List<String>, filterCondition: (value: Pair<Point, Point>) -> Boolean): Int {
        val floor = Array(FLOOR_SIZE) { Array(FLOOR_SIZE) { 0 } }
        val intervals = input.map {
            it.split(" -> ")
        }.map {
            Pair(parsePoint(it[0]), parsePoint(it[1]))
        }.filter {
                filterCondition(it)
            }
        for (interval in intervals) {
            val (start, end) = interval
            val diff = Pair(end.x - start.x, end.y - start.y)
            val max = maxOf(abs(diff.first), abs(diff.second))
            val diffNormalized = Pair(diff.first / max, diff.second / max)
            var i = start.x
            var j = start.y
            while (i != end.x || j != end.y) {
                floor[i][j]++
                i += diffNormalized.first
                j += diffNormalized.second
            }
            floor[i][j]++
        }

        return floor.flatten().map { if (it > 1) 1 else 0 }.sum()
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
