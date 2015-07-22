package com.tecnoguru.rock_paper_scissors.games

import org.scalatest.WordSpec
import org.scalatest.MustMatchers

class GameDefinitionSpec extends WordSpec with MustMatchers {

  "A Valid Game Definition" must {
    import GameDefinition._
    import ValidGameDefinition._

    "indicate who wins" in {
      ValidGameDefinition.check(Item1, Item2) mustEqual Win(Item1, "1 is better", Item2)
      ValidGameDefinition.check(Item2, Item1) mustEqual Win(Item1, "1 is better", Item2)
    }

    "Indicate a tie" in {
      ValidGameDefinition.check(Item1, Item1) mustEqual Tie
    }
  }
}
