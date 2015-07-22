package com.tecnoguru.rock_paper_scissors

import java.util.concurrent.TimeUnit

import akka.actor.{ ActorRef, ActorSystem }
import akka.pattern.ask
import akka.util.Timeout
import com.tecnoguru.rock_paper_scissors.games.{ RockPaperScissorsLizardSpock, GameDefinition, Canonical }
import com.tecnoguru.rock_paper_scissors.games.Canonical.{ Scissors, Paper, Rock }
import com.tecnoguru.rock_paper_scissors.games.GameDefinition.{ Win, Tie, Item }

import scala.concurrent.{ ExecutionContext, Future }
import scala.util.Random

object Main {
  def main(args: Array[String]): Unit = {
    // $COVERAGE-OFF$ Disabling coverage for main method since it is a simple test driver
    if (args.length == 0 || args.length > 2) {
      println(
        """
          |Hello Human. Welcome to Rock-Paper-Scissors!
          |
          |If you want to run a computer vs computer game, just pass "computer" as a command-line argument
          |If you want to play against the computer, pass in your selection (rock, paper or scissors) as a
          |command-line argument
          |
          |For example:
          |
          |$ java -jar rock_paper_scissors.jar computer
          |$ java -jar rock_paper_scissors.jar paper
          |
          |By default you will play a game of "canonical" Rock Paper Scissors (i.e., the valid selections are
          |rock, paper or scissors). There is also the possibility of playing Rock Paper Scissors Lizard Spock, which
          |adds 2 additional options (lizard and spock). To play this, add "spock" as a second parameter:
          |
          |$ java -jar rock_paper_scissors.jar paper spock
          |
          |Enjoy!
        """.stripMargin)

      System.exit(0)
    }

    val gameDefinition: GameDefinition = selectGame(args)

    val userItemArgument = args(0).toLowerCase
    val userOption = gameDefinition.nameToItem.get(userItemArgument)

    if (userOption.isEmpty && userItemArgument != "computer") {
      println("Illegal option. You must use rock, paper, scissors or computer")
      System.exit(0)
    }
    // $COVERAGE-ON

    startGame(userOption, gameDefinition)
  }

  /**
   * Select the game depending on the parameters the user passed. For real life this is probably not the best idea, I
   * would add some sort of GameTypeRegistry where we could register games with their names.
   * @param args
   * @return
   */
  private def selectGame(args: Array[String]): Canonical = {
    if (args.length == 1) {
      Canonical
    } else if (args(1).toLowerCase == "spock") {
      RockPaperScissorsLizardSpock
    } else {
      println("Illegal game type. Currently the only alternate game type supported is \"spock\"")
      System.exit(0)
      Canonical // Useless, necessary because Scala doesn't know that System.exit will never return
    }
  }

  /**
   * Start a game of rock-paper-scissors
   * @param userOption The user's selected Item
   */
  private def startGame(userOption: Option[Item], definition: GameDefinition): Unit = {
    import scala.concurrent.ExecutionContext.Implicits.global
    implicit val timeout = Timeout(500, TimeUnit.MILLISECONDS)

    val system = ActorSystem("Rock-Paper-Scissors")
    val game = system.actorOf(Game.props(definition))

    val result = userOption match {
      case None ⇒
        val player1Item = generateComputerItem(definition)
        val player2Item = generateComputerItem(definition)

        println(s"Player 1 selected $player1Item")
        println(s"Player 2 selected $player2Item")

        play(game, "Player 1", "Player 2", player1Item, player2Item)

      case Some(userItem) ⇒
        val computerItem = generateComputerItem(definition)

        println(s"You selected $userItem")
        println(s"The computer selected $computerItem")

        play(game, "You", "The computer", userItem, computerItem)
    }

    result foreach { result ⇒
      println(result)
      system.shutdown
    }
  }

  /**
   * Play a given game. This is a Façade on top of the Game actor. Architecturally it would be part of the View, but for
   * the simple command-line version this is a convenient place to put it.
   * @param game The instance of the Game to play
   * @param player1Name The name of the first player (for return message purposes)
   * @param player2Name The name of the second player (for result message purposes)
   * @param player1Item The Item selected by the first player
   * @param player2Item The Item selected by the second player
   * @param ec An ExecutionContext to execute an Ask command
   * @param timeout A Timeout for getting a message back to the Actor
   * @return A Future containing a message with the result
   */
  private def play(game: ActorRef, player1Name: String, player2Name: String, player1Item: Item, player2Item: Item)(implicit ec: ExecutionContext, timeout: Timeout): Future[String] = {
    game ! player1Item

    (game ? player2Item) map {
      case Tie ⇒
        s"$player1Name tied with $player2Name"

      case Win(`player1Item`, name, p2Item) ⇒
        s"$player1Name won! $player1Item $name $player2Item"

      case Win(cItem, name, _) ⇒
        s"$player2Name won! $player2Item $name $player1Item"
    }
  }

  // $COVERAGE-OFF$ Disabling coverage for main class since it is a simple test driver
  private def generateComputerItem(definition: GameDefinition): Item = {
    val index = Random.nextInt(definition.nameToItem.size)
    definition.nameToItem.values.toIndexedSeq(index)
  }
  // $COVERAGE-ON
}