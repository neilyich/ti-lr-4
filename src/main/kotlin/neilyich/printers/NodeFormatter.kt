package neilyich.printers

import neilyich.printers.Separators.CORNER_DOWN_LEFT
import neilyich.printers.Separators.CORNER_DOWN_RIGHT
import neilyich.printers.Separators.CORNER_UP_LEFT
import neilyich.printers.Separators.CORNER_UP_RIGHT
import neilyich.printers.Separators.CROSS_DOWN
import neilyich.printers.Separators.CROSS_LEFT
import neilyich.printers.Separators.CROSS_RIGHT
import neilyich.printers.Separators.CROSS_UP
import neilyich.printers.Separators.SEP_HOR
import neilyich.printers.Separators.SEP_VER
import neilyich.printers.align.FixedHeightFormatter
import neilyich.printers.align.FixedWidthFormatter
import neilyich.printers.align.HCenterFormatter
import neilyich.printers.align.VCenterFormatter
import neilyich.tree.GameTreeNode
import neilyich.tree.RouteWin
import neilyich.tree.RouteWins

class NodeFormatter(
    maxWinsSize: Int,
    private val winFormatter: WinFormatter,
    private val colors: Map<Int, String>
): FixedWidthFormatter<GameTreeNode>, FixedHeightFormatter<GameTreeNode> {

    companion object {
        private const val SET_RED_COLOR = "\u001B[0m"
        private const val SET_BOLD = "\u001B[1m"
        private const val RESET = "\u001B[0m"
    }

    override val width = winFormatter.width + 2

    val midIndex = winFormatter.midIndex + 1

    private val playerIdFormatter = HCenterFormatter(winFormatter.width, object : FixedHeightFormatter<Int> {
        override fun format(input: Int): List<String> {
            return listOf(input.toString())
        }
        override val height = 1
    })

    override val height = maxWinsSize * winFormatter.height + maxWinsSize + 1 + 2

    private val formatter = VCenterFormatter(height, object : FixedWidthFormatter<GameTreeNode> {
        override fun format(input: GameTreeNode): List<String> {
            return formatWins(input)
        }
        override val width = this@NodeFormatter.width
    })

    override fun format(node: GameTreeNode): List<String> {
        return formatter.format(node).map { it.ifBlank { it.substring(0, midIndex) + SEP_VER + it.substring(midIndex + 1) } }
    }

    private fun formatWins(node: GameTreeNode): List<String> {
        val wins = node.subTreeWins()
        val result = ArrayList<String>(calcHeight(wins))
        result.add(formatHorizontalSeparator(0, wins.size + 1, true, node.children.filterNotNull().isNotEmpty()))
        result.addAll(formatPlayerIdCell(node))
        for (r in wins.indices) {
            result.add(formatHorizontalSeparator(r + 1, wins.size + 1, true, node.children.filterNotNull().isNotEmpty()))
            result.addAll(formatWinCell(wins[r]))
        }
        result.add(formatHorizontalSeparator(wins.size + 1, wins.size + 1, true, node.children.filterNotNull().isNotEmpty()))
        if (node.isOnOptimalPath) {
            return result.map { SET_RED_COLOR + it + RESET }
        }
        return result
    }

    private fun formatHorizontalSeparator(r: Int, height: Int, hasParents: Boolean, hasChildren: Boolean): String {
        val start = when (r) {
            0 -> CORNER_UP_LEFT
            height -> CORNER_DOWN_LEFT
            else -> CROSS_LEFT
        }
        val end = when (r) {
            0 -> CORNER_UP_RIGHT
            height -> CORNER_DOWN_RIGHT
            else -> CROSS_RIGHT
        }
        val midSep = when (r) {
            0 -> if (hasParents) CROSS_DOWN else SEP_HOR
            height -> if (hasChildren) CROSS_UP else SEP_HOR
            else -> SEP_HOR
        }
        return buildString {
            append(start)
            for (i in 0 until winFormatter.width) {
                val sep = if (i == midIndex - 1) {
                    midSep
                } else {
                    SEP_HOR
                }
                append(sep)
            }
            append(end)
        }
    }

    private fun formatWinCell(win: RouteWin): List<String> {
        val formattedWin = winFormatter.format(win)
        return formattedWin.map { SEP_VER + formatWin(win) + it + RESET + SEP_VER }
    }

    private fun formatWin(win: RouteWin): String {
        return colors[win.id]?.let { "\u001B[38;2;${it}m" } ?: RESET
        //return "\u001B[38;2;${colors[win.id] ?: "255;255;255"}m"
    }

    private fun formatPlayerIdCell(node: GameTreeNode): List<String> {
        val formattedId = playerIdFormatter.format(node.player.id + 1)
        val reset = if (node.isOnOptimalPath) {
            SET_RED_COLOR
        } else {
            RESET
        }
        return formattedId.map { SEP_VER + SET_BOLD + it + reset + SEP_VER }
    }

    private fun calcHeight(wins: RouteWins): Int {
        return wins.size * 2 + 1
    }
}