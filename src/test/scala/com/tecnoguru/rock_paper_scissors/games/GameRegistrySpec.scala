package com.tecnoguru.rock_paper_scissors.games

import org.scalatest.{ WordSpec, MustMatchers }

class GameRegistrySpec extends WordSpec with MustMatchers {
  "the GameRegistry" must {
    "return a game definition if the name is correct" in new MockGameRegistry {
      registry.getGame("valid") mustEqual Some(ValidGameDefinition)
    }

    "return None if the name is incorrect" in new MockGameRegistry {
      registry.getGame("nonexistent") mustEqual None
    }

    "return the names of the games" in new MockGameRegistry {
      registry.getAvailableGames.toSeq must contain theSameElementsAs Seq("valid", "invalid")
    }
  }

  trait MockGameRegistry {
    val registry = new GameRegistry
    registry.register("valid", ValidGameDefinition)
    registry.register("invalid", NonCyclicGameDefinition)
  }
}
