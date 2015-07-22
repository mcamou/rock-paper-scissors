package com.tecnoguru.rock_paper_scissors

import akka.actor.{ Actor, ActorLogging, Props }
import com.tecnoguru.rock_paper_scissors.games.GameDefinition

/**
 * A Game during runtime
 */
class Game(definition: GameDefinition) extends Actor with ActorLogging {
  override def receive = ???
}

/**
 * The companion object of the Game actor
 */
object Game {
  def props(definition: GameDefinition) = Props(classOf[Game], definition)
}
