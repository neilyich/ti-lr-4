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

    private val width = (0 .. gameTree.depth).map { layerWidth(it) }.maxOf { it }

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
}