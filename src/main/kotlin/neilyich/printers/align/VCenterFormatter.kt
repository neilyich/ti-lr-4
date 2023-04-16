package neilyich.printers.align

class VCenterFormatter<T>(
    override val height: Int,
    private val delegate: FixedWidthFormatter<T>,
) : FixedHeightFormatter<T> {
    override fun format(input: T): List<String> {
        val formatted = delegate.format(input)
        val dif = height - formatted.size
        val topPad = dif / 2
        val botPad = dif - topPad
        val result = ArrayList<String>(height)
        repeat(topPad) {
            result.add(" ".repeat(delegate.width))
        }
        result.addAll(formatted)
        repeat(botPad) {
            result.add(" ".repeat(delegate.width))
        }
        return result
    }
}