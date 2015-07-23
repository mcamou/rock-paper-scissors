package com.tecnoguru.rock_paper_scissors.api

import akka.actor.ActorRefFactory
import spray.http.StatusCodes.NotFound
import spray.routing.HttpService

/**
 * Spray service actor to serve HTTP requests
 */
trait ApiServiceTrait extends HttpService {
  implicit val actorRefFactory: ActorRefFactory

  val route = {
    get {
      complete {
        NotFound -> "Please specify a game"
      }
    }
  }
}

class ApiService(implicit val actorRefFactory: ActorRefFactory) extends ApiServiceTrait