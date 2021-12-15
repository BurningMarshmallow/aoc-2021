import java.util.*

// Based on https://github.com/AxelUser/aoc-2021/blob/master/src/main/kotlin/solutions/day15/Chiton.kt
fun main() {
    data class State(val point: Pair<Int, Int>, val risk: Int)

    fun List<List<Int>>.getRisk(point: Pair<Int, Int>): Int {
        val (y, x) = point
        val length = this.count()
        val width = this[0].count()

        val shift = y / length + x / width
        return ((this[y % length][x % width] - 1 + shift) % 9) + 1
    }

    fun List<List<Int>>.bottomRight(scale: Int = 1): Pair<Int, Int> {
        return size * scale - 1 to size * scale - 1
    }

    fun dijkstra(
        start: Pair<Int, Int>,
        destination: Pair<Int, Int>,
        getAdjacent: (Pair<Int, Int>) -> Sequence<State>
    ): Int {
        val risks = mutableMapOf(start to 0)
        val pq = PriorityQueue<State>(compareBy { it.risk })
        pq.add(State(start, 0))

        while (pq.isNotEmpty()) {
            val closest = pq.remove()
            for (adj in getAdjacent(closest.point)) {
                val newRisk = adj.risk + closest.risk
                if (risks.getOrDefault(adj.point, Int.MAX_VALUE) > newRisk) {
                    risks[adj.point] = newRisk
                    pq.add(State(adj.point, newRisk))
                }
            }
        }

        return risks.getValue(destination)
    }

    fun List<List<Int>>.getNeighbours(point: Pair<Int, Int>, scale: Int = 1): Sequence<Pair<Int, Int>> {
        val (i, j) = point
        val height = this.count() * scale
        val width = this[0].count() * scale
        return sequence {
            if (i > 0) yield(i - 1 to j)
            if (i < (height - 1)) yield(i + 1 to j)
            if (j > 0) yield(i to j - 1)
            if (j < (width - 1)) yield(i to j + 1)
        }
    }

    fun solve(input: List<String>, scale: Int = 1): Int {
        val cave = input.map { it.map(Char::digitToInt) }
        return dijkstra(0 to 0, cave.bottomRight(scale)) { point ->
            cave.getNeighbours(point, scale).map { adj -> State(adj, cave.getRisk(adj)) }
        }
    }

    fun part1(input: List<String>): Int {
        return solve(input)
    }

    fun part2(input: List<String>): Int {
        return solve(input, 5)
    }

    val testInput = readInput("input_test")
    check(part1(testInput) == 40)
    check(part2(testInput) == 315)
    val input = readInput("input")
    println(part1(input))
    println(part2(input))
}
