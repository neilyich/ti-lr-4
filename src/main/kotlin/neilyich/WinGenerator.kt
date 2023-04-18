package neilyich

import kotlin.random.Random
import kotlin.random.nextInt

class WinGenerator(
    private val random: Random,
    private val range: IntRange,
) {
    fun randomForPlayers(players: List<Player>): List<Int> {
        return players.indices.map { random.nextInt(range) }
    }
}