package neilyich.printers

import neilyich.printers.align.FixedHeightFormatter
import neilyich.printers.align.FixedWidthFormatter
import neilyich.printers.align.HCenterFormatter
import neilyich.tree.RouteWin

class WinFormatter(
    numberWidth: Int,
    playersCount: Int,
) : FixedWidthFormatter<RouteWin>, FixedHeightFormatter<RouteWin> {

    override val width = playersCount * numberWidth + playersCount + 1

    val midIndex = width / 2

    override val height = 1

    private val formatter = HCenterFormatter(width, object : Formatter<RouteWin> {
        override fun format(input: RouteWin): List<String> {
            return listOf(input.get().joinToString(separator = ",", prefix = "(", postfix = ")") { formatInt(it) })
        }

    })

    override fun format(win: RouteWin): List<String> {
        return formatter.format(win)
    }

    private fun formatInt(n: Int): String {
        return n.toString()
    }
}