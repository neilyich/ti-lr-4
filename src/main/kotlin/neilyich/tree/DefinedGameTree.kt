package neilyich.tree

import neilyich.Player

class DefinedGameTree : GameTree {
    override val players: List<Player>
        get() = listOf(Player(0, 2), Player(1, 2))
    override val head: GameTreeNode
    override val depth = 3

    init {
        val n0 = GameTreeNode(players[1], emptyList(), listOf(listOf(-1, -9)))
        val n5 = GameTreeNode(players[1], emptyList(), listOf(listOf(-4, -3)))
        val n11 = GameTreeNode(players[1], emptyList(), listOf(listOf(-1, 6)))
        val n21 = GameTreeNode(players[1], emptyList(), listOf(listOf(-1, 8)))
        val n25 = GameTreeNode(players[1], emptyList(), listOf(listOf(-1, -5)))
        val n29 = GameTreeNode(players[1], emptyList(), listOf(listOf(6, 3)))
        val n35 = GameTreeNode(players[1], emptyList(), listOf(listOf(-3, -2)))
        val n38 = GameTreeNode(players[1], emptyList(), listOf(listOf(-2, 3)))

        val n1 = GameTreeNode(players[0], listOf(n0, n5))
        val n17 = GameTreeNode(players[0], listOf(n11, n21))
        val n27 = GameTreeNode(players[0], listOf(n25, n29))
        val n37 = GameTreeNode(players[0], listOf(n35, n38))

        val n9 = GameTreeNode(players[1], listOf(n1, n17))
        val n31 = GameTreeNode(players[1], listOf(n27, n37))

        val n23 = GameTreeNode(players[0], listOf(n9, n31))

        head = n23
    }
}