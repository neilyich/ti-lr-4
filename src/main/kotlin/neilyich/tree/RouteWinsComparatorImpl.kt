package neilyich.tree

import neilyich.Player

class RouteWinsComparatorImpl(
    private val players: List<Player>,
) : RouteWinsComparator {
    override fun compare(playerId: Int, l: RouteWins, r: RouteWins): Int {
        var cmp = comparePlayerWins(playerId, l, r)
        if (cmp != 0) {
            return cmp
        }
        for (opponentId in players.indices) {
            if (opponentId == playerId) {
                continue
            }
            val newCmp = comparePlayerWins(opponentId, l, r)
            if (cmp == 0 && newCmp != 0) {
                cmp = newCmp
            } else if (cmp < 0 && newCmp > 0) {
                return 0
            } else if (cmp > 0 && newCmp < 0) {
                return 0
            }
        }
        return cmp
    }

    private fun comparePlayerWins(playerId: Int, l: RouteWins, r: RouteWins): Int {
        val lWin = l.map { it[playerId] }
        val rWin = r.map { it[playerId] }
        return compareWins(lWin, rWin)
    }

    private fun compareWins(lWin: List<Int>, rWin: List<Int>): Int {
        val lMin = lWin.minOf { it }
        val rMin = rWin.minOf { it }
        val lMax = lWin.maxOf { it }
        val rMax = rWin.maxOf { it }
        if (rMax < lMin) {
            return 1
        }
        else if (lMax < rMin) {
            return -1
        } else if (lMin == rMin) {
            return lMax.compareTo(rMax)
        } else if (lMax == rMax) {
            return lMin.compareTo(rMin)
        }
        return 0
    }
}