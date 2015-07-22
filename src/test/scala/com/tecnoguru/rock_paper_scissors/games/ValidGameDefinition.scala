package com.tecnoguru.rock_paper_scissors.games

/**
 * A Valid game definition
 */
object ValidGameDefinition extends GameDefinition {

  case object Item1 extends Item

  case object Item2 extends Item

  case object Item3 extends Item

  beats(Item1, "1 is better", Item2)
  beats(Item2, "2 is better", Item3)
  beats(Item3, "3 is better", Item1)
}
