package com.tecnoguru.rock_paper_scissors

import java.util.concurrent.TimeUnit

import akka.actor.{ ActorRef, ActorSystem }
import akka.pattern.ask
import akka.util.Timeout
import com.tecnoguru.rock_paper_scissors.games.{GameDefinition, Canonical}
import com.tecnoguru.rock_paper_scissors.games.Canonical.{ Scissors, Paper, Rock }
import com.tecnoguru.rock_paper_scissors.games.GameDefinition.{ Win, Tie, Item }

import scala.concurrent.{ ExecutionContext, Future }
import scala.util.Random

object Main {
  def main(args: Array[ String ]): Unit = {
    if (args.length != 1) {
      println(
        """
          |Hello Human. Welcome to Rock-Paper-Scissors!
          |
          |If you want to do a computer vs computer game, just pass "computer" as a command-line argument
          |If you want to play against the computer, pass in your selection (rock, paper or scissors) as a
          |command-line argument
          |
          |For example:
          |
          |$ java -jar rock_paper_scissors.jar computer
          |$ java -jar rock_paper_scissors.jar paper
          |
          |Enjoy!
        """.stripMargin)

      System.exit(0)
    }

    val userOption = args(0).toLowerCase match {
      case "rock"     ⇒ Some(Rock)
      case "paper"    ⇒ Some(Paper)
      case "scissors" ⇒ Some(Scissors)
      case "computer" ⇒ None
      case _ ⇒
        println("Illegal option. You must use rock, paper, scissors or computer")
        System.exit(0)
        None // Here so the type checker does not complain
    }

    startGame(userOption, Canonical)
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
        val player1Item = generateComputerItem
        val player2Item = generateComputerItem

        println(s"Player 1 selected $player1Item")
        println(s"Player 2 selected $player2Item")

        play(game, "Player 1", "Player 2", generateComputerItem, generateComputerItem)

      case Some(userItem) ⇒
        val computerItem = generateComputerItem

        println(s"You selected $userItem")
        println(s"The computer selected $computerItem")

        play(game, "You", "The computer", userItem, generateComputerItem)
    }

    result foreach { result ⇒
      println(result)
      system.shutdown
    }
  }

  private def play(game: ActorRef, player1Name: String, player2Name: String, player1Item: Item, player2Item: Item)(implicit ec: ExecutionContext, timeout: Timeout): Future[ String ] = {
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

  private def generateComputerItem: Item = {
    Random.nextInt(2) match {
      case 0 ⇒ Rock
      case 1 ⇒ Paper
      case 2 ⇒ Scissors
    }
  }
}