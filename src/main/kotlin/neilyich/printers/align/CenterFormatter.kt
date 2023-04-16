package neilyich.printers.align

import neilyich.printers.Formatter

class CenterFormatter<T>(
    override val height: Int,
    override val width: Int,
    delegate: Formatter<T>,
) : FixedWidthFormatter<T>, FixedHeightFormatter<T> {

    private val formatter = VCenterFormatter(height, HCenterFormatter(width, delegate))

    override fun format(input: T): List<String> {
        return formatter.format(input)
    }
}