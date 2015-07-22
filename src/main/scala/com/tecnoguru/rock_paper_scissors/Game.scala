package com.tecnoguru.rock_paper_scissors

import akka.actor.{ ActorRef, Actor, ActorLogging, Props }
import akka.util.Timeout
import com.tecnoguru.rock_paper_scissors.games.GameDefinition
import com.tecnoguru.rock_paper_scissors.games.GameDefinition.{ Win, Tie, Item }
import akka.pattern.ask

import scala.concurrent.{ Future, ExecutionContext }

/**
 * A Game running Game
 */
class Game(definition: GameDefinition) extends Actor with ActorLogging {

  /**
   * When we receive the first message, store the first Item and Sender here
   */
  var first: Option[(Item, ActorRef)] = None

  override def receive = {
    case item: Item ⇒
      first match {
        case None ⇒
          first = Some((item, sender))

        case Some((firstItem, firstSender)) ⇒
          val response = definition.check(firstItem, item)
          firstSender ! response
          sender ! response
          context.stop(self)
      }
  }
}

/**
 * The companion object of the Game actor
 */
object Game {
  def props(definition: GameDefinition) = Props(classOf[Game], definition)
}
