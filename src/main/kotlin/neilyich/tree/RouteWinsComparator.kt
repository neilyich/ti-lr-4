package neilyich.tree

interface RouteWinsComparator {
    fun compare(playerId: Int, l: RouteWins, r: RouteWins): Int
}