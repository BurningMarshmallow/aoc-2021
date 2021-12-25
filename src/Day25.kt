fun main() {
    data class Point(val x: Int, val y: Int)

    fun moveRight(rightFishes: HashSet<Point>, bottomFishes: HashSet<Point>, m: Int): HashSet<Point> {
        val fishes = rightFishes.union(bottomFishes)
        val nextRightFishes = HashSet<Point>(rightFishes.size)
        for (fish in rightFishes) {
            if (!fishes.contains(Point(fish.x, (fish.y + 1) % m))) {
                nextRightFishes.add(Point(fish.x, (fish.y + 1) % m))
            } else {
                nextRightFishes.add(Point(fish.x, fish.y))
            }
        }

        return nextRightFishes
    }

    fun moveBottom(rightFishes: HashSet<Point>, bottomFishes: HashSet<Point>, n: Int): HashSet<Point> {
        val fishes = rightFishes.union(bottomFishes)
        val nextBottomFishes = HashSet<Point>(bottomFishes.size)
        for (fish in bottomFishes) {
            if (!fishes.contains(Point((fish.x + 1) % n, fish.y))) {
                nextBottomFishes.add(Point((fish.x + 1) % n, fish.y))
            } else {
                nextBottomFishes.add(Point(fish.x, fish.y))
            }
        }

        return nextBottomFishes
    }

    fun part1(input: List<String>): Int {
        val n = input.size
        val m = input[0].length
        var rightFishes = HashSet<Point>(n * m)
        var bottomFishes = HashSet<Point>(n * m)

        for (i in input.indices) {
            for (j in input[0].indices) {
                if (input[i][j] == '>') rightFishes.add(Point(i, j))
                if (input[i][j] == 'v') bottomFishes.add(Point(i, j))
            }
        }

        var steps = 0
        while (true) {
            steps++
            val nextRightFishes = moveRight(rightFishes, bottomFishes, m)
            val nextBottomFishes = moveBottom(nextRightFishes, bottomFishes, n)
            if (rightFishes == nextRightFishes && bottomFishes == nextBottomFishes) {
                break
            }

            rightFishes = nextRightFishes
            bottomFishes = nextBottomFishes
        }

        return steps
    }

    val input = readInput("input")
    println(part1(input))
}