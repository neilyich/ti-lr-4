package neilyich

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import neilyich.printers.GameTreeFormatter
import neilyich.tree.RandomGameTree
import neilyich.tree.RouteWinsComparatorImpl
import java.io.File
import kotlin.math.max
import kotlin.random.Random

fun main() {
    val mapper = configMapper()
    val task = mapper.readValue(File("lr4.json"), Lr4Task::class.java)
    val players = task.randomGameTree.players.mapIndexed { id, player -> Player(id, player.strategiesCount) }
    val winGenerator = WinGenerator(Random(task.randomGameTree.seed), task.randomGameTree.minWin..task.randomGameTree.maxWin)
    //val gameTree = DefinedGameTree()
    val gameTree = RandomGameTree(players, task.randomGameTree.depth, winGenerator, RouteWinsComparatorImpl(players))
    val solutions = gameTree.solutions()
    gameTree.markOptimalPaths()
    val numberWidth = max(task.randomGameTree.minWin.toString().length, task.randomGameTree.maxWin.toString().length)
    val f = GameTreeFormatter(numberWidth, gameTree).format()
    f.forEach { println(it) }
    println()
    println("Решения: $solutions")
}

private fun configMapper(): ObjectMapper {
    val mapper = ObjectMapper()
    mapper.registerModules(kotlinModule())
    mapper.enable(JsonParser.Feature.ALLOW_COMMENTS)
    return mapper
}