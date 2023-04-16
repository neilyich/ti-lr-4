package neilyich

data class Lr4Task(
    val randomGameTree: RandomGameTree
) {
    data class RandomGameTree(
        val seed: Int,
        val players: List<Player>,
        val depth: Int,
        val minWin: Int,
        val maxWin: Int,
    ) {
        data class Player(
            val strategiesCount: Int
        )
    }
}