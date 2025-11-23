<img width="1000" alt="Screenshot 2023-12-06 at 1 32 53 AM" src="media-showcase/title.png">

<br>

<h2 align="center"><b>Bomb It</b></h2>

<p align="center">🎮 🎲</p>

<p align="center">Built using cross-platform Java game development library <a href="https://github.com/libgdx/libgdx">libGDX</a></p>

<p align="center">Student project 2023/24</p>

<br>

---

## 🎮 Features

This game is an implementation of mostly Bomb It 4.

Game works fully locally where one or two players can play from the same computer. Other players are bots.

<img width="1000" alt="Screenshot 2023-12-06 at 1 32 53 AM" src="media-showcase/demo-start-menu.png">

<img width="1000" alt="Screenshot 2023-12-06 at 1 32 53 AM" src="media-showcase/demo-game-menu.png">

<br>
<br>

### Game mode: Arcade

- each player can drop bombs 💣
- bombs kill any player in range (even the dropper, so move fast! 🏃)
- when killed, the player re-spawns at its starting point
- the goal is to **do as many kills as possible** 🤺
- when time runs out, the player with the most kills wins the game 🏆

<img width="1000" alt="Screenshot 2023-12-06 at 1 32 53 AM" src="media-showcase/demo-arcade-mode.gif">

<br>
<br>

### Game mode: Tile Tag

- each player has its own color 🎨
- when a player moves across a tile on the floor, the tile switches to the player's color
- similar to arcade, there are bombs as well and players can be killed
- players re-spawn at the the starting point, all of their active tiles lose color and they start at 0 again
- the goal is to **have 50 active tiles with your color**
- beware of bombs and move fast before others steal your tiles! 🏃

<img width="1000" alt="Screenshot 2023-12-06 at 1 32 53 AM" src="media-showcase/demo-tile-tag-mode.gif">

<br>
<br>

### Game dynamics

There is one or two physical players. Both play from the same computer. Others (4 in total) can be filled by bots - optional in game settings.

Each player has a starting spawn point in his respective corner. Each player also has a unique look that can be selected at the start of the game. The look also auto sets a color for the Tile Tag game mode.

With each player starting in its own corner, the map is filled with many obstacles. Obstacles can be permanent (traffic cones) or temporary, which can be removed with a bomb. The player has to remove these obstacles to move across the map, get to other players and fulfil the game goals.

Players can drop bombs and kill players in range, even the dropper. In all modes players re-spawn in their starting point. Note that in the Tile Tag this resets your progress.

<br>

<p align="center"><img width="300" alt="Screenshot 2023-12-06 at 1 32 53 AM" src="media-showcase/demo-bonuses.png"></p>

The map offers bonuses as well:

- 💣 **Bomb**
  - at the start the player can drop only one bomb and before you can drop another, the first one has to detonate
  - each collected bomb bonus increases how many active bombs a player can have
- 🧤 **Glove**
  - allows a player to be able to push an active bomb to the end of the path by bumping into it

---

## 🕹️ Bomb It game series

A widely popular bomber-like game Bomb It. Originally developed by Zlong Games in November of 2006 and published by Spil Games.

The game worked over Flash at the time. Today all of the games have been ported over and can be played via HTML5.

There are 8 versions. With the Bomb It 8 being published as recent as 2023.

---

## 💻 Get started

The game is built using Java, so a Java IDE like IntelliJ IDEA or Android Studio or similar is recommended.

The game has been tested on macOS with IntelliJ IDEA with **Java SDK 21**.

The game can be run via Gradle with the desktop build file.

```sh
./gradlew desktop:run
```

---

## ❤️ Background

The game was created as a student project during my time at computer science university FERI in Maribor, Slovenia during my 2023/24 year.
