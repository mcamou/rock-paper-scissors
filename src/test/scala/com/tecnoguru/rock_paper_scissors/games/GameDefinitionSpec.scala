package com.tecnoguru.rock_paper_scissors.games

import org.specs2.mutable.Specification

class GameDefinitionSpec extends Specification {

  "A Valid Game Definition" should {
    "indicate who wins" in {
      import ValidGameDefinition._
      import GameDefinition.Result

      ValidGameDefinition.check(Item1, Item2) === Result(Item1, "1 is better", Item2)
      ValidGameDefinition.check(Item2, Item1) === Result(Item1, "1 is better", Item2)
    }
  }
}
