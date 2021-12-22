import kotlin.math.max
import kotlin.math.min

fun main() {
    fun parse(input: List<String>): List<Pair<Boolean, List<IntRange>>> {
        val values = input.map { it.split(" ") }
            .map { it[0] to it[1].split(",").map { range -> range.split("=")[1] } }
            .map {
                (it.first == "on") to it.second.map { range -> range.split("..") }
                    .map { (low, high) -> low.toInt()..high.toInt() }
            }
        return values
    }

    fun intersect(a: List<IntRange>, b: List<IntRange>): List<IntRange> {
        return (0..2).map { x -> max(a[x].first, b[x].first)..min(a[x].last, b[x].last) }
    }

    fun countVolume(cuboid: List<IntRange>): Long {
        if ((0..2).any { cuboid[it].first > cuboid[it].last }) {
            return 0
        }

        return cuboid.map { (it.last - it.first + 1).toLong() }.reduce { acc, num -> acc * num }
    }

    fun intersects(first: List<IntRange>, second: List<IntRange>): Boolean {
        return countVolume(intersect(first, second)) != 0.toLong()
    }

    fun inRangeOfFifties(cuboid: Pair<Boolean, List<IntRange>>): Boolean {
        val fifties = listOf(-50..50, -50..50, -50..50)
        return intersects(cuboid.second, fifties)
    }

    fun solve(input: List<Pair<Boolean, List<IntRange>>>, filter: (Pair<Boolean, List<IntRange>>) -> Boolean): Long {
        val cuboids: MutableList<Pair<Boolean, List<IntRange>>> = mutableListOf()
        for (step in input) {
            val (shouldTurnOn, cube) = step
            val intersections = cuboids.filter { cuboid -> intersects(cube, cuboid.second) }
                .map { cuboid -> !cuboid.first to intersect(cube, cuboid.second) }
                .toMutableList()
            if (shouldTurnOn) {
                cuboids += step
            }
            cuboids += intersections
        }

        return cuboids.filter { filter(it) }
            .sumOf { (countVolume(it.second) * (if (it.first) 1 else -1)) }
    }

    fun part1(input: List<Pair<Boolean, List<IntRange>>>): Long {
        return solve(input, ::inRangeOfFifties)
    }

    fun part2(input: List<Pair<Boolean, List<IntRange>>>): Long {
        return solve(input) { true }
    }

    val testInput = parse(readInput("input_test"))
    check(part1(testInput) == 474140.toLong())
    check(part2(testInput) == 2758514936282235)
    val input = parse(readInput("input"))
    println(part1(input))
    println(part2(input))
}
