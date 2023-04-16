package neilyich.printers

import neilyich.printers.align.FixedHeightFormatter
import neilyich.printers.align.FixedWidthFormatter
import neilyich.printers.align.Spacer
import neilyich.tree.GameTreeNode

class LayerFormatter(
    override val width: Int,
    private val nodeFormatter: NodeFormatter,
) : FixedWidthFormatter<List<GameTreeNode>>, FixedHeightFormatter<List<GameTreeNode>> {

    override val height = nodeFormatter.height

    var nodesCoordinates: List<Int>
        private set

    init {
        nodesCoordinates = emptyList()
    }

    override fun format(input: List<GameTreeNode>): List<String> {
        val totalNodesWidth = input.size * nodeFormatter.width
        val spacesCount = input.size
        val totalSpace = width - totalNodesWidth
        val spaceWidth = totalSpace / spacesCount
        val formattedNodes = input.map { nodeFormatter.format(it) }
        val result = ArrayList<String>(height)
        val spaceStartWidth = (spaceWidth + 1) / 2
        val spaceStart = Spacer(height, spaceStartWidth).format(Any())
        val spaceEnd = Spacer(height, spaceWidth - spaceStartWidth).format(Any())
        calcNodesCoordinates(spaceStartWidth, spaceWidth, input)
        val spaceRemaining = totalSpace % spacesCount
        val indexToAddSpace = totalSpace / (spaceRemaining + 1)
        for (r in 0 until height) {
            val space = Spacer(height, spaceWidth).format(Any())
            var totalSpaceIndex = spaceWidth
            result.add(buildString {
                append(spaceStart[r].also { totalSpaceIndex += it.length })
                for (i in formattedNodes.indices) {
                    append(formattedNodes[i][r])
                    if (i < formattedNodes.size - 1) {
                        append(space[r].also { totalSpaceIndex += it.length })
                    }
                    if (spaceRemaining > 0) {
                        while (totalSpaceIndex >= indexToAddSpace) {
                            append(" ")
                            totalSpaceIndex -= indexToAddSpace
                        }
                    }
                }
                append(spaceEnd[r].also { totalSpaceIndex += it.length })
                padEnd(width)
            })
        }
        return result
    }

    private fun calcNodesCoordinates(spaceStartWidth: Int, spaceWidth: Int, nodes: List<GameTreeNode>) {
        nodesCoordinates = nodes.indices.map { i ->
            val spaces = if (i == 0) spaceStartWidth else spaceStartWidth + spaceWidth * i
            val symbolsToLeft = spaces + i * nodeFormatter.width
            return@map symbolsToLeft + nodeFormatter.midIndex
        }
    }
}