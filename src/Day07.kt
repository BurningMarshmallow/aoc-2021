import kotlin.math.abs
import kotlin.math.min

fun spendConstantFuel(distance: Int): Int {
    return distance
}

fun spendIncreasingFuel(distance: Int): Int {
    return distance * (distance + 1) / 2
}

fun main() {
    fun solve(input: List<String>, spendFuel: (Int) -> Int): Int {
        val init = input[0].split(',').map(Integer::parseInt)
        var answer = Integer.MAX_VALUE
        for (bestPosition in init.minOf { it } .. init.maxOf { it } ) {
            val fuel = init.sumOf { spendFuel(abs(it - bestPosition)) }
            answer = min(fuel, answer)
        }
        return answer
    }

    fun part1(input: List<String>): Int {
        return solve(input, ::spendConstantFuel)
    }

    fun part2(input: List<String>): Int {
        return solve(input, ::spendIncreasingFuel)
    }

    val testInput = readInput("input_test")
    check(part1(testInput) == 37)
    check(part2(testInput) == 168)
    val input = readInput("input")
    println(part1(input))
    println(part2(input))
}
