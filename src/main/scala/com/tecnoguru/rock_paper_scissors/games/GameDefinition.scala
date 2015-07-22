package com.tecnoguru.rock_paper_scissors.games

trait GameDefinition {
  protected def beats(winner: Item, name: String, loser: Item) = ???

  def check(item1: Item, item2: Item): GameDefinition.Result = ???
}

object GameDefinition {
  case class Result(winner: Item, name: String, loser: Item)
}