fun main() {
    fun parse(input: List<String>): Array<Int> {
        return input.map { it.split(": ")[1].toInt() }.toTypedArray()
    }

    fun part1(input: Array<Int>): Int {
        val pos = input.clone()
        var dice = 1
        val score = arrayOf(0, 0)
        while (true) {
            for (player in 0..1) {
                val nextPos = (pos[player] + 3 * dice + 3 - 1) % 10 + 1
                dice += 3
                score[player] += nextPos
                pos[player] = nextPos

                if (score[player] >= 1000) {
                    return score[1 - player] * (dice - 1)
                }
            }
        }
    }

    val probs: Map<Int, Int> = mapOf(3 to 1, 4 to 3, 5 to 6, 6 to 7, 7 to 6, 8 to 3, 9 to 1)
    val cache: MutableMap<List<Long>, Array<Long>> = mutableMapOf()

    fun solve(pos: Array<Int>, scores: Array<Long> = arrayOf(0, 0), turn: Int = 0): Array<Long> {
        val cacheKey = listOf(pos[0].toLong(), pos[1].toLong(), scores[0], scores[1], turn.toLong())
        if (cache.containsKey(cacheKey)) {
            return cache[cacheKey]!!
        }

        if (scores[0] >= 21) {
            return arrayOf(1, 0)
        } else {
            if (scores[1] >= 21) {
                return arrayOf(0, 1)
            }
        }

        var wins: Array<Long> = arrayOf(0, 0)
        for (value in probs.keys) {
            val nextPos = pos.clone()
            nextPos[turn] += value
            while (nextPos[turn] > 10) {
                nextPos[turn] -= 10
            }
            val nextScores = scores.clone()
            nextScores[turn] += nextPos[turn].toLong()
            wins = (0..1).map { player ->
                val winsForPlayer = solve(nextPos, nextScores, (turn + 1) % 2)[player]
                wins[player] + probs[value]!!.toLong() * winsForPlayer
            }.toTypedArray()
        }

        cache[cacheKey] = wins
        return wins
    }

    fun part2(input: Array<Int>): Long {
        val solve = solve(input)
        return solve.maxOf { it }
    }

    val testInput = parse(readInput("input_test"))
    check(part1(testInput) == 739785)
    check(part2(testInput) == 444356092776315)
    val input = parse(readInput("input"))
    println(part1(input))
    println(part2(input))
}
