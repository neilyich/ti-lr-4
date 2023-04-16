package neilyich.tree

import neilyich.Player
import neilyich.WinGenerator

class RandomGameTree(
    override val players: List<Player>,
    override val depth: Int,
    private val winGenerator: WinGenerator,
) : GameTree {
    override val head: GameTreeNode

    init {
        if (depth <= 0) {
            throw IllegalArgumentException("depth must be positive")
        }
        if (players.size <= 1) {
            throw IllegalArgumentException("game must have at least 2 players")
        }
        head = generateRandomSubTree(players[0], 0)
    }

    private fun generateRandomSubTree(player: Player, currentDepth: Int): GameTreeNode {
        if (currentDepth == depth) {
            return GameTreeNode(
                player = player,
                children = emptyList(),
                wins = listOf(winGenerator.randomForPlayers(players))
            )
        }
        val nextPlayer = nextPlayer(player)
        val children = player.strategies().map { generateRandomSubTree(nextPlayer, currentDepth + 1) }
        return GameTreeNode(player, children)
    }

    private fun nextPlayer(player: Player): Player {
        if (player.id < players.size - 1) {
            return players[player.id + 1]
        }
        return players[0]
    }

}