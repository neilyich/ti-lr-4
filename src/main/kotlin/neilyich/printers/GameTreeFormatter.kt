package neilyich.printers

import neilyich.printers.Separators.CORNER_UP_LEFT
import neilyich.printers.Separators.CORNER_UP_RIGHT
import neilyich.printers.Separators.CROSS
import neilyich.printers.Separators.CROSS_DOWN
import neilyich.printers.Separators.CROSS_UP
import neilyich.printers.Separators.SEP_HOR
import neilyich.tree.GameTree
import neilyich.tree.GameTreeNode

class GameTreeFormatter(
    numberWidth: Int,
    private val gameTree: GameTree,
) {
    fun format(): List<String> {
        (0 .. gameTree.depth).forEach {
            println("lastLayer = ${layer(it).size}")
        }
        val result = mutableListOf<String>()
        for (depth in layerFormatters.indices) {
            val layer = layerFormatters[depth]
            val formattedLayer = layer.format(layer(depth))
            if (depth > 0) {
                result.add(layersUpSpacer(depth, layer.nodesCoordinates.toSet(), layerFormatters[depth - 1].nodesCoordinates.toSet()))
            }
            result.addAll(formattedLayer)
        }
        return result
    }

    private fun layersUpSpacer(depth: Int, childCoordinates: Set<Int>, parentCoordinates: Set<Int>) = buildString {
        val childrenPerParent = gameTree.player(depth + 1).strategies().count()
        var childrenCounter = 0
        for (i in 0 until width) {
            if (i in childCoordinates) {
                if (i in parentCoordinates) {
                    append(CROSS)
                } else if (childrenCounter == 0) {
                    append(CORNER_UP_LEFT)
                } else if (childrenCounter == childrenPerParent - 1) {
                    append(CORNER_UP_RIGHT)
                } else {
                    append(CROSS_UP)
                }
                childrenCounter = (childrenCounter + 1) % childrenPerParent
            } else if (i in parentCoordinates) {
                append(CROSS_DOWN)
            } else if (childrenCounter > 0) {
                append(SEP_HOR)
            } else {
                append(" ")
            }
        }
    }

    private val winFormatter = WinFormatter(
        numberWidth = numberWidth,
        playersCount = gameTree.players.size
    )

    private val width = (0 .. gameTree.depth).map { layerWidth(it) }.maxOf { it }.also { println("width = $it") }

    private val layerFormatters = (0 .. gameTree.depth).map { layerFormatter(it) }

    private fun layerFormatter(depth: Int) = LayerFormatter(
        width = width,
        nodeFormatter = NodeFormatter(
            maxWinsSize = maxWinsSize(depth),
            winFormatter = winFormatter,
        ),
    )

    private fun layerWidth(depth: Int): Int {
        val nodeWidth = NodeFormatter(maxWinsSize(depth), winFormatter).width

        val lastLayerNodesCount = layerNodesCount(depth)
        return lastLayerNodesCount * nodeWidth
    }

    private fun maxWinsSize(depth: Int): Int {
        return maxWinsSize(gameTree.head, 0, depth)
    }

    private fun maxWinsSize(node: GameTreeNode, currentDepth: Int, depth: Int): Int {
        if (currentDepth == depth) {
            return node.subTreeWins().size
        }
        return node.children.mapNotNull { it?.let { maxWinsSize(it, currentDepth + 1, depth) } }.maxOf { it }
    }

    private fun layerNodesCount(depth: Int): Int {
        return layerNodesCount(gameTree.head, 0, depth)
    }

    private fun layerNodesCount(node: GameTreeNode, currentDepth: Int, depth: Int): Int {
        if (currentDepth == depth) {
            return 1
        }
        return node.children.mapNotNull { it?.let { layerNodesCount(it, currentDepth + 1, depth) } }.sum()
    }

    private fun layer(depth: Int): List<GameTreeNode> {
        return layer(gameTree.head, 0, depth)
    }

    private fun layer(node: GameTreeNode, currentDepth: Int, depth: Int): List<GameTreeNode> {
        if (currentDepth == depth) {
            return listOf(node)
        }
        return node.children.flatMap {
            it?.let {
                layer(it, currentDepth + 1, depth)
            } ?: emptyList()
        }
    }


//    private val layerSeparatorHeight = 1
//    private val height = calcHeight()
//
//    private val lines = Array(height) { "" }
//
//    companion object {
//        private const val SEP_HOR = "─"
//        private const val SEP_VER = "│"
//        private const val CROSS = "┼"
//        private const val CROSS_UP = "┬"
//        private const val CROSS_DOWN = "┴"
//        private const val CROSS_LEFT = "├"
//        private const val CROSS_RIGHT = "┤"
//        private const val CORNER_UP_LEFT = "┌"
//        private const val CORNER_UP_RIGHT = "┐"
//        private const val CORNER_DOWN_LEFT = "└"
//        private const val CORNER_DOWN_RIGHT = "┘"
//    }
//
//
//    private fun calcHeight(): Int {
//        val head = gameTree.head
//        return calcHeight(head) + gameTree.depth * layerSeparatorHeight
//    }
//
//    private fun calcHeight(node: GameTreeNode): Int {
//        val wins = node.subTreeWins()
//        return calcHeight(wins) + node.children.mapNotNull { it?.let { calcHeight(it.subTreeWins()) } }.maxOf { it }
//    }
//
//    private fun calcHeight(wins: Wins): Int {
//        return wins.size * 2 + 1
//    }
//
//    fun format(): List<String> {
//
//    }
//
//    fun formatWin(win: Win): String {
//        val str = win.joinToString(separator = ",", prefix = "(", postfix = ")") { formatInt(it) }
//        val dif = max(winWidth - str.length, 0)
//        val leftPad = (dif + 1) / 2
//        val rightPad = dif - leftPad
//        return " ".repeat(leftPad) + str + " ".repeat(rightPad)
//    }
//
//    private fun formatWinCell(win: Win): String {
//        val formattedWin = formatWin(win)
//        return SEP_VER + formattedWin + SEP_VER
//    }
//
//    fun formatWins(wins: Wins): List<String> {
//        val result = ArrayList<String>(calcHeight(wins))
//        for (r in wins.indices) {
//            result.add(formatHorizontalSeparator(r, wins))
//            result.add(formatWinCell(wins[r]))
//        }
//        result.add(formatHorizontalSeparator(wins.size, wins))
//        return result
//    }
//
//    private fun formatHorizontalSeparator(r: Int, wins: Wins): String {
//        val start = when (r) {
//            0 -> CORNER_UP_LEFT
//            wins.size -> CORNER_DOWN_LEFT
//            else -> CROSS_LEFT
//        }
//        val end = when (r) {
//            0 -> CORNER_UP_RIGHT
//            wins.size -> CORNER_DOWN_RIGHT
//            else -> CROSS_RIGHT
//        }
//        return buildString {
//            append(start)
//            repeat(winWidth - start.length - end.length) { append(SEP_HOR) }
//            append(end)
//        }
//    }
//
//    private fun formatInt(n: Int): String {
//        return n.toString()
//    }
//
//
//
//    fun printMixedStrategy(label: String?, strategy: List<Double>) {
//        label?.let { print("$label: ") }
//        println(formatMixedStrategy(strategy))
//    }
//
//    fun printDouble(label: String?, n: Double) {
//        label?.let { print("$label: ") }
//        println(formatDouble(n))
//    }
//
//    fun withBlue(printBlock: GameTreeFormatter.() -> Unit) {
//        printWithColor(34, printBlock)
//    }
//
//    fun withRed(printBlock: GameTreeFormatter.() -> Unit) {
//        printWithColor(31, printBlock)
//    }
//
//    fun withPurple(printBlock: GameTreeFormatter.() -> Unit) {
//        printWithColor(35, printBlock)
//    }
//
//    fun withGray(printBlock: GameTreeFormatter.() -> Unit) {
//        printWithColor(37, printBlock)
//    }
//
//    private fun printWithColor(color: Int, printBlock: GameTreeFormatter.() -> Unit) {
//        setPrintColor(color)
//        this.printBlock()
//        resetPrintColor()
//    }
//
//    private fun setPrintColor(color: Int) {
//        print("\u001B[${color}m")
//    }
//
//    private fun resetPrintColor() {
//        setPrintColor(0)
//    }
//
//    fun printWins(label: String? = null, game2D: Game2D) {
//        label?.let { println("$it:") }
//        for (r in 0 until game2D.rows) {
//            withGray {
//                printHorizontalSeparator(r, game2D)
//                print(SEP_VER)
//            }
//            for (c in 0 until game2D.cols) {
//                val situation = game2D.situation(r, c)
//                val usingFormatting: (printBlock: GameTreeFormatter.() -> Unit) -> Unit = if (situation.isNashEquilibrium) {
//                    if (situation.isOptimalByPareto) {
//                        this::withPurple
//                    } else {
//                        this::withRed
//                    }
//                } else {
//                    if (situation.isOptimalByPareto) {
//                        this::withBlue
//                    } else {
//                        this::withGray
//                    }
//                }
//                usingFormatting {
//                    printSituation(game2D.situation(r, c))
//                }
//                withGray { print(SEP_VER) }
//            }
//            println()
//        }
//        withGray {
//            printHorizontalSeparator(game2D.rows, game2D)
//        }
//    }
//
//    private fun printHorizontalSeparator(r: Int, wins: Wins): String {
//        val start = when (r) {
//            0 -> CORNER_UP_LEFT
//            wins.size -> CORNER_DOWN_LEFT
//            else -> CROSS_LEFT
//        }
//        val end = when (r) {
//            0 -> CORNER_UP_RIGHT
//            wins.size -> CORNER_DOWN_RIGHT
//            else -> CROSS_RIGHT
//        }
//        return buildString {
//            append(start)
//            repeat(winWidth - start.length - end.length) { append(SEP_HOR) }
//            append(end)
//        }
//    }
//
//    private fun printWin(win: Win) {
//        print(formatWin(win))
//    }
//
//
//    private fun formatMixedStrategy(strategy: List<Double>): String {
//        return strategy.joinToString(separator = ",") { formatDouble(it) }
//    }
//
//    private fun formatDouble(n: Double): String {
//        return BigDecimal.valueOf(n).setScale(3, RoundingMode.HALF_EVEN).toString()
//    }
}