package com.tecnoguru.rock_paper_scissors.games

import com.tecnoguru.rock_paper_scissors.games.GameDefinition.Item

/**
 * An invalid game definition - the cycle is not complete
 */
object NonCyclicGameDefinition extends GameDefinition {

  case object Item1 extends Item

  case object Item2 extends Item

  case object Item3 extends Item

  beats(Item2, "2 is better", Item3)
  beats(Item3, "3 is better", Item1)

}
