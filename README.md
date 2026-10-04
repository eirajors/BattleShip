# Battleship - Semester Assignment 1


In this assignment you will implement parts of the game **Battleship** and develop your own artificial intelligence (AI) to play the game.

The goal of Battleship is to find and sink all of your opponent's ships before they sink yours.

In this version of the game, each player has a 10 × 10 board and three ships. Each ship occupies *three* cells and can be placed either horizontally or vertically.

For every shot, the game reports one of the following results:

- **MISS** – the shot did not hit a ship
- **HIT** – the shot hit a ship, but the ship is not yet sunk
- **SUNK** – the shot hit the final remaining part of a ship
- **ALREADY_SHOT** – the position has already been shot at

Your job is to finish the implementation of the game and create an AI that can play Battleship.

---

## Overview

The program uses a Model-View-Controller structure similar to the programs you have worked with in INF101.

The most important parts of the program are:

- `BattleShipBoard` represents a Battleship board and keeps track of ships and shots.
- `Ship` represents a ship and keeps track of which parts of the ship have been hit.
- `BattleShipModel` controls the state of a game.
- `IStrategy` defines how an AI chooses where to shoot.
- `IPlacer` defines how an AI places its ships.
- `AIShipPlacer` is a provided baseline for random ship placement.
- `HuntTargetStrategy` is a provided baseline strategy that shoots randomly.
- `MyPlacer` is where you implement your own ship placement strategy.
- `MyStrategy` is where you implement your own shooting strategy.

The graphical interface and game controllers are provided. You should not need to modify them.

---

# Part 1 - Finish the game

The provided program contains several incomplete methods.

Your first task is to make the Battleship game function correctly.

The tests in `src/test` describe the expected behavior of the game.

You should first make the tests for `Ship` and `BattleShipBoard` pass before moving on to the AI.

---

## Task 1 - Ship

The `Ship` class represents a single ship.

A ship has:

- a starting position
- a direction
- three positions on the board
- information about which positions have been hit

Some functionality has already been implemented for you.

### TODO: Implement `Ship::hit`

The `hit` method should register a hit on the ship.

The method should:

- return `true` if a new hit was successfully registered
- return `false` if the position does not belong to the ship
- return `false` if that position has already been hit

A position should only count as one hit.

---

## Task 2 - Check whether a ship is sunk

### TODO: Implement `Ship::isSunk`

A ship is sunk when all of its positions have been hit.

The method should return:

- `false` when one or more parts of the ship have not been hit
- `true` when every part of the ship has been hit

Run `ShipTest` to check your implementation.

---

# Part 2 - Implement the Battleship board

The `BattleShipBoard` class represents one player's board.

The board has:

- size 10 × 10
- three ships
- information about all shots that have been made

You must implement the following methods.

---

## Task 3 - Place ships

### TODO: Implement `BattleShipBoard::placeShip`

The method receives:

- a starting position
- a direction

It should create and place a ship on the board if the placement is valid.

A placement is valid when:

- fewer than three ships have already been placed
- every part of the ship is inside the board
- the ship does not overlap an existing ship

Ships are allowed to touch each other.

The method should return:

- `true` if the ship was successfully placed
- `false` if the placement is invalid


---

## Task 4 - Shoot at the board

### TODO: Implement `BattleShipBoard::shoot`

The method receives a position and should return the result of shooting at that position.

The possible results are:

- `MISS`
- `HIT`
- `SUNK`
- `ALREADY_SHOT`

If the position has already been shot at, the method should return `ALREADY_SHOT`.

If a ship is hit, the corresponding `Ship` object must be informed about the hit.

If the hit sinks the ship, the method should return `SUNK`.

If there is no ship at the position, the method should return `MISS`.

Shooting outside the board should throw an `IllegalArgumentException`.

---

## Task 5 - Check whether the game is won

### TODO: Implement `BattleShipBoard::allShipsSunk`

The method should return `true` only when:

1. all three ships have been placed, and
2. every ship has been sunk.

Otherwise it should return `false`.

At this point you shouldbe able to run `BattleShipMain.java` to play the Human vs AI version of the game.

---

# Part 3 - Create your own AI

Now that the game works, it is time to create an AI that can play Battleship.

Two baseline implementations are provided.

They give you something to compare your own AI against.

---

## Baseline ship placement

`AIShipPlacer` is a provided strategy that places ships randomly.

It chooses random starting positions and random directions until all ships have been placed.

This is intentionally a simple strategy.

You should use it as a baseline when evaluating your own ship placement strategy.

**Do not modify `AIShipPlacer`.**

---

## Baseline shooting strategy

`HuntTargetStrategy` is also provided as a baseline.

Despite its name, this version simply chooses a random position that has not been shot at before.

It does not use information from previous hits to improve its future shots.

This makes it a useful baseline for evaluating your own shooting strategy.

**Do not modify `HuntTargetStrategy`.**

---

# Task 6 - Ship placement strategy

### TODO: Implement `MyPlacer::placeShips`

Your goal is to create a better ship placement strategy than the random baseline.

Implement your strategy in:

`MyPlacer.java`

Your strategy must place all three ships legally on the board.

You should think about how an opponent is likely to search the board.

For example, consider:

- whether your ships should be spread out
- whether some positions are more likely to be attacked
- whether your ships should be placed close together or far apart
- whether predictable patterns make your ships easier to find

You are free to choose your own strategy.

The important requirement is that your strategy should be your own implementation and should place all ships successfully.

---

# Task 7 - Shooting strategy

### TODO: Implement `MyStrategy`

Your shooting strategy is implemented in:

`MyStrategy.java`

The strategy has two important methods:

```java
chooseShot()

registerShot(CellPosition position, boolean hit)
``` 
The first method chooses which location you want to attack.
The second method gives you the feedback on your previous attack and it is upto you on how to use it for your AI.


For this task you must explain your code. What was your idea for your strategy?

As every ship has a length of 3, by shooting every third in a row you would be guaranteed to hit at least one CellPosition of each ship, making that my default method. To optimalize this method, I used a pointer for the row, such that the method didn't have to go through every hit position, until one didn't return null. 
If a ship was hit (the boolean variable isHit was changed to true), the idea was to shoot around that position. The problem then, was that it didn't always get all the positions. This was because if two ships (often vertical and horizontal) were next to each other, the shooting method could mistake the vertical ship as a part of the horizontal ship. I then changed it so that it searchs until it hits a miss in every direction of the original hitposition, regardless if a ship is sunk. Still, if the the original hitposition was not a part of the vertical ship, it wouldn´t always hit the last position. 
I then created a failsafe method called checkAroundHitpositions, which looks around every hitposition that it has found. This would be the method which was used after shooteveryThird() had finished, and there were still some missing ships. It will look through all the hitpositions, and send every one of them to the lookAround method. For every try, this has always worked, but to be completely safe, I created a last shootUnshot method, which shoots all the unshot positions just to be safe. 
