package tiago.ninemenmorris.model;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private static final Game ourInstance = new Game();

    public final Player player1;
    public final Player player2;
    private Player curPlayer;
    private GameState gameState;
    private Board board;

    public static Game getInstance() {
        return ourInstance;
    }

    private Game() {
        player1 = new Player("Player 1");
        player2 = new Player("Player 2");
        gameState = GameState.PAUSED;
        board = new Board();
    }

    public void start() {
        int random = (int) (Math.random()*1);
        if (random == 0) {
            curPlayer = player1;
            player1.activeTurn = true;
        }
        else {
            curPlayer = player2;
            player2.activeTurn = true;
        }
        gameState = GameState.PLACING;
    }

    public int getUnplacedCheckers(Player p) {
        if (p.equals(player1)) {
            return board.getNumUnplacedReds();
        } else if (p.equals(player2)) {
            return board.getNumUnplacedBlues();
        } else {
            throw new IllegalArgumentException("Invalid player");
        }
    }

    public void placeChecker(Player p, int i) {

    }

    public Player getCurrentPlayer() {
        return curPlayer;
    }

}
