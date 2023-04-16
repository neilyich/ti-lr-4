package neilyich.printers.align

import neilyich.printers.Formatter
import kotlin.math.max

class HCenterFormatter<T>(
    override val width: Int,
    private val delegate: Formatter<T>,
) : FixedWidthFormatter<T> {

    override fun format(input: T): List<String> {
        val formatted = delegate.format(input)
        val formattedWidth = formatted.map { it.length }.maxOf { it }
        val dif = max(width - formattedWidth, 0)
        val leftPad = (dif + 1) / 2
        return formatted.map { " ".repeat(leftPad) + it + " ".repeat(max(width - leftPad - it.length, 0)) }
    }
}