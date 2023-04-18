package neilyich.tree

import neilyich.Player


private typealias Win = List<Int>
typealias RouteWins = List<RouteWin>

data class RouteWin(
    private val win: Win,
    val id: Int,
) {
    operator fun get(i: Int) = win[i]
    fun get() = win
}

class GameTreeNode(
    val player: Player,
    val children: List<GameTreeNode?>,
    private val routeWinsComparator: RouteWinsComparator,
    private var wins: RouteWins? = null,
) {

    var isOnOptimalPath: Boolean = false

    var optimalStrategies: List<Int> = emptyList()
        private set

    fun subTreeWins(): RouteWins = wins ?: calcSubTreeWins().also { wins = it }

    private fun calcSubTreeWins(): RouteWins {
        val winsList = children.mapNotNull {
            it?.subTreeWins()
        }
        val maxWin = winsList.maxWithOrNull { o1, o2 -> routeWinsComparator.compare(player.id, o1, o2) }!!
        val maxWins = winsList.mapIndexed { i, w -> i to w }.filter { (_, w) -> routeWinsComparator.compare(player.id, maxWin, w) == 0 }
        optimalStrategies = maxWins.map { it.first }
        return maxWins.flatMap { it.second }
    }


}
