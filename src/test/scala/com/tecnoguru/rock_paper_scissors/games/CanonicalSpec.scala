package com.tecnoguru.rock_paper_scissors.games

import com.tecnoguru.rock_paper_scissors.games.GameDefinition.Win
import org.scalatest.{ MustMatchers, WordSpec }

class CanonicalSpec extends WordSpec with MustMatchers {

  "A Canonical rock-paper-scissors game" must {
    import Canonical._

    "have Rock beat Scissors" in {
      check(Rock, Scissors) mustEqual Win(Rock, "breaks", Scissors)
      check(Scissors, Rock) mustEqual Win(Rock, "breaks", Scissors)
    }

    "have Paper beat Rock" in {
      check(Paper, Rock) mustEqual Win(Paper, "wraps", Rock)
      check(Rock, Paper) mustEqual Win(Paper, "wraps", Rock)
    }

    "have Scissors beat Paper" in {
      check(Scissors, Paper) mustEqual Win(Scissors, "cuts", Paper)
      check(Paper, Scissors) mustEqual Win(Scissors, "cuts", Paper)
    }

    "correctly map names to items" in {
      Canonical.nameToItem must contain theSameElementsAs Map("rock" -> Rock, "paper" -> Paper, "scissors" -> Scissors)
    }
  }
}
