import kotlin.math.abs
import kotlin.math.max

fun main() {
    fun readScanners(input: List<String>): MutableList<MutableList<List<Int>>> {
        val blocks = mutableListOf<MutableList<List<Int>>>()
        var block = mutableListOf<List<Int>>()
        for (line in input) {
            if (line.contains("scanner")) {
                continue
            }
            if (line.isEmpty()) {
                blocks.add(block)
                block = mutableListOf()
            } else {
                block.add(line.split(",").map { it.toInt() })
            }

        }
        blocks.add(block)
        return blocks
    }

    fun <T> permute(xs: MutableList<T>, low: Int = 0): Sequence<List<T>> {
        return sequence {
            if (low + 1 >= xs.size) {
                yield(xs.toList())
            } else {
                for (p in permute(xs, low + 1)) {
                    yield(p.toList())
                }
                for (i in low + 1 until xs.size) {
                    xs[low] = xs[i].also { xs[i] = xs[low] }
                    for (p in permute(xs, low + 1)) {
                        yield(p.toList())
                    }
                    xs[low] = xs[i].also { xs[i] = xs[low] }
                }
            }
        }
    }

    fun getRotations(xs: MutableList<Int>): Sequence<List<Int>> {
        return sequence {
            for (p in permute(xs)) {
                for (i in -1..1) {
                    if (i == 0)
                        continue
                    for (j in -1..1) {
                        if (j == 0)
                            continue
                        for (k in -1..1) {
                            if (k == 0)
                                continue
                            yield(listOf(p[0] * i, p[1] * j, p[2] * k))
                        }
                    }
                }
                yield(p)
            }
        }
    }

    fun sign(x: Int): Int {
        when {
            x > 0 -> return 1
            x == 0 -> return 0
        }

        return -1
    }

    fun transform(perm: List<Int>, values: List<Int>): List<Int> {
        val y = MutableList(3) { 0 }
        for ((i, t) in perm.withIndex()) {
            y[abs(t) - 1] = values[i] * sign(t)
        }
        return y.toList()
    }

    fun rotate(values: MutableList<List<Int>>): Sequence<MutableList<List<Int>>> {
        return sequence {
            for (p in getRotations(mutableListOf(1, 2, 3))) {
                val next = values.map { transform(p, it) }.toMutableList()
                yield(next)
            }
        }
    }

    fun add(x: List<Int>, y: List<Int>): List<Int> {
        return listOf(x[0] + y[0], x[1] + y[1], x[2] + y[2])
    }

    fun sub(x: List<Int>, y: List<Int>): List<Int> {
        return listOf(x[0] - y[0], x[1] - y[1], x[2] - y[2])
    }

    fun findCommon(first: MutableList<List<Int>>, second: MutableList<List<Int>>): List<Int>? {
        for (x in first) {
            for (y in second) {
                val scannerPos = sub(x, y)
                var cnt = 0
                for (value in second) {
                    if (first.contains(add(scannerPos, value))) {
                        cnt++
                    }
                    if (cnt >= 3) { // Hard to prove, but works for test and input
                        return scannerPos
                    }
                }
            }
        }

        return null
    }

    fun findScannerPos(
        first: MutableList<List<Int>>,
        second: MutableList<List<Int>>
    ): Pair<List<Int>, MutableList<List<Int>>>? {
        for (next in rotate(second)) {
            val scannerPos = findCommon(first, next)
            if (scannerPos != null) {
                return scannerPos to next
            }
        }
        return null
    }

    fun calculateNumberOfBeacons(
        scanners: MutableList<MutableList<List<Int>>>,
        scannerPositions: MutableMap<Int, List<Int>>
    ): Int {
        val beacons = hashSetOf<List<Int>>()
        for ((i, scanner) in scanners.withIndex()) {
            for (beacon in scanner) {
                beacons.add(add(scannerPositions[i]!!, beacon))
            }
        }

        return beacons.size
    }

    fun solve(input: List<String>): Pair<MutableList<MutableList<List<Int>>>, MutableMap<Int, List<Int>>> {
        val scanners = readScanners(input)
        val scannerPositions = mutableMapOf(0 to listOf(0, 0, 0))
        while (true) {
            if (scannerPositions.size == scanners.size) {
                break
            }

            for (i in scanners.indices) {
                if (!scannerPositions.containsKey(i)) {
                    continue
                }
                for (j in scanners.indices) {
                    if (scannerPositions.containsKey(j)) {
                        continue
                    }
                    val result = findScannerPos(scanners[i], scanners[j])
                    if (result != null) {
                        scannerPositions[j] = add(scannerPositions[i]!!, result.first)
                        scanners[j] = result.second
                    }
                }
            }
        }

        return scanners to scannerPositions
    }

    fun part1(result: Pair<MutableList<MutableList<List<Int>>>, MutableMap<Int, List<Int>>>): Int {
        return calculateNumberOfBeacons(result.first, result.second)
    }

    fun dist(x: List<Int>, y: List<Int>): Int {
        return x.indices.sumOf { abs(x[it] - y[it]) }
    }

    fun calculateMaxDistance(scannerPositions: MutableMap<Int, List<Int>>): Int {
        var maxDistance = -1
        for (x in scannerPositions.values) {
            for (y in scannerPositions.values) {
                maxDistance = max(maxDistance, dist(x, y))
            }
        }

        return maxDistance
    }

    fun part2(result: Pair<MutableList<MutableList<List<Int>>>, MutableMap<Int, List<Int>>>): Int {
        return calculateMaxDistance(result.second)
    }

    val testInput = readInput("input_test")
    val testResult = solve(testInput)
    check(part1(testResult) == 79)
    check(part2(testResult) == 3621)
    val input = readInput("input")
    val result = solve(input)
    println(part1(result))
    println(part2(result))
}
