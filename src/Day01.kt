fun main() {
    fun countNumOfLarger(values: List<Int>): Int {
        var numOfLarger = 0
        var prev = values[0]
        for (value in values) {
            if (value > prev) {
                numOfLarger++
            }
            prev = value
        }
        return numOfLarger
    }

    fun part1(input: List<String>): Int {
        val values = input.map(Integer::parseInt)
        return countNumOfLarger(values)
    }

    fun part2(input: List<String>): Int {
        val values = input.map(Integer::parseInt)
        val sums = values.windowed(3) { it.sum() }
        return countNumOfLarger(sums)
    }

    val input = readInput("input")
    println(part1(input))
    println(part2(input))
}
