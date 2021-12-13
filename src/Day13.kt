import kotlin.math.abs

fun main() {
    data class Point(var x: Int, var y: Int)

    fun parsePoint(it: String): Point {
        val values = it.split(',')
        return Point(values[1].toInt(), values[0].toInt())
    }

    fun merge(first: Char, second: Char): Char {
        if (first == '#' || second == '#')
            return '#'
        return '.'
    }

    fun fold(
        foldInstructions: List<String>,
        init: Pair<Int, Int>,
        paper: MutableList<MutableList<Char>>
    ): Pair<Int, Int> {
        var paperSize = init
        for (foldInstruction in foldInstructions) {
            val instruction = foldInstruction.split("along ")[1].split('=')
            val (dir, value) = instruction[0] to instruction[1].toInt()
            if (dir == "x") {
                for (i in 0 until paperSize.first) {
                    for (j in 0 until value) {
                        paper[i][j] = merge(paper[i][j], paper[i][value + abs(value - j)])
                    }
                }

                paperSize = paperSize.first to value
            } else {
                for (i in 0 until value) {
                    for (j in 0 until paperSize.second) {
                        paper[i][j] = merge(paper[i][j], paper[value + abs(value - i)][j])
                    }
                }

                paperSize = value to paperSize.second
            }
        }
        return paperSize
    }

    fun simulate(
        input: List<String>,
        onlyFirstInstruction: Boolean
    ): Pair<Pair<Int, Int>, MutableList<MutableList<Char>>> {
        val dotsFormatted = input.takeWhile { it != "" }
        var foldInstructions = input.slice(dotsFormatted.size + 1 until input.size)
        if (onlyFirstInstruction) {
            foldInstructions = foldInstructions.take(1)
        }
        val dots = dotsFormatted.map(::parsePoint)
        var paperSize = dots.maxOf { it.x + 2 } to dots.maxOf { it.y + 2 }
        val paper = MutableList(paperSize.first) { MutableList(paperSize.second) { '.' } }
        for (dot in dots) {
            paper[dot.x][dot.y] = '#'
        }

        paperSize = fold(foldInstructions, paperSize, paper)
        return paperSize to paper
    }

    fun part1(input: List<String>): Int {
        val (paperSize, paper) = simulate(input, true)

        return paper.take(paperSize.first)
            .flatMap { it.take(paperSize.second) }
            .count { it == '#' }
    }

    fun printPaper(
        paperSize: Pair<Int, Int>,
        paper: MutableList<MutableList<Char>>
    ) {
        for (i in 0 until paperSize.first) {
            for (j in 0 until paperSize.second) {
                print(paper[i][j])
            }
            println()
        }
    }

    fun part2(input: List<String>): Pair<Pair<Int, Int>, MutableList<MutableList<Char>>> {
        return simulate(input, false)
    }

    val testInput = readInput("input_test")
    check(part1(testInput) == 17)
    val input = readInput("input")
    println(part1(input))
    val (paperSize, paper) = part2(input)
    printPaper(paperSize, paper)
}
