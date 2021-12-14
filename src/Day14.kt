fun main() {
    fun parseInput(input: List<String>): Pair<String, Map<String, String>> {
        val template = input.first()
        val rules = input
            .drop(2)
            .map { it.split(" -> ") }
            .associate { (a, b) -> a to b }
        return template to rules
    }

    fun countPairs(source: String): Map<String, Long> {
        return source
            .windowed(2) { it.toString() }
            .groupingBy { it }
            .eachCount()
            .mapValues { it.value.toLong() }
    }

    fun solve(input: List<String>, iterations: Int): Long {
        val (template, rules) = parseInput(input)
        var pairsCount = countPairs(template)

        repeat(iterations) {
            pairsCount = pairsCount.flatMap { (pair, count) ->
                val newChar = rules[pair]!!
                listOf(pair[0] + newChar to count, newChar + pair[1] to count)
            }
                .groupBy { it.first }
                .mapValues { (_, counts) -> counts.sumOf { it.second } }
        }

        val freqs = pairsCount
            .flatMap { (pair, count) -> pair.map { it to count } }
            .groupBy { it.first }
            .mapValues { (_, listCounts) -> listCounts.sumOf { (_, count) -> count } }
            .mapValues { (char, count) ->
                if (char == template.first() || char == template.last()) (count + 1) / 2
                else count / 2
            }
            .values
        val max = freqs.maxOf { it }
        val min = freqs.minOf { it }
        return max - min
    }

    fun part1(input: List<String>): Int {
        return solve(input, 10).toInt()
    }


    fun part2(input: List<String>): Long {
        return solve(input, 40)
    }

    val testInput = readInput("input_test")
    check(part1(testInput) == 1588)
    check(part2(testInput) == 2188189693529)
    val input = readInput("input")
    println(part1(input))
    println(part2(input))
}
