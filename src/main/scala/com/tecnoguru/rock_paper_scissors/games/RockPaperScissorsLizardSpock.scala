package com.tecnoguru.rock_paper_scissors.games

import com.tecnoguru.rock_paper_scissors.games.GameDefinition.Item

/**
 * Definition of Rock-Paper-Scissors-Lizard-Spock, as per http://www.samkass.com/theories/RPSSL.html
 * @see http://www.samkass.com/theories/RPSSL.html
 */
object RockPaperScissorsLizardSpock extends Canonical {
  override val nameToItem = Map(
    "rock" -> Rock,
    "paper" -> Paper,
    "scissors" -> Scissors,
    "lizard" -> Lizard,
    "spock" -> Spock
  )

  case object Lizard extends Item
  case object Spock extends Item

  beats(Rock, "crushes", Lizard)
  beats(Lizard, "poisons", Spock)
  beats(Spock, "smashes", Scissors)
  beats(Scissors, "decapitates", Lizard)
  beats(Lizard, "eats", Paper)
  beats(Paper, "disproves", Spock)
  beats(Spock, "vaporizes", Rock)
}
