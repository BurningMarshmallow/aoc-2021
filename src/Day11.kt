fun main() {
    fun willFlash(value:Int) = value > 9

    fun flash(i: Int, j: Int, grid: List<MutableList<Int>>) {
        for (dx in -1..1) {
            for (dy in -1..1) {
                if (dx == 0 && dy == 0) {
                    continue
                }
                if (i + dx in grid.indices && j + dy in grid[0].indices) {
                    grid[i + dx][j + dy]++
                }
            }
        }
    }

    fun simulate(grid: List<MutableList<Int>>): Int {
        var numOfFlashes = 0
        for (i in grid.indices) {
            for (j in grid[0].indices) {
                grid[i][j]++
            }
        }

        val flashed = mutableSetOf<Pair<Int, Int>>()
        while (true) {
            var flashHappened = false
            for (i in grid.indices) {
                for (j in grid[0].indices) {
                    if (willFlash(grid[i][j]) && !flashed.contains(i to j)) {
                        flashHappened = true
                        flashed.add(i to j)
                        numOfFlashes++
                        flash(i, j, grid)
                    }
                }
            }

            if (!flashHappened) {
                for (i in grid.indices) {
                    for (j in grid[0].indices) {
                        if (willFlash(grid[i][j]))
                            grid[i][j] = 0
                    }
                }
                return numOfFlashes
            }
        }
    }

    fun part1(input: List<String>): Int {
        val curr = input.map { it.toCharArray().map(Char::digitToInt).toMutableList() }
        var total = 0
        repeat (100) {
            val flash = simulate(curr)
            total += flash
        }

        return total
    }

    fun part2(input: List<String>): Int {
        val curr = input.map { it.toCharArray().map(Char::digitToInt).toMutableList() }
        var step = 0
        while (true) {
            step++
            val flash = simulate(curr)
            if (flash == curr.size * curr[0].size) {
                return step
            }
        }
    }

    val testInput = readInput("input_test")
    check(part1(testInput) == 1656)
    check(part2(testInput) == 195)
    val input = readInput("input")
    println(part1(input))
    println(part2(input))
}
