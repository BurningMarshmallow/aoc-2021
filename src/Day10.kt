fun main() {
    fun isOpening(bracket: Char): Boolean {
        return setOf('(', '[', '{', '<').contains(bracket)
    }

    fun match(opening: Char): Char {
        return mapOf('(' to ')', '[' to ']', '{' to '}', '<' to '>')[opening]!!
    }

    fun findFirstNonMatching(brackets: String): Char? {
        val opened = mutableListOf<Char>()
        for (bracket in brackets) {
            if (isOpening(bracket)) {
                opened.add(bracket)
            } else {
                if (bracket == match(opened.last())) {
                    opened.removeAt(opened.lastIndex)
                } else {
                    return bracket
                }
            }
        }

        return null
    }

    fun calculateScore(bracket: Char?): Int {
        when (bracket) {
            ')' -> return 3
            ']' -> return 57
            '}' -> return 1197
            '>' -> return 25137
        }

        return 0
    }

    fun part1(input: List<String>): Int {
        return input.sumOf { calculateScore(findFirstNonMatching(it)) }
    }

    fun getBracketValueForAutocomplete(bracket: Char): Byte {
        when (bracket) {
            '(' -> return 1
            '[' -> return 2
            '{' -> return 3
            '<' -> return 4
        }

        return 0
    }

    fun calculateScoreForAutocomplete(autocomplete: String): Long {
        return autocomplete.fold(0.toLong()) { total, it -> total * 5 + getBracketValueForAutocomplete(it) }
    }

    fun getAutocompleteValue(brackets: String): Long {
        val opened = mutableListOf<Char>()
        for (bracket in brackets) {
            if (isOpening(bracket)) {
                opened.add(bracket)
            } else {
                opened.removeAt(opened.lastIndex)
            }
        }

        return calculateScoreForAutocomplete(opened.joinToString("").reversed())
    }

    fun part2(input: List<String>): Long {
        val incompleteLines = input.filter { findFirstNonMatching(it) == null }
        val scoresToAutocomplete = incompleteLines.map { getAutocompleteValue(it) }.sorted()
        return scoresToAutocomplete[scoresToAutocomplete.size / 2]
    }

    val testInput = readInput("input_test")
    check(part1(testInput) == 26397)
    check(part2(testInput) == 288957.toLong())
    val input = readInput("input")
    println(part1(input))
    println(part2(input))
}
