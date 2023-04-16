package neilyich.printers

import neilyich.printers.align.FixedHeightFormatter
import neilyich.printers.align.FixedWidthFormatter
import neilyich.printers.align.HCenterFormatter
import neilyich.tree.Win

class WinFormatter(
    numberWidth: Int,
    playersCount: Int,
) : FixedWidthFormatter<Win>, FixedHeightFormatter<Win> {

    override val width = playersCount * numberWidth + playersCount + 1

    val midIndex = width / 2

    override val height = 1

    private val formatter = HCenterFormatter(width, object : Formatter<Win> {
        override fun format(input: Win): List<String> {
            return listOf(input.joinToString(separator = ",", prefix = "(", postfix = ")") { formatInt(it) })
        }

    })

    override fun format(win: Win): List<String> {
        return formatter.format(win)
    }

    private fun formatInt(n: Int): String {
        return n.toString()
    }
}