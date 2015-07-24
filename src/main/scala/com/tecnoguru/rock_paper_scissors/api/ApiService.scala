package com.tecnoguru.rock_paper_scissors.api

import akka.actor.{ ActorRef, ActorRefFactory }
import akka.util.Timeout
import com.tecnoguru.rock_paper_scissors.Game
import com.tecnoguru.rock_paper_scissors.games.GameDefinition.{ Tie, Win }
import com.tecnoguru.rock_paper_scissors.games.{ GameDefinition, GameRegistry }
import spray.http.StatusCodes.NotFound
import spray.httpx.Json4sJacksonSupport
import spray.routing.{ HttpService, Route }
import org.json4s.DefaultFormats
import akka.pattern.ask
import scala.concurrent.duration._

/**
 * Spray service actor to serve HTTP requests
 */
trait ApiServiceTrait extends HttpService
    with Json4sJacksonSupport {
  implicit val actorRefFactory: ActorRefFactory
  def gameRegistry: GameRegistry

  case class Result(player1Item: String, player2Item: String, winner: String, relationship: String)

  override implicit val json4sJacksonFormats = DefaultFormats

  private val validGameTypes = gameRegistry.getAvailableGames.mkString(", ")

  /**
   * Create a new Game for the given GameDefinition, and return the corresponding ActorRef
   * @param gameDefinition The GameDefinition for the wanted game
   * @return The ActorRef that corresponds to the given game
   */
  def createGame(gameDefinition: GameDefinition): ActorRef = {
    actorRefFactory.actorOf(Game.props(gameDefinition))
  }

  val route = {
    get {
      pathEndOrSingleSlash {
        invalidGame(s"Please select a game. Valid game types are: $validGameTypes")
      } ~
        path(Segment) { gameName ⇒
          selectGame(gameName)
        }
    }
  }

  /**
   * Play the game selected by the user
   * @param gameName The name of the game the user wants to play
   * @return A Route that plays that game if it exists or returns an appropriate error
   */
  private def selectGame(gameName: String): Route = {
    gameRegistry.getGame(gameName) match {
      case Some(game) ⇒ playGame(game)
      case None       ⇒ invalidGame(s"Valid game types are: $validGameTypes")
    }
  }

  /**
   * Play a given game
   * @param definition the game to play
   * @return a route that plays the given game
   */
  private def playGame(definition: GameDefinition): Route = {
    implicit val timeout = Timeout(50000.millis)
    import actorRefFactory.dispatcher

    val game = createGame(definition)
    val player1Item = definition.randomItem
    val player2Item = definition.randomItem

    game ! player1Item

    val result = (game ? player2Item) map {
      case Win(`player1Item`, relationship, _) ⇒
        Result(player1Item.toString, player2Item.toString, "player 1", relationship)

      case Win(_, relationship, _) ⇒
        Result(player2Item.toString, player1Item.toString, "player 2", relationship)

      case Tie ⇒
        Result(player1Item.toString, player2Item.toString, "tie", "is the same as")
    }

    complete {
      result
    }
  }

  /**
   * Called when the user has selected a nonexistent game
   * @param message The message to display to the user in the HTML body
   * @return A Route with an appropriate HTTP error and the given message
   */
  private def invalidGame(message: String): Route = {
    complete {
      NotFound -> message
    }
  }
}

class ApiService(val gameRegistry: GameRegistry)(implicit val actorRefFactory: ActorRefFactory) extends ApiServiceTrait