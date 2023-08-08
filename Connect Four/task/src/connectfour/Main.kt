package connectfour

var turn = ""

fun checkDimensions(input: String) : Pair<Int, Int> {
    var boardSize = input.split(Regex("\\s")).joinToString("").split(Regex("x|X")).filter { !it.equals("") }.toMutableList()
    if (boardSize.isEmpty()) {
        boardSize.addAll(arrayOf("6", "7"))
    }
    while (boardSize.size < 2) {
        println("Invalid input")
        println("Set the board dimensions (Rows x Columns)")
        println("Press Enter for default (6 x 7)")
        boardSize = readln().split(Regex("\\s")).joinToString("").split(Regex("x|X")).filter { !it.equals("") }.toMutableList()
    }
    while (!boardSize[0].matches(Regex("[0-9]+")) || !boardSize[1].matches(Regex("[0-9]+"))) {
        println("Invalid input")
        println("Set the board dimensions (Rows x Columns)")
        println("Press Enter for default (6 x 7)")
        boardSize = readln().split(Regex("\\s")).joinToString("").split(Regex("x|X")).filter { !it.equals("") }.toMutableList()
    }
    while (boardSize[0].toInt() !in 5..9 || boardSize[1].toInt() !in 5..9) {
        if (boardSize[0].toInt() !in 5..9) {
            println("Board rows should be from 5 to 9")
            println("Set the board dimensions (Rows x Columns)")
            println("Press Enter for default (6 x 7)")
            boardSize = readln().split(Regex("\\s")).joinToString("").split(Regex("x|X")).filter { !it.equals("") }.toMutableList()
        } else if (boardSize[1].toInt() !in 5..9) {
            println("Board columns should be from 5 to 9")
            println("Set the board dimensions (Rows x Columns)")
            println("Press Enter for default (6 x 7)")
            boardSize = readln().split(Regex("\\s")).joinToString("").split(Regex("x|X")).filter { !it.equals("") }.toMutableList()
        }
    }
    return boardSize[0].toInt() to boardSize[1].toInt()
}

fun checkMode(input: String) : Int {
    var mode = input
    while (!mode.matches("[1-9]+".toRegex()) && mode.isNotEmpty()) {
            println("Invalid input")
            println("""Do you want to play single or multiple games?
For a single game, input 1 or press Enter
Input a number of games:""")
            mode = readln()
    }
    if (mode.isEmpty()) mode = "1"
    return mode.toInt()
}

class Board(val rows: Int, val columns: Int) {
    var board : MutableList<MutableList<String>> = MutableList(rows) { MutableList(columns) {" "} }
    fun showABoard() {
        var header = ""
        for (i in 1..columns) { header += " $i" }
        println(header)
        for (i in 1..rows) {
            print("║")
            for (k in 1..columns) {
                print("${board[i - 1][k - 1]}")
                print("║")
            }
            println()
        }
        var footer = "╚"
        for (i in 1..columns) {
            when {
                i < columns -> footer += "═╩"
                i == columns -> footer += "═╝"
            }
        }
        println(footer)
    }
    fun winOrNot() : Boolean {
        var check = false
        if (!check) {
            Loop@for (k in 0 until columns) {
                for (i in 0..rows - 4) {
                    check = board[i][k] == board[i + 1][k] && board[i][k] == board[i + 2][k] && board[i][k] == board[i + 3][k] && board[i][k] != " "
                    if (check) break@Loop
                }
            }
        }
        if (!check) {
            Loop@for (i in 0 until rows) {
                for (k in 0..columns - 4) {
                    check = board[i][k] == board[i][k + 1] && board[i][k] == board[i][k + 2] && board[i][k] == board[i][k + 3] && board[i][k] != " "
                    if (check) break@Loop
                }
            }
        }
        if (!check) {
            Loop@for (k in 0..columns - 4) {
                for (i in 0..rows - 4) {
                    for (a in 1..3)
                        check = board[i][k] == board[i + a][k + a] && board[i][k] != " "
                }
                if (check) break@Loop
                for (i in 3 until rows) {
                    for (a in 1..3)
                        check = board[i][k] == board[i - a][k + a] && board[i][k] != " "
                }
                if (check) break@Loop
            }
        }
        return check
    }
    fun drawOrNot() : Boolean {
        var check = false
        val checkList = mutableListOf<Boolean>()
        for (i in 0 until rows) {
            for (k in 0 until columns) {
                check = board[i][k] != " "
                checkList.add(check)
            }
        }
        return !checkList.contains(false)
    }
}
class Player(val name: String, val sign: String) {
    fun makeTurn(gameBoard: Board) {
        println("$name's turn:")
        turn = readln()
        if (turn != "end") {
            while (!turn.matches("[0-9]+".toRegex()) || (turn.toInt() < 1 || turn.toInt() > gameBoard.columns)) {
                if (!turn.matches("[0-9]+".toRegex())) {
                    println("Incorrect column number")
                    println("$name's turn:")
                    turn = readln()
                } else {
                    println("The column number is out of range (1 - ${gameBoard.columns})")
                    println("$name's turn:")
                    turn = readln()
                }
                if (turn == "end") break
            }
            var check = gameBoard.rows
            if (turn != "end") {
                while (gameBoard.board[check - 1][turn.toInt() - 1] != " ") {
                    check--
                    if (check == 0) {
                        check = gameBoard.rows
                        println("Column ${turn.toInt()} is full")
                        println("$name's turn:")
                        turn = readln()
                        if (turn == "end") break
                    }
                }
            }
            if (check != 0 && turn != "end") {
                gameBoard.board[check - 1][turn.toInt() - 1] = sign
                gameBoard.showABoard()
            }

        }
        when (turn) {
            "end" -> println("Game over!")
        }
    }
}

fun main() {
    println("Connect Four")
    println("First player's name:")
    val firstPlayer = Player(readln(), "o")
    println("Second player's name:")
    val secondPlayer = Player(readln(), "*")
    println("Set the board dimensions (Rows x Columns)")
    println("Press Enter for default (6 x 7)")
    var size: Pair<Int, Int> = checkDimensions(readln())
    var gameBoard = Board(size.first, size.second)
    println("""Do you want to play single or multiple games?
For a single game, input 1 or press Enter
Input a number of games:""")
    var mode = checkMode(readln())
    if (mode == 1) {
        println("${firstPlayer.name} VS ${secondPlayer.name}")
        println("${gameBoard.rows} X ${gameBoard.columns} board")
        println("Single game")
        gameBoard.showABoard()
        while (turn != "end") {
            firstPlayer.makeTurn(gameBoard)
            if (gameBoard.winOrNot()) println("""Player ${firstPlayer.name} won
            |Game over!
        """.trimMargin())
            if (gameBoard.drawOrNot()) println("""It is a draw
                |Game Over!
            """.trimMargin())
            if (gameBoard.winOrNot()) break
            if (turn != "end") {
                secondPlayer.makeTurn(gameBoard)
                if (gameBoard.winOrNot()) println("""Player ${secondPlayer.name} won
            |Game over!
        """.trimMargin())
                if (gameBoard.drawOrNot()) println("""It is a draw
                |Game Over!
            """.trimMargin())
                if (gameBoard.winOrNot()) break
            }
        }
    } else {
        println("""${firstPlayer.name} VS ${secondPlayer.name}
${gameBoard.rows} X ${gameBoard.columns} board
Total $mode games""")
        Loop@while (turn != "end") {
            var scoreFirst = 0
            var scoreSecond = 0
            for (game in 1..mode) {
                println("Game #$game")
                gameBoard.showABoard()
                if (game % 2 == 1) {
                    while (turn != "end") {
                        firstPlayer.makeTurn(gameBoard)
                        if (turn == "end") break@Loop
                        if (gameBoard.winOrNot()) {
                            scoreFirst += 2
                            println("""Player ${firstPlayer.name} won
Score
${firstPlayer.name}: ${scoreFirst} ${secondPlayer.name}: ${scoreSecond}
        """.trimMargin())
                        }
                        if (gameBoard.drawOrNot()) {
                            scoreFirst++
                            scoreSecond++
                            println("""It is a draw
Score
${firstPlayer.name}: ${scoreFirst} ${secondPlayer.name}: ${scoreSecond}
            """.trimMargin())
                        }
                        if (gameBoard.winOrNot() || gameBoard.drawOrNot()) break
                        if (turn != "end") {
                            secondPlayer.makeTurn(gameBoard)
                            if (turn == "end") break@Loop
                            if (gameBoard.winOrNot()) {
                                scoreSecond += 2
                                println("""Player ${secondPlayer.name} won
Score
${firstPlayer.name}: ${scoreFirst} ${secondPlayer.name}: ${scoreSecond}
        """.trimMargin())
                            }
                            if (gameBoard.drawOrNot()) {
                                scoreFirst++
                                scoreSecond++
                                println("""It is a draw
Score
${firstPlayer.name}: ${scoreFirst} ${secondPlayer.name}: ${scoreSecond}
            """.trimMargin())
                            }
                            if (gameBoard.winOrNot() || gameBoard.drawOrNot()) break
                        }
                    }
                } else {
                    while (turn != "end") {
                        secondPlayer.makeTurn(gameBoard)
                        if (turn == "end") break@Loop
                        if (gameBoard.winOrNot()) {
                            scoreSecond += 2
                            println("""Player ${secondPlayer.name} won
Score
${firstPlayer.name}: ${scoreFirst} ${secondPlayer.name}: ${scoreSecond}
        """.trimMargin())
                        }
                        if (gameBoard.drawOrNot()) {
                            scoreFirst++
                            scoreSecond++
                            println("""It is a draw
Score
${firstPlayer.name}: ${scoreFirst} ${secondPlayer.name}: ${scoreSecond}
            """.trimMargin())
                        }
                        if (gameBoard.winOrNot() || gameBoard.drawOrNot()) break
                        if (turn != "end") {
                            firstPlayer.makeTurn(gameBoard)
                            if (turn == "end") break@Loop
                            if (gameBoard.winOrNot()) {
                                scoreFirst += 2
                                println("""Player ${firstPlayer.name} won
Score
${firstPlayer.name}: ${scoreFirst} ${secondPlayer.name}: ${scoreSecond}
        """.trimMargin())
                            }
                            if (gameBoard.drawOrNot()) {
                                scoreFirst++
                                scoreSecond++
                                println("""It is a draw
Score
${firstPlayer.name}: ${scoreFirst} ${secondPlayer.name}: ${scoreSecond}
            """.trimMargin())
                            }
                            if (gameBoard.winOrNot() || gameBoard.drawOrNot()) break
                        }
                    }
                }
                gameBoard = Board(size.first, size.second)
            }
            if (turn != "end") println("Game over!")
        }
    }
}

