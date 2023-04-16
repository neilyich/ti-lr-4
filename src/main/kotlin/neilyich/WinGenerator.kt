package neilyich

import neilyich.tree.Win
import kotlin.random.Random
import kotlin.random.nextInt

class WinGenerator(
    private val random: Random,
    private val range: IntRange,
) {
    fun randomForPlayers(players: List<Player>): Win {
        return players.indices.map { random.nextInt(range) }
    }
}