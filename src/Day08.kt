fun main() {
    fun countUnique(input: List<String>): Int {
        return input.map { it.split(" | ")[1] }
            .flatMap { it.split(' ') }
            .count { listOf(2, 3, 4, 7).contains(it.length) }
    }

    fun decode(puzzleInput: String): String {
        val wordsByLength = puzzleInput.split(' ')
            .groupBy { it.length }

        // a is difference
        val two = wordsByLength[2]!!.single()
        val three = wordsByLength[3]!!.single()
        val a = three.toSet().subtract(two.toSet()).single()

        // c is contained only in two of six-es
        val six = wordsByLength[6]!!
        val counts = six.joinToString(separator = "")
            .groupingBy { it }
            .eachCount()
        val c = if (counts[two[0]]!! == 2) two[0] else two[1]

        // f is two without c
        val f = if (two[0] == c) two[1] else two[0]

        // d if four intersect all fives
        val four = wordsByLength[4]!!.single()
        val fives = wordsByLength[5]!!
        val d = four.toSet()
            .intersect(fives[0].toSet())
            .intersect(fives[1].toSet())
            .intersect(fives[2].toSet())
            .single()

        // b is four without c,f,d
        val b = four.toSet()
            .subtract(setOf(c))
            .subtract(setOf(d))
            .subtract(setOf(f))
            .single()

        // g is contained in all sixes without a,b,f
        val g = six[0].toSet()
            .intersect(six[1].toSet())
            .intersect(six[2].toSet())
            .subtract(setOf(a))
            .subtract(setOf(b))
            .subtract(setOf(f))
            .single()

        // e is remaining letter
        val e = ('a'..'g').toSet()
            .subtract(setOf(a))
            .subtract(setOf(b))
            .subtract(setOf(c))
            .subtract(setOf(d))
            .subtract(setOf(f))
            .subtract(setOf(g))
            .single()

        return "$a$b$c$d$e$f$g"
    }

    fun wordToDigit(decoded: String): Char {
        val decodedAndSorted = decoded.toCharArray().sorted().joinToString("")
        when (decodedAndSorted) {
            "abcefg" -> return '0'
            "cf" -> return '1'
            "acdeg" -> return '2'
            "acdfg" -> return '3'
            "bcdf" -> return '4'
            "abdfg" -> return '5'
            "abdefg" -> return '6'
            "acf" -> return '7'
            "abcdefg" -> return '8'
            "abcdfg" -> return '9'
        }

        error("Not found! $decodedAndSorted")
    }

    fun getDigit(
        encodedDigit: String,
        translationMap: Map<Char, Char>
    ) = wordToDigit(encodedDigit.map { translationMap[it] }.joinToString(""))

    fun solve(puzzle: List<String>): Int {
        val (puzzleInput, puzzleOutput) = puzzle
        val decoded = decode(puzzleInput)
        val alphabet = ('a'..'g').joinToString("")
        val translationMap = decoded.withIndex().associate { it.value to alphabet[it.index] }

        return puzzleOutput
            .split(" ")
            .fold("") { total, encodedDigit -> total + getDigit(encodedDigit, translationMap) }
            .toInt()
    }

    fun part1(input: List<String>): Int {
        return countUnique(input)
    }

    fun part2(input: List<String>): Int {
        val puzzles = input.map { it.split(" | ") }
        return puzzles.sumOf { solve(it) }
    }

    val testInput = readInput("input_test")
    check(part1(testInput) == 26)
    check(part2(testInput) == 61229)
    val input = readInput("input")
    println(part1(input))
    println(part2(input))
}
