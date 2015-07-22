package com.tecnoguru.rock_paper_scissors.games

import com.tecnoguru.rock_paper_scissors.games.GameDefinition.Item

/**
 * A Valid game definition, used for testing
 */
object ValidGameDefinition extends GameDefinition {
  override val nameToItem: Map[String, Item] = Map("item1" -> Item1, "item2" -> Item2, "item3" -> Item3)

  case object Item1 extends Item

  case object Item2 extends Item

  case object Item3 extends Item

  beats(Item1, "1 is better", Item2)
  beats(Item2, "2 is better", Item3)
  beats(Item3, "3 is better", Item1)
}
