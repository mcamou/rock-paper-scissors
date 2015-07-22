package com.tecnoguru.rock_paper_scissors.games

import com.tecnoguru.rock_paper_scissors.games.GameDefinition.Item

/**
 * Definition of the Canonical game of Rock-Paper-Scissors
 */
trait Canonical extends GameDefinition {
  override val nameToItem: Map[String, Item] = Map("rock" -> Rock, "paper" -> Paper, "scissors" -> Scissors)

  case object Rock extends Item
  case object Paper extends Item
  case object Scissors extends Item

  beats(Rock, "breaks", Scissors)
  beats(Paper, "wraps", Rock)
  beats(Scissors, "cuts", Paper)
}

object Canonical extends Canonical