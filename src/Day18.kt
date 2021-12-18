import kotlin.math.max

interface SnailfishNumber

fun main() {
    class Value(val value: Int) : SnailfishNumber
    class Pair(val left: SnailfishNumber, val right: SnailfishNumber) : SnailfishNumber

    class Parser(var input: String) {
        var pos: Int = 0

        fun parse(): SnailfishNumber {
            return if (input[pos] != '[') {
                val value = input[pos]
                pos += 1

                Value(Character.getNumericValue(value))
            } else {
                val from = pos
                readToClosing()
                pos += 1
                val to = pos
                val subParser = Parser(input.substring(from, to))

                subParser.parseAll()
            }
        }

        private fun readToClosing() {
            val opened = mutableListOf<Char>()
            while (true) {
                if (input[pos] == '[') {
                    opened.add(input[pos])
                } else {
                    if (input[pos] == ']') {
                        opened.removeAt(opened.lastIndex)
                        if (opened.isEmpty())
                            break
                    }
                }
                pos++
            }
        }

        fun parseAll(): SnailfishNumber {
            if (input[0] != '[')
                return Value(input.toInt())
            else {
                skipSymbol()
                val left = parse()
                skipSymbol()
                val right = parse()
                return Pair(left, right)
            }
        }

        private fun skipSymbol() {
            pos += 1
        }
    }

    fun equals(first: SnailfishNumber, second: SnailfishNumber): Boolean {
        return if (first is Pair && second is Pair) {
            equals(first.left, second.left) && equals(first.right, second.right)
        } else {
            if (first is Value && second is Value) {
                first.value == second.value
            } else {
                false
            }
        }
    }

    fun addToRight(num: SnailfishNumber, addition: SnailfishNumber): SnailfishNumber {
        if (addition !is Value) {
            error("Addition should be Value")
        }
        when (num) {
            is Value -> return Value(num.value + addition.value)
            is Pair -> {
                return Pair(num.left, addToRight(num.right, addition))
            }
        }
        error("Impossible addToRight")
    }

    fun addToLeft(num: SnailfishNumber, addition: SnailfishNumber): SnailfishNumber {
        if (addition !is Value) {
            error("Addition should be Value")
        }
        when (num) {
            is Value -> return Value(num.value + addition.value)
            is Pair -> {
                return Pair(addToLeft(num.left, addition), num.right)
            }
        }
        error("Impossible addToLeft")
    }

    data class ExplodeResult(
        val exploded: Boolean,
        val num: SnailfishNumber,
        val left: SnailfishNumber,
        val right: SnailfishNumber
    )

    fun explode(num: SnailfishNumber, depth: Int = 0): ExplodeResult {
        when (num) {
            is Value -> return ExplodeResult(false, num, Value(0), Value(0))
            is Pair -> {
                if (depth < 4) {
                    var result = explode(num.left, depth + 1)
                    if (result.exploded) {
                        val next = Pair(result.num, addToLeft(num.right, result.right))
                        return ExplodeResult(true, next, result.left, Value(0))
                    }
                    result = explode(num.right, depth + 1)
                    if (result.exploded) {
                        val next = Pair(addToRight(num.left, result.left), result.num)
                        return ExplodeResult(true, next, Value(0), result.right)
                    }
                    return ExplodeResult(false, num, Value(0), Value(0))
                } else {
                    return ExplodeResult(true, Value(0), num.left, num.right)
                }
            }
        }
        error("Impossible")
    }

    fun split(num: SnailfishNumber): SnailfishNumber {
        when (num) {
            is Value -> {
                if (num.value >= 10) {
                    return Pair(Value(num.value / 2), Value(num.value / 2 + num.value % 2))
                }
                return num
            }
            is Pair -> {
                val splitLeft = split(num.left)
                if (!equals(splitLeft, num.left)) {
                    return Pair(splitLeft, num.right)
                }
                val splitRight = split(num.right)
                return Pair(num.left, splitRight)
            }
        }
        error("Impossible split")
    }

    fun reduce(init: SnailfishNumber): SnailfishNumber {
        var num = init
        while (true) {
            val explodeResult = explode(num)
            num = explodeResult.num
            if (!explodeResult.exploded) {
                num = split(num)
                if (equals(explodeResult.num, num)) {
                    break
                }
            }
        }

        return num
    }

    fun add(acc: SnailfishNumber, num: SnailfishNumber): SnailfishNumber {
        return reduce(Pair(acc, num))
    }

    fun magnitude(num: SnailfishNumber): Int {
        when (num) {
            is Value -> return num.value
            is Pair -> {
                return magnitude(num.left) * 3 + magnitude(num.right) * 2
            }
        }
        error("Impossible magnitude")
    }

    fun part1(input: List<String>): Int {
        val numbers = input.map { Parser(it).parseAll() }
        val result = numbers.reduce { acc, num -> add(acc, num) }
        return magnitude(result)
    }

    fun part2(input: List<String>): Int {
        val numbers = input.map { Parser(it).parseAll() }
        var maxMagnitude = 0
        for (i in numbers.indices) {
            for (j in numbers.indices) {
                if (i == j)
                    continue
                val magnitude = magnitude(add(numbers[i], numbers[j]))
                maxMagnitude = max(maxMagnitude, magnitude)
            }
        }

        return maxMagnitude
    }

    val testInput = readInput("input_test")
    check(part1(testInput) == 4140)
    check(part2(testInput) == 3993)
    val input = readInput("input")
    println(part1(input))
    println(part2(input))
}
