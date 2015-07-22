package com.tecnoguru.rock_paper_scissors.games

import com.tecnoguru.rock_paper_scissors.games.GameDefinition.Win
import org.scalatest.{ MustMatchers, WordSpec }

class RockPaperScissorsLizardSpockSpec extends WordSpec with MustMatchers {
  import RockPaperScissorsLizardSpock._

  "A Rock-Paper-Scissors-Lizard-Spock" must {
    "have Scissors cut Paper" in {
      check(Scissors, Paper) mustEqual Win(Scissors, "cuts", Paper)
    }

    "have Paper wrap Rock" in {
      check(Paper, Rock) mustEqual Win(Paper, "wraps", Rock)
    }

    "have Rock crush Lizard" in {
      check(Rock, Lizard) mustEqual Win(Rock, "crushes", Lizard)
    }

    "have Lizard poison Spock" in {
      check(Lizard, Spock) mustEqual Win(Lizard, "poisons", Spock)
    }

    "have Spock smash Scissors" in {
      check(Spock, Scissors) mustEqual Win(Spock, "smashes", Scissors)
    }

    "have Scissors decapitate Lizard" in {
      check(Scissors, Lizard) mustEqual Win(Scissors, "decapitates", Lizard)
    }

    "have Lizard eat Paper" in {
      check(Lizard, Paper) mustEqual Win(Lizard, "eats", Paper)
    }

    "have Paper disprove Spock" in {
      check(Paper, Spock) mustEqual Win(Paper, "disproves", Spock)
    }

    "have Spock vaporize Rock" in {
      check(Spock, Rock) mustEqual Win(Spock, "vaporizes", Rock)
    }

    "have Rock crush Scissors" in {
      check(Rock, Scissors) mustEqual Win(Rock, "breaks", Scissors)
    }
  }
}
