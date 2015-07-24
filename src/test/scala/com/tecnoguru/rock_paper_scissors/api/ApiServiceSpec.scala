package com.tecnoguru.rock_paper_scissors.api

import akka.testkit.{TestKitBase, TestProbe}
import com.tecnoguru.rock_paper_scissors.games.GameDefinition.{Item, Win}
import com.tecnoguru.rock_paper_scissors.games.{GameDefinition, GameRegistry, ValidGameDefinition}
import org.json4s.JObject
import org.scalatest.{BeforeAndAfterAll, MustMatchers, WordSpec}
import spray.http.StatusCodes
import spray.testkit.ScalatestRouteTest

class ApiServiceSpec extends WordSpec
    with MustMatchers
    with ScalatestRouteTest
    with TestKitBase
    with BeforeAndAfterAll
    with ApiServiceTrait {
  override val actorRefFactory = system

  override lazy val gameRegistry = new GameRegistry

  val mockGame = TestProbe()

  override def beforeAll() = {
    gameRegistry.register("valid", ValidGameDefinition)
  }

  // Override the createGame method so we can control the test
  override def createGame(gameDefinition: GameDefinition) = mockGame.ref

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

    "it receives a GET for an existing game" must {
      "play a game and reply with a JSON response" in {
        // given
        val result = Get("/valid") ~> route

        val item1 = mockGame expectMsgClass classOf[ Item ]
        val item2 = mockGame expectMsgClass classOf[ Item ]

        // when
        mockGame reply Win(item1, "beats", item2)
        mockGame reply Win(item1, "beats", item2)

        result ~> check {
          // then
          status mustEqual StatusCodes.OK
          responseAs[ JObject ].values must contain theSameElementsAs {
            Map(
              "player1Item" -> item1.toString,
              "player2Item" -> item2.toString,
              "winner" -> "player 1",
              "relationship" -> "beats"
            )
          }
        }
      }
    }
  }
}
