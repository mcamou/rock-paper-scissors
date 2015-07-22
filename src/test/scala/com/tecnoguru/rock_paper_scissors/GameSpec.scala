package com.tecnoguru.rock_paper_scissors

import akka.actor.{ ActorSystem, Terminated }
import akka.testkit.{ ImplicitSender, TestKit, TestProbe }
import com.tecnoguru.rock_paper_scissors.games.GameDefinition.{ Tie, Win }
import com.tecnoguru.rock_paper_scissors.games.ValidGameDefinition
import com.tecnoguru.rock_paper_scissors.games.ValidGameDefinition.{ Item1, Item2 }
import org.scalatest.{ BeforeAndAfterAll, WordSpecLike }

import scala.concurrent.duration._

class GameSpec(_system: ActorSystem) extends TestKit(_system)
    with ImplicitSender
    with WordSpecLike
    with BeforeAndAfterAll {
  def this() = this(ActorSystem("GameSpec"))

  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  "A Game" when {
    "receiving the first message" must {
      "not reply" in {
        val game = system.actorOf(Game.props(ValidGameDefinition))
        game ! Item1
        expectNoMsg(500.millis)
      }
    }

    "receiving the second message" must {
      "reply with a Win if one of the senders won" in new GameContext {
        game.tell(Item1, player1.ref)
        game.tell(Item2, player2.ref)

        player1.expectMsg(Win(Item1, "1 is better", Item2))
        player2.expectMsg(Win(Item1, "1 is better", Item2))
      }

      "reply with a Tie if the senders tied" in new GameContext {
        game.tell(Item1, player1.ref)
        game.tell(Item1, player2.ref)

        player1.expectMsg(Tie)
        player2.expectMsg(Tie)
      }

      "die afterwards" in new GameContext {
        watch(game)

        game.tell(Item1, player1.ref)
        game.tell(Item2, player2.ref)

        expectMsgPF(remaining) {
          case Terminated(`game`) ⇒
        }
      }
    }
  }

  trait GameContext {
    val game = system.actorOf(Game.props(ValidGameDefinition))
    val player1 = TestProbe()
    val player2 = TestProbe()
  }
}
