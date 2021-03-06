# Game rules

* There are a series of Items. In the canonical version of the game, there are three:
> * Rock
> * Paper
> * Scissors

* The Items are ranked in a circular fashion, so that there is no one "best" item. Also, each rank relationship is expressed by means of a verb: For example, in the canonical version of the game:
> * Rock > Scissors (Rock breaks scissors)
> * Paper > Rock (Paper wraps rock)
> * Scissors > Paper (Scissors cut paper)

* There are extended versions of the game with more than three Items (i.e., rock-paper-scissors-lizard-spock)

# Game mechanics

* In the canonical version of the game, there are only two participants.

* Each participant selects an Item and keeps it secret from his/her opponent

* At a specific point in time, both participants show their selected item

* The winner is the participant with the highest-ranked item

# Design considerations

* The initial implementation (MVP) needs to be able to play the canonical version of rock-paper-scissors, however, it should be easily extensible for other versions.

* The MVP should allow both for Player vs Computer and Computer vs Computer.

# NOTES (these are a bit stream-of-consciousness)

It is probably best to start with a core that encodes the general rules, on top of which we can build different user interfaces.

Regarding representation of the Items, I see a couple of options:

* Using case objects

* Reading them from a configuration

For the MVP, using case objects is probably the easiest, since it will allow us to do away with the additional (non-problem-related) code to read in the Items and Rules from a file, plus we get compile-time checking . The disadvantage is that we need to recompile each time we add a new game variation, but for an initial implementation I believe the tradeoff is acceptable.

The game rules could be expressed as a digraph. However, a full-blown digraph is probably overkill for this application since we will not do any graph walking, we only need to jump from one node to the next to see who beats who. We could use a Map[Item, Seq[Tuple2[Item, String]]], where the key to the map is the winning Item and the value is the losing Item and the String is the name of the relationship (for presentation purposes).

Thinking it a bit more thoroughly, a better structure is a Map[Tuple2[Item, Item], String]], where the key to the map is a tuple containing the winning and the losing Item and the value is the name of the relationship (for presentation purposes).

For ease of game definition, it would be nice to have some sort of DSL, so that you could define the rules similar to this:

```scala
object RockPaperScissors extends GameDescription {
  rules (
    Rock breaks Scissors,
    Paper wraps Rock,
    Scissors cut Paper,
  )
}	
```
However, for initial implementation, the following syntax should be sufficient:

```scala
class RockPaperScissors extends GameDescription {
  beats(Rock, "breaks", Scissors)
  beats(Paper, "wraps", Rock)
  beats(Scissors, "cuts", Paper)
}	
```

Thinking about Akka, it makes sense for the Game to be an Actor. It would then receive the players' guesses, and as soon as it receives 2 guesses it would send back the result of the game to both participants. This enables us to decouple the business rules from the different clients. Also, having the reply go to both senders would enable Human-to-Human gameplay in the future.

As far as the UI is concerned, an initial version would use the command line. We can later add a REST interface.

The system should validate that there are no conflicting/duplicate rules. Ideally it should also validate that the game is complete (i.e. that there are no missing options)

It would also be good to have some sort of Game Registry, so that we could give the user the option of starting a different Game type each time.

Having implemented the command-line version, it's time to implement a web version. The initial idea would be to implement a simple interface over HTTP. JSON and a full REST protocol are probably overkill for this, perhaps a single URI such as this:

```
/<game_type>/<item>
```

If no `<item>` is provided, a computer-vs-computer game would be executed.

The reply would initially be just text, similar to what the command-line version shows. A next enhancement would be to reply with JSON and provide a simple web front-end.

I will add an additional option when running the application from the command line. When running with the `web` parameter, it will start a web server. The second parameter would be the port number, using a reasonable default such as 8080 or 8000.
 
For this a small refactoring is needed so we don't duplicate code in the Main method. Also, having those println statements in 2 or 3 methods in the Main class is not a good design (although it worked for getting something up and running quickly).

After a second thought, the user's selected item should probably be a URL parameter, so the URI would be:

```
/<game_type>?item=<item>
```

To me this feels a bit better. We could also have a full JSON body for the user's selection but it's probably
overkill for this. If you want to play a computer vs computer game, you leave out the item parameter.

For unit testing purposes, we need to create the GameRegistry and inject it into the ApiService. That will allow us to inject a mock game registry during testing. In the future we can make it an Actor and have it also create the Games, so that we can have, for example, a backend server which runs the games and a frontend server which runs only the web interface) but for the current problem that is overkill (YAGNI). A part here that is a bit ugly is the population of this GameRegistry. I would like to have it auto-populated by the GameDefinitions, but this will do away with the possibility of plugging in a different one for testing. Therefore, the population will currently have to be done by the Main class.