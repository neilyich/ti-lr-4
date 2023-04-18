package neilyich.tree

import neilyich.Player

interface GameTree {
    val players: List<Player>
    val head: GameTreeNode
    val depth: Int
    fun solutions(): RouteWins = head.subTreeWins()
    fun player(depth: Int): Player = players[depth % players.size]
    fun markOptimalPaths() {
        markOptimalPaths(head)
    }

    private fun markOptimalPaths(node: GameTreeNode) {
        node.isOnOptimalPath = true
        val strategies = node.optimalStrategies
        val onOptimalPaths = strategies.mapNotNull { node.children[it] }
        onOptimalPaths.forEach { markOptimalPaths(it) }
    }
}
