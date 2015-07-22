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

