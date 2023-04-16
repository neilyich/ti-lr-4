package neilyich.tree

import neilyich.Player
import kotlin.math.min

typealias Win = List<Int>
typealias Wins = List<Win>

class GameTreeNode(
    val player: Player,
    val children: List<GameTreeNode?>,
    private var wins: Wins? = null,
) {

    var isOnOptimalPath: Boolean = false

    var optimalStrategies: List<Int> = emptyList()
        private set

    fun subTreeWins(): Wins = wins ?: calcSubTreeWins().also { wins = it }

    private fun calcSubTreeWins(): Wins {
        val winsList = children.mapNotNull {
            it?.subTreeWins()
        }
        val maxWin = winsList.maxWithOrNull { o1, o2 -> compareWins(o1, o2) }!!
        val maxWins = winsList.mapIndexed { i, w -> i to w }.filter { (_, w) -> compareWins(maxWin, w) == 0 }
        optimalStrategies = maxWins.map { it.first }
        return maxWins.flatMap { it.second }
    }

    private fun filterOptimalWins(wins: Wins, maxWin: Int): Wins {
        return wins.filter { it[player.id] == maxWin }
    }

    private fun compareWins(l: Wins, r: Wins): Int {
        val lWins = l.map { it[player.id] }
        val rWins = r.map { it[player.id] }
        val lMin = lWins.minOf { it }
        val rMin = rWins.minOf { it }
        val lMax = lWins.maxOf { it }
        val rMax = rWins.maxOf { it }
        if (rMax < lMin) {
            return 1
        }
        else if (lMax < rMin) {
            return -1
        }
        return 0
    }

}
