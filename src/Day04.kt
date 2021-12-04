const val BOARD_SIZE = 5

fun main() {

    fun readNumbers(input: List<String>) = input[0].split(',').map(Integer::parseInt)

    fun CharSequence.splitIgnoreEmpty(delimiter: Char): List<String> {
        return this.split(delimiter).filter {
            it.isNotEmpty()
        }
    }

    fun transpose(input: MutableList<MutableList<Boolean>>): MutableList<MutableList<Boolean>> {
        val transposed: MutableList<MutableList<Boolean>> = mutableListOf()
        input.forEach {
            it.forEachIndexed { index, c ->
                if (transposed.size <= index) transposed.add(index, mutableListOf(c))
                else transposed[index].add(c)
            }
        }
        return transposed
    }

    fun hasWinningLine(bingoBoardMarked: MutableList<MutableList<Boolean>>) =
        bingoBoardMarked.any { it.all { marked -> marked } }

    fun readBingoBoards(input: List<String>): MutableList<Array<Array<Int>>> {
        val bingoBoards = mutableListOf<Array<Array<Int>>>()
        for (i in 2..input.count() step BOARD_SIZE + 1) {
            val bingoBoard = Array(BOARD_SIZE) { Array(BOARD_SIZE) { 0 } }
            for (j in 0 until BOARD_SIZE) {
                bingoBoard[j] = input[i + j].splitIgnoreEmpty(' ').map(Integer::parseInt).toTypedArray()
            }
            bingoBoards.add(bingoBoard)
        }
        return bingoBoards
    }

    fun play(
        numbers: List<Int>,
        bingoBoards: MutableList<Array<Array<Int>>>,
        bingoBoardsMarked: MutableList<MutableList<MutableList<Boolean>>>,
        numberToWin: Int
    ): Pair<Int, Int> {
        var winnerBoard = -1
        val winnerBoards = mutableSetOf<Int>()
        for (number in numbers) {
            for ((numOfBoard, bingoBoard) in bingoBoards.withIndex()) {
                val bingoBoardMarked = bingoBoardsMarked[numOfBoard]
                for (i in 0 until BOARD_SIZE) {
                    for (j in 0 until BOARD_SIZE) {
                        if (bingoBoard[i][j] == number) {
                            bingoBoardMarked[i][j] = true
                        }
                    }
                }

                val bingoBoardMarkedTransposed = transpose(bingoBoardsMarked[numOfBoard])
                if (!winnerBoards.contains(numOfBoard) &&
                    (hasWinningLine(bingoBoardMarked) || hasWinningLine(bingoBoardMarkedTransposed))
                ) {
                    if (winnerBoards.count() == numberToWin - 1) {
                        winnerBoard = numOfBoard
                        break
                    }
                    winnerBoards.add(numOfBoard)
                }
            }
            if (winnerBoard != -1) {
                return Pair(winnerBoard, number)
            }
        }

        error("Did not find the winner!")
    }

    fun findSumOfUnmarkedForWinner(
        bingoBoardsMarked: MutableList<MutableList<MutableList<Boolean>>>,
        winnerBoard: Int,
        bingoBoards: MutableList<Array<Array<Int>>>
    ): Int {
        var sumOfUnmarked = 0
        for (i in 0 until BOARD_SIZE) {
            for (j in 0 until BOARD_SIZE) {
                if (!bingoBoardsMarked[winnerBoard][i][j]) {
                    sumOfUnmarked += bingoBoards[winnerBoard][i][j]
                }
            }
        }
        return sumOfUnmarked
    }

    fun solve(input: List<String>, firstWins: Boolean): Int {
        val numbers = readNumbers(input)
        val bingoBoards = readBingoBoards(input)
        val numOfBoards = bingoBoards.count()
        val bingoBoardsMarked =
            MutableList(numOfBoards) { MutableList(BOARD_SIZE) { MutableList(BOARD_SIZE) { false } } }

        val numberToWin = if (firstWins) 1 else numOfBoards
        val (winnerBoard, winnerValue) = play(numbers, bingoBoards, bingoBoardsMarked, numberToWin)
        val sumOfUnmarked = findSumOfUnmarkedForWinner(bingoBoardsMarked, winnerBoard, bingoBoards)

        return sumOfUnmarked * winnerValue
    }

    fun part1(input: List<String>): Int {
        return solve(input, true)
    }

    fun part2(input: List<String>): Int {
        return solve(input, false)
    }

    val testInput = readInput("input_test")
    check(part1(testInput) == 4512)
    check(part2(testInput) == 1924)
    val input = readInput("input")
    println(part1(input))
    println(part2(input))
}
