package neilyich.printers.align

class Spacer(
    override val height: Int,
    override val width: Int,
) : FixedWidthFormatter<Any>, FixedHeightFormatter<Any> {
    override fun format(input: Any): List<String> {
        val result = ArrayList<String>(height)
        for (row in 0 until height) {
            result.add(" ".repeat(width))
        }
        return result
    }
}