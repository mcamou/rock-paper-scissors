# Build and execution instructions

First, download and install SBT, either through your package manager of choice or from [the SBT download site](http://www.scala-sbt.org/download.html)

Now go to the project's root directory. From here you can either execute the program via SBT using:

`sbt "run <parameter>"`

Try running `sbt run` without parameters to see the valid parameters.

You can also generate a "fat JAR" for distribution, including the code and all dependencies. For this, first execute:

```
sbt assembly
```

This will leave the "fat Jar" in `target/scala-2.11/rock_paper_scissors.jar`. You can then execute the application using:

```
java -jar target/scala-2.11/rock_paper_scissors.jar <parameter>
```

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

# Extensions

The game can be easily extended for more complex definitions (i.e. [Rock Paper Scissors Lizard Spock](http://www.samkass.com/theories/RPSSL.html)). For an example of this, see the `com.tecnoguru.rock_paper_scissors.games.RockPaperScissorsLizardSpock` class. Note that any new games you implement must also be added to the GameRegistry in the `Main#registerGames` method.

Also, since the game is implemented using Akka Actors, it can easily be extended for Player vs Player option (via the HTTP interface).

