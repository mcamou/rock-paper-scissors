package com.tecnoguru.rock_paper_scissors.games

import com.tecnoguru.rock_paper_scissors.games.GameDefinition.Item

/**
 * Definition of the Canonical game of Rock-Paper-Scissors
 */
object Canonical extends GameDefinition {
  case object Rock extends Item
  case object Paper extends Item
  case object Scissors extends Item

  beats(Rock, "breaks", Scissors)
  beats(Paper, "wraps", Rock)
  beats(Scissors, "cuts", Paper)
}
