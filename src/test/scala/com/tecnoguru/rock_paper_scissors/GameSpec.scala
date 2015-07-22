package com.tecnoguru.rock_paper_scissors

import akka.actor.ActorSystem
import akka.testkit.{ ImplicitSender, TestKit, TestProbe }
import com.tecnoguru.rock_paper_scissors.games.GameDefinition.Win
import com.tecnoguru.rock_paper_scissors.games.ValidGameDefinition
import com.tecnoguru.rock_paper_scissors.games.ValidGameDefinition.{ Item1, Item2 }
import org.scalatest.{ BeforeAndAfterAll, WordSpecLike }

class GameSpec(_system: ActorSystem) extends TestKit(_system)
    with ImplicitSender
    with WordSpecLike
    with BeforeAndAfterAll {
  def this() = this(ActorSystem("GameSpec"))

  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  "A Game" should {
    "Accept two Items and reply with a Result to both senders" in {
      val game = system.actorOf(Game.props(ValidGameDefinition))
      val player1 = TestProbe()
      val player2 = TestProbe()

      game.tell(Item1, player1.ref)
      game.tell(Item2, player2.ref)

      player1.expectMsg(Win(Item1, "1 is better", Item2))
      player2.expectMsg(Win(Item1, "1 is better", Item2))
    }
  }
}
