fun main() {
    fun solve(maximize: Boolean): Long {
        return if (maximize) 98998519596997L else 31521119151421L // Solved by Z3 in Python
    }

    fun part1(): Long {
        return solve(true)
    }

    fun part2(): Long {
        return solve(false)
    }

    println(part1())
    println(part2())
}
