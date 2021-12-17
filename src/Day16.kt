import java.lang.Exception

fun main() {
    var versionCounter = 0

    class Parser(input: String) {
        var tape: String = genBits(input)
        var pos: Int = 0

        fun genBits(input: String): String {
            return input.toCharArray().joinToString("") { hexToBinary(it) }
        }

        private fun hexToBinary(it: Char): String {
            val n = "0123456789abcdef".indexOf(it.lowercase())
            return Integer.toBinaryString(n + 0b10000).substring(1)
        }

        fun analyse(): Long {
            val version = parse(3)
            val typeId = parse(3)
            versionCounter += version
            if (typeId == 4) {
                var readNext = true
                var value = 0.toLong()
                while (readNext) {
                    value *= 16
                    readNext = parse(1) != 0
                    value += parse(4)
                }
                return value
            } else {
                val lenId = parse(1)
                val subPackets: List<Long>
                if (lenId == 0) {
                    val length = parse(15)
                    val subParser = Parser("")
                    subParser.tape = tape.substring(pos, pos + length)
                    pos += length
                    subPackets = subParser.analyseAll().toList()
                } else {
                    val length = parse(11)
                    subPackets = (0 until length).map { analyse() }.toList()
                }

                when (typeId) {
                    0 -> return subPackets.sum()
                    1 -> return subPackets.fold(1) { total, value -> total * value }
                    2 -> return subPackets.minOf { it }
                    3 -> return subPackets.maxOf { it }
                    5 -> return if (subPackets[0] > subPackets[1]) 1 else 0
                    6 -> return if (subPackets[0] < subPackets[1]) 1 else 0
                    7 -> return if (subPackets[0] == subPackets[1]) 1 else 0
                }
            }

            return 0
        }

        fun analyseAll(): Sequence<Long> {
            return sequence {
                try {
                    while (true) {
                        yield(analyse())
                    }
                } catch (_: Exception) {
                }
            }
        }

        private fun parse(numOfSymbols: Int): Int {
            val chunk = tape.substring(pos, pos + numOfSymbols)
            pos += numOfSymbols
            return chunk.toInt(2)
        }
    }

    fun solve(input: List<String>): Long {
        versionCounter = 0
        val p = Parser(input[0])
        return p.analyseAll().first()
    }

    fun part1(input: List<String>): Long {
        solve(input)
        return versionCounter.toLong()
    }

    fun part2(input: List<String>): Long {
        return solve(input)
    }

    val testInput = readInput("input_test")
    check(part1(testInput) == 6.toLong())
    check(part2(testInput) == 2021.toLong())
    val input = readInput("input")
    println(part1(input))
    println(part2(input))
}
