fun main() {

    fun part1(input: List<String>): Int {
        val instructions = input.map { Pair(it.split(' ')[0], it.split(' ')[1].toInt()) }
        var position = 0
        var depth = 0
        for ((command, units) in instructions) {
            when (command) {
                "forward" -> position += units
                "up" -> depth -= units
                "down" -> depth += units
            }
        }

        return position * depth
    }

    fun part2(input: List<String>): Int {
        val instructions = input.map { Pair(it.split(' ')[0], it.split(' ')[1].toInt()) }
        var position = 0
        var depth = 0
        var aim = 0
        for ((command, units) in instructions) {
            when (command) {
                "forward" -> {
                    position += units
                    depth += aim * units
                }
                "up" -> aim -= units
                "down" -> aim += units
            }
        }

        return position * depth
    }

    val input = readInput("input")
    println(part1(input))
    println(part2(input))
}
