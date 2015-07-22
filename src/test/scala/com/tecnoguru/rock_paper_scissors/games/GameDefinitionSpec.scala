package com.tecnoguru.rock_paper_scissors.games

import org.scalatest.WordSpec

class GameDefinitionSpec extends WordSpec {

  "A Valid Game Definition" should {
    import ValidGameDefinition._
    import GameDefinition._

    "indicate who wins" in {
      ValidGameDefinition.check(Item1, Item2) === Win(Item1, "1 is better", Item2)
      ValidGameDefinition.check(Item2, Item1) === Win(Item1, "1 is better", Item2)
    }

    "Indicate a tie" in {
      ValidGameDefinition.check(Item1, Item1) === Tie
    }
  }
}
