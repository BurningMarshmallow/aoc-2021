fun main() {
    fun getInitLightPixels(input: List<String>): HashSet<Pair<Int, Int>> {
        val image = input.drop(2)
        val lightPixels = HashSet<Pair<Int, Int>>(image.size * image.size)
        for (i in image.indices) {
            for (j in image[0].indices) {
                if (image[i][j] == '#') lightPixels.add(i to j)
            }
        }
        return lightPixels
    }

    fun getOuterColor(shouldBlink: Boolean, step: Int) = (if (shouldBlink) step % 2 else 0).digitToChar()

    fun indexOutOfBounds(
        ny: Int,
        bottom: Int,
        top: Int,
        nx: Int,
        left: Int,
        right: Int
    ) = ny < bottom || ny > top || nx < left || nx > right

    fun solve(input: List<String>, iterations: Int): Int {
        val algo = input.first()

        var lightPixels = getInitLightPixels(input)
        var bottom = lightPixels.minOf { it.first }
        var top = lightPixels.maxOf { it.first }
        var left = lightPixels.minOf { it.second }
        var right = lightPixels.maxOf { it.second }

        val shouldBlink = algo[0] == '#'
        val numberBuilder = StringBuilder(9)
        repeat(iterations) {
            val outerColor = getOuterColor(shouldBlink, it)
            val nextLightPixels = HashSet<Pair<Int, Int>>(lightPixels.size)
            for (y in (bottom - 1)..(top + 1)) {
                for (x in (left - 1)..(right + 1)) {
                    for (ny in y - 1..y + 1) {
                        for (nx in x - 1..x + 1) {
                            numberBuilder.append(
                                if (indexOutOfBounds(ny, bottom, top, nx, left, right)) outerColor
                                else if (lightPixels.contains(ny to nx)) '1'
                                else '0'
                            )
                        }
                    }
                    if (algo[numberBuilder.toString().toInt(2)] == '#') {
                        nextLightPixels.add(y to x)
                    }
                    numberBuilder.clear()
                }
            }
            lightPixels = nextLightPixels
            bottom--
            left--
            top++
            right++
        }
        return lightPixels.size
    }

    fun part1(input: List<String>): Int {
        return solve(input, 2)
    }

    fun part2(input: List<String>): Int {
        return solve(input, 50)
    }

    val testInput = readInput("input_test")
    check(part1(testInput) == 35)
    check(part2(testInput) == 3351)
    val input = readInput("input")
    println(part1(input))
    println(part2(input))
}