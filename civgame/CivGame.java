package civgame;

import java.util.*;

public class CivGame {
    public static void main(String[] args) {
        Game game = new Game(2, 20, 20);  // Create a new game with 2 players on 20x20 map
        game.start();
    }
}