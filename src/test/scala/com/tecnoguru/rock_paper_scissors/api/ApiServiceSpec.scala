package com.tecnoguru.rock_paper_scissors.api

import org.scalatest.{ MustMatchers, WordSpec }
import spray.http.StatusCodes
import spray.testkit.ScalatestRouteTest

class ApiServiceSpec extends WordSpec with MustMatchers with ScalatestRouteTest with ApiServiceTrait {
  override implicit val actorRefFactory = system

  "The API" when {
    "it receives a GET for the root document" must {
      "reply with a 404" in {
        Get() ~> route ~> check {
          status mustEqual StatusCodes.NotFound
        }
      }
    }
  }
}
