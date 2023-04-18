package neilyich.printers

import kotlin.random.Random

object Colors {

    private val r = Random(System.currentTimeMillis())

    private val all = listOf(
        rgb(200, 0, 0),
        rgb(0, 100, 200),
        rgb(200, 100, 0),
        rgb(200, 200, 0),
        rgb(0, 200, 0),
        rgb(0, 200, 200),
        rgb(100, 0, 200),
        rgb(200, 0, 200)
    )

    private fun rgb(r: Int, g: Int, b: Int): String {
        return "$r;$g;$b"
    }

    private var colorsMap: Map<Int, String>? = null

    fun colorsForIds(ids: List<Int>): Map<Int, String> {
        return calcColorsForIds(ids).also { colorsMap = it }
    }

    private fun calcColorsForIds(ids: List<Int>): Map<Int, String> {
        val sortedIds = ids.sorted()
        return sortedIds.mapIndexed { index, id -> if (index < all.size) id to all[index] else id to randomColor() }.toMap()
    }

    private fun randomColor(): String {
        return rgb(r.nextInt(256), r.nextInt(256), r.nextInt(256))
    }
}