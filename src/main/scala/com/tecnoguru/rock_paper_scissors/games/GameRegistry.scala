package com.tecnoguru.rock_paper_scissors.games

class GameRegistry {
  private var nameToDefinition: Map[String, GameDefinition] = Map.empty

  def register(name: String, definitionProps: GameDefinition) = nameToDefinition += (name -> definitionProps)

  def getGame(name: String): Option[GameDefinition] = nameToDefinition.get(name)

  def getAvailableGames: Iterable[String] = nameToDefinition.keys
}
