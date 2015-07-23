package com.tecnoguru.rock_paper_scissors.api

import com.tecnoguru.rock_paper_scissors.games.{ ValidGameDefinition, GameRegistry }
import org.scalatest.{ BeforeAndAfterAll, MustMatchers, WordSpec }
import spray.http.StatusCodes
import spray.testkit.ScalatestRouteTest

class ApiServiceSpec extends WordSpec with MustMatchers with ScalatestRouteTest with ApiServiceTrait with BeforeAndAfterAll {
  override implicit val actorRefFactory = system
  override val gameRegistry = new GameRegistry

  override def beforeAll() = {
    gameRegistry.register("valid", ValidGameDefinition)
  }

  "The API" when {
    "it receives a GET for the root document" must {
      "reply with a NotFound error code" in {
        Get() ~> route ~> check {
          status mustEqual StatusCodes.NotFound
        }
      }
    }

    "it receives a GET for an invalid game" must {
      "reply with a NotFound error code" in {
        Get("/invalid") ~> route ~> check {
          status mustEqual StatusCodes.NotFound
        }
      }
    }
  }
}
