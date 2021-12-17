import kotlin.math.max

fun main() {
    data class Point(var x: Int, var y: Int) {
        operator fun plusAssign(other: Point) {
            x += other.x
            y += other.y
        }
    }

    fun parse(input: List<String>): Pair<Point, Point> {
        val regex = Regex("-?\\d+")
        val numbers = regex.findAll(input[0]).map { it.value.toInt() }.toList()

        return Point(numbers[0], numbers[2]) to Point(numbers[1], numbers[3])
    }

    fun sign(x: Int): Int {
        when {
            x > 0 -> return 1
            x == 0 -> return 0
        }

        return -1
    }

    fun simulate(bottomLeft: Point, topRight: Point, v: Point): Pair<Boolean, Int> {
        val p = Point(0, 0)
        var maxY = Int.MIN_VALUE
        var inArea = false
        for (step in 0..1000) {
            p += v
            maxY = max(maxY, p.y)

            if ((bottomLeft.x <= p.x && p.x <= topRight.x) && (bottomLeft.y <= p.y && p.y <= topRight.y)) {
                inArea = true
            }

            v.x -= sign(v.x)
            v.y -= 1

            if (p.x > topRight.x && v.x >= 0 && !inArea) {
                break
            }
            if (p.x < bottomLeft.x && v.x == 0 && !inArea) {
                break
            }
        }

        return inArea to if (inArea) maxY else Int.MIN_VALUE
    }

    fun calculateVelocities(input: List<String>): Sequence<Pair<Boolean, Int>> {
        val (bottomLeft, topRight) = parse(input)

        return sequence {
            for (x in 1..topRight.x) {
                for (y in bottomLeft.y..-bottomLeft.y) {
                    val result = simulate(bottomLeft, topRight, Point(x, y))
                    if (result.first) {
                        yield(result)
                    }
                }
            }
        }
    }

    fun part1(input: List<String>): Int {
        return calculateVelocities(input).maxOf { it.second }
    }

    fun part2(input: List<String>): Int {
        return calculateVelocities(input).count()
    }

    val testInput = readInput("input_test")
    check(part1(testInput) == 45)
    check(part2(testInput) == 112)
    val input = readInput("input")
    println(part1(input))
    println(part2(input))
}
