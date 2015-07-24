# Build instructions

You must have a JDK installed (JDK 7+). Download and install SBT, either through your package manager of choice or from [the SBT download site](http://www.scala-sbt.org/download.html)

To compile the application, execute:

```
sbt assembly
```

This will generate a "fat JAR" with all dependencies included in `target/scala-2.11/rock_paper_scissors.jar`. You can then execute the application using:

```
java -jar target/scala-2.11/rock_paper_scissors.jar <parameters>
```

If you don't pass in any parameters you will get a usage message.

# Playing from the command line

You can play from the command line, either in Player vs Computer or Computer vs Computer modes.

## Computer vs computer

To have the computer play itself, pass the parameter `computer` and, optionally, the game you wish to play. There are 2 games currently implemented. `canonical` will run the canonical game of Rock Paper Scissors, while `spock` will run a game of Rock Paper Scissors Lizard Spock. If you do not pass in a game type, the computer will play the canonical version. For example:

```
$ java -jar target/scala-2.11/rock_paper_scissors.jar computer
$ java -jar target/scala-2.11/rock_paper_scissors.jar computer canonical
$ java -jar target/scala-2.11/rock_paper_scissors.jar computer spock
```

## Player vs computer

To play against the computer, pass in your selected item as a first parameter (instead of `computer`). Again, the second parameter is the type of game. The selected item has to be valid for the game type selected and is case-insensitive. To get a list of the valid item types for the given game, you can pass any invalid item type. For example:

```
# This will play a canonical game, where you choose "Rock" as your item
$ java -jar target/scala-2.11/rock_paper_scissors.jar rock

# The same as the above
$ java -jar target/scala-2.11/rock_paper_scissors.jar rock canonical

# This will play Rock Paper Scissors Lizard Spock, where you choose "Lizard" as your item
$ java -jar target/scala-2.11/rock_paper_scissors.jar lizard spock 

# This will show the available items for Rock Paper Scissors Lizard Spock
$ java -jar target/scala-2.11/rock_paper_scissors.jar foo spock 
```

# Playing from a web browser

To start the game in web service mode, pass in `web` as the first argument:

```
$ java -jar target/scala-2.11/rock_paper_scissors.jar web 
```

This will open a web server on port 8080, listening on all interfaces. If you request the root document of that web server ([http://localhost:8080/](http://localhost:8080/) you will get a JSON list of the available game types, as URLs.

The URLs simply have the game type as first element. If you do not pass in any parameters, the computer will play itself and reply with a JSON document with the result. The document will have a structure similar to this example:

```json
{
  player1Item: "Scissors",
  player2Item: "Paper",
  winner: "player 1",
  relationship: "cuts"
}
```

To play against the computer, pass in your option in the `item` parameter ([http://localhost:8080/canonical?item=rock](http://localhost:8080/canonical?item=rock). In the results, the user is always Player 1.

There is currently no web user interface, since the main purpose of this exercise was to use Spray and Akka.

# Testing and coverage

To execute the unit tests, run:

```
sbt clean test
```

To generate the code coverage report, run:

```
sbt clean coverage test
```

The code coverage reports are written to `target/scala-2.11/scoverage-report`

# Missing functionality, extensions and other notes

Some things I didn't have the time for:

* The web service has no way to query the list of the available items.
 
* It should be possible for the user to select which port the web service will run on

* An HTML/JavaScript UI

Aside from that, the game can be easily extended for more complex game definitions (i.e. [Rock Paper Scissors Lizard Spock](http://www.samkass.com/theories/RPSSL.html)). For an example of this, see the `com.tecnoguru.rock_paper_scissors.games.RockPaperScissorsLizardSpock` class. Note that any new games you implement must also be added to the GameRegistry in the `Main#registerGames` method.

Also, since the game is implemented using Akka Actors, it can easily be extended for Player vs Player or Computer vs Computer where two different computers can play (instead of running everything in a single computer).

I would consider the MVP to be commit `9da55bf92e4e2642628c817a44f791c6f85150be`, where the system can play the canonical version through the command line.
