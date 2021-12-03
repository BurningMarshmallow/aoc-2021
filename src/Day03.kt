fun main() {

    fun transpose(input: List<String>): List<String> {
        val transposed: MutableList<String> = mutableListOf()
        input.forEach {
            it.forEachIndexed { index, c ->
                if (transposed.size <= index) transposed.add(index, c.toString())
                else transposed[index] = transposed[index] + c
            }
        }
        return transposed
    }

    fun part1(input: List<String>): Int {
        val transposedNumbers = transpose(input)
        val msbBuilder = StringBuilder()
        val lsbBuilder = StringBuilder()
        for (number in transposedNumbers) {
            if (number.count { it == '1' } > number.count { it == '0' }) {
                msbBuilder.append('1')
                lsbBuilder.append('0')
            } else {
                msbBuilder.append('0')
                lsbBuilder.append('1')
            }
        }

        val gamma = msbBuilder.toString().toInt(2)
        val epsilon = lsbBuilder.toString().toInt(2)

        return gamma * epsilon
    }

    fun oxygenDigitChoice(zeroCount: Int, oneCount: Int): Char {
        return if (oneCount >= zeroCount) '1' else '0'
    }

    fun co2DigitChoice(zeroCount: Int, oneCount: Int): Char {
        return if (oneCount >= zeroCount) '0' else '1'
    }

    fun determineRating(input: List<String>, digitChoice: (zeroCount: Int, oneCount: Int) -> Char): Int {
        val numberOfBits = input[0].count()
        var numbers = input
        for (i in (0 until numberOfBits)) {
            if  (numbers.count() == 1) {
                break
            }
            var oneCount = 0
            var zeroCount = 0
            for (j in (0 until numbers.count())) {
                val digit = numbers[j][i]
                if (digit == '1') {
                    oneCount++
                } else {
                    zeroCount++
                }
            }

            numbers = numbers.filter { it[i] == digitChoice(zeroCount, oneCount) }
        }

        return numbers.single().toInt(2)
    }

    fun part2(input: List<String>): Int {
        val oxygen = determineRating(input, ::oxygenDigitChoice)
        val co2 = determineRating(input, ::co2DigitChoice)
        return oxygen * co2
    }

    val testInput = readInput("input_test")
    check(part1(testInput) == 198)
    check(part2(testInput) == 230)
    val input = readInput("input")
    println(part1(input))
    println(part2(input))
}
