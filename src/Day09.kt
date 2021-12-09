fun main() {
    fun inBounds(value: Int, size: Int) =
        (0 <= value) && (value < size)

    fun getMin(a: List<List<Int>>, i: Int, j: Int): Pair<Int, Int> {
        var mini = i
        var minj = j
        if (inBounds(i + 1, a.size)) {
            if (a[mini][minj] >= a[i + 1][j]) {
                mini = i + 1
                minj = j
            }
        }
        if (inBounds(i - 1, a.size)) {
            if (a[mini][minj] >= a[i - 1][j]) {
                mini = i - 1
                minj = j
            }
        }
        if (inBounds(j + 1, a[0].size)) {
            if (a[mini][minj] >= a[i][j + 1]) {
                mini = i
                minj = j + 1
            }
        }
        if (inBounds(j - 1, a[0].size)) {
            if (a[mini][minj] >= a[i][j - 1]) {
                mini = i
                minj = j - 1
            }
        }

        return mini to minj
    }

    fun isMin(a: List<List<Int>>, i: Int, j: Int): Boolean {
        return getMin(a, i, j) == i to j
    }

    fun part1(input: List<String>): Int {
        val init = input.map { it.toCharArray().map(Char::digitToInt) }
        var totalRisk = 0
        for (i in init.indices) {
            for (j in init[0].indices) {
                if (isMin(init, i, j)) {
                    totalRisk += init[i][j] + 1
                }
            }
        }
        return totalRisk
    }

    fun find(basins: MutableList<Int>, u: Int): Int {
        return if (basins[u] == u) u else find(basins, basins[u])
    }

    fun union(basins: MutableList<Int>, u: Int, v: Int) {
        basins[find(basins, v)] = find(basins, u)
    }

    fun findBasinSizes(heightmap: List<List<Int>>): Map<Int, Int> {
        val n = heightmap.size
        val m = heightmap[0].size
        val basins = (0 until n * m).toMutableList()

        for (i in heightmap.indices) {
            for (j in heightmap[0].indices) {
                if (heightmap[i][j] == 9) {
                    continue
                }
                val (mini, minj) = getMin(heightmap, i, j)
                union(basins, i * m + j, mini * m + minj)
            }
        }

        return (0 until n * m).map { find(basins, it) }
            .groupingBy { it }
            .eachCount()
    }

    fun part2(input: List<String>): Int {
        val heightmap = input.map { it.toCharArray().map(Char::digitToInt) }
        val basinSizes = findBasinSizes(heightmap)
        return basinSizes.values
            .sortedDescending()
            .take(3)
            .fold(1) { total, it -> total * it }
    }

    val testInput = readInput("input_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 1134)
    val input = readInput("input")
    println(part1(input))
    println(part2(input))
}
