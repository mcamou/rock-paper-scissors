package com.tecnoguru.rock_paper_scissors.api

import akka.actor._
import akka.io.IO
import com.tecnoguru.rock_paper_scissors.games.GameRegistry
import spray.can.Http
import spray.http.StatusCodes
import spray.routing.{ ExceptionHandler, HttpServiceActor }
import spray.util.LoggingContext

/**
 * Actor that implements the HTTP service
 * @param gameRegistry The gameRegistry for the games the user wants to play
 */
class ApiService(val gameRegistry: GameRegistry)
    extends HttpServiceActor
    with ApiServiceTrait {
  import spray.routing.RejectionHandler.Default
  override implicit val actorRefFactory = context
  implicit val routingSettings = spray.routing.RoutingSettings.default

  implicit def exceptionHandler(implicit log: LoggingContext) = {
    ExceptionHandler {
      case ex: Exception ⇒
        requestUri { uri ⇒
          log.error(ex, s"Exception while accessing $uri")
          complete(StatusCodes.InternalServerError -> ex.getMessage)
        }
    }
  }

  def receive = runRoute(route)
}

object ApiService {
  /**
   * Start the HTTP server
   * @param gameRegistry The gameRegistry for the games the user can play
   * @param port The port the HTTP service runs on
   * @param system The ActorSystem for the whole thing
   */
  def start(gameRegistry: GameRegistry, port: Int)(implicit system: ActorSystem): Unit = {
    val service = system.actorOf(Props(classOf[ApiService], gameRegistry), "api")

    IO(Http) ! Http.Bind(service, "0.0.0.0", port)
  }
}