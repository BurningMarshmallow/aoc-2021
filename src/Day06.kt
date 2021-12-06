fun main() {
    fun solve(input: List<String>, numberOfDays: Int): Long {
        val init = input[0].split(',').map(Integer::parseInt)
            .groupingBy { it }
            .eachCount()
        val curr = (0..8).associateWith { (init[it] ?: 0).toLong() }.toMutableMap()

        for (day in 0..numberOfDays) {
            val dying = curr[0]!!
            for (value in 0..5) {
                curr[value] = curr[value + 1]!!
            }
            curr[6] = dying + curr[7]!!
            curr[7] = curr[8]!!
            curr[8] = dying
        }

        return curr.values.sum()
    }

    fun part1(input: List<String>): Long {
        return solve(input, 79)
    }

    fun part2(input: List<String>): Long {
        return solve(input, 255)
    }

    val testInput = readInput("input_test")
    check(part1(testInput) == 5934.toLong())
    check(part2(testInput) == 26984457539)
    val input = readInput("input")
    println(part1(input))
    println(part2(input))
}
