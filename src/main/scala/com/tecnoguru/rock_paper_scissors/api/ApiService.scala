package com.tecnoguru.rock_paper_scissors.api

import akka.actor.ActorRefFactory
import com.tecnoguru.rock_paper_scissors.games.GameRegistry
import spray.http.StatusCodes.NotFound
import spray.routing.HttpService

/**
 * Spray service actor to serve HTTP requests
 */
trait ApiServiceTrait extends HttpService {
  implicit val actorRefFactory: ActorRefFactory
  val gameRegistry: GameRegistry

  val route = {
    get {
      complete {
        NotFound -> "Please specify a game"
      }
    } ~
      path(Segment) { gameName ⇒
        gameRegistry.getGame(gameName) match {
          case Some(game) ⇒ ???
          case None ⇒ complete {
            NotFound -> s"Valid game types are: ${gameRegistry.getAvailableGames.mkString(", ")}"
          }
        }
      }
  }
}

class ApiService(val gameRegistry: GameRegistry)(implicit val actorRefFactory: ActorRefFactory) extends ApiServiceTrait