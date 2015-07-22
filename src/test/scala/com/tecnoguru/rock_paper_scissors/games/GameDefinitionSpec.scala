package com.tecnoguru.rock_paper_scissors.games

import org.scalatest.{ MustMatchers, WordSpec }

class GameDefinitionSpec extends WordSpec with MustMatchers {
  import GameDefinition._

  "A Valid Game Definition" must {
    import ValidGameDefinition.{ Item1, Item2 }

    "indicate who wins" in {
      ValidGameDefinition.check(Item1, Item2) mustEqual Win(Item1, "1 is better", Item2)
      ValidGameDefinition.check(Item2, Item1) mustEqual Win(Item1, "1 is better", Item2)
    }

    "Indicate a tie" in {
      ValidGameDefinition.check(Item1, Item1) mustEqual Tie
    }
  }

  "An Invalid Game definition" must {
    import NonCyclicGameDefinition.{ Item1, Item2 }

    "throw an exception" when {
      "an unknown pair is specified" in {
        an[IllegalStateException] must be thrownBy { NonCyclicGameDefinition.check(Item1, Item2) }
      }
    }
  }
}
