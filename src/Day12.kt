fun main() {
    fun countPathsInternal(
        adj: MutableMap<String, MutableList<String>>,
        u: String,
        dest: String,
        visited: MutableMap<String, Int>,
        canVisitTwice: Boolean
    ): Int {
        visited[u] = visited[u]!! + 1
        var numOfPaths = 0
        if (u == dest) {
            numOfPaths = 1
        } else {
            val notVisitedTwice = visited.values.all { it != 2 }
            for (next in adj[u]!!) {
                if (next != "start" && (canVisitTwice && notVisitedTwice) || visited[next]!! < 1) {
                    numOfPaths += countPathsInternal(adj, next, dest, visited, canVisitTwice)
                }
            }
        }

        visited[u] = visited[u]!! - 1
        return numOfPaths
    }

    fun countPaths(
        caves: List<String>,
        adj: MutableMap<String, MutableList<String>>,
        canVisitTwice: Boolean
    ): Int {
        val visited = caves
            .associateWith { if (it[0].isUpperCase()) Int.MIN_VALUE else 0 }
            .toMutableMap()
        return countPathsInternal(adj, "start", "end", visited, canVisitTwice)
    }

    fun addEdge(
        adj: MutableMap<String, MutableList<String>>,
        from: String,
        to: String
    ) {
        if (adj.containsKey(from)) {
            adj[from]!!.add(to)
        } else {
            adj[from] = mutableListOf(to)
        }
    }

    fun parseCavesAndAdj(input: List<String>): Pair<List<String>, MutableMap<String, MutableList<String>>> {
        val edges = input.map { it.split("-") }
        val caves = edges.flatten().distinct()

        val adj = mutableMapOf<String, MutableList<String>>()
        for ((from, to) in edges) {
            addEdge(adj, from, to)
            addEdge(adj, to, from)
        }
        return caves to adj
    }

    fun part1(input: List<String>): Int {
        val (caves, adj) = parseCavesAndAdj(input)
        return countPaths(caves, adj, false)
    }

    fun part2(input: List<String>): Int {
        val (caves, adj) = parseCavesAndAdj(input)
        return countPaths(caves, adj, true)
    }

    val testInput = readInput("input_test")
    check(part1(testInput) == 10)
    check(part2(testInput) == 36)
    val input = readInput("input")
    println(part1(input))
    println(part2(input))
}
