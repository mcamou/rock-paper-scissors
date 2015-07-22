package com.tecnoguru.rock_paper_scissors.games

/**
 * All game definitions should implement this trait
 */
trait GameDefinition {
  import GameDefinition._

  /**
   * Here we store the rules for this specific game
   */
  private var rules: Map[(Item, Item), String] = Map.empty

  /**
   * Called by subclasses to define the rules of a specific game. Should be called once per each item pair.
   * @param winner The winning item in this relationship
   * @param name The name of this relationship (what does this relationship "mean")
   * @param loser The losing item in this relationship
   */
  protected def beats(winner: Item, name: String, loser: Item) = {
    rules += (winner, loser) -> name
  }

  /**
   * Check whether an item beats another item
   * @param item1 The first item to check
   * @param item2 The second item to check
   * @return A Result indicating which item wins, which one loses, and what the relationship between them is
   */
  def check(item1: Item, item2: Item): Result = {
    if (item1 == item2) {
      Tie
    } else {
      val winner1 = rules.get((item1, item2))
      val winner2 = rules.get((item2, item1))

      (winner1, winner2) match {
        case (Some(name), None) ⇒ Win(item1, name, item2)
        case (None, Some(name)) ⇒ Win(item2, name, item1)
        case _                  ⇒ throw new IllegalStateException("Illegal game")
      }
    }
  }
}

/**
 * Companion class of the GameDefinition trait
 */
object GameDefinition {
  /**
   * Marker trait for Items used in GameDefinitions
   */
  trait Item

  /**
   * Sealed trait to mark the different types of results
   */
  trait Result

  /**
   * The Result of a specific check, indicating which item wins, which one loses, and what the relationship between them is
   * @param winner The winning item of the contest
   * @param name The name of the relationship
   * @param loser The losing item of the contest
   */
  case class Win(winner: Item, name: String, loser: Item) extends Result

  /**
   * Case object to mark that the result of the game is a tie
   */
  case object Tie extends Result
}