package tiago.ninemenmorris.model;

import java.util.ArrayList;
import java.util.Collection;
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
        player1 = new Player("Player 1", Color.RED);
        player2 = new Player("Player 2", Color.BLUE);
        gameState = GameState.PAUSED;
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
        board = new Board();
    }

    public int getNumberOfUnplacedCheckers(Player p) {
        if (p.equals(player1)) {
            return board.getNumUnplacedReds();
        } else if (p.equals(player2)) {
            return board.getNumUnplacedBlues();
        } else {
            throw new IllegalArgumentException("Invalid player");
        }
    }

    public Checker placeChecker(Position position) {
        Checker placedChecker = board.placeChecker(position, curPlayer.color);
        if (placedChecker != null) {
            swapCurrentPlayer();
        }
        return placedChecker;
    }

    public Collection<Checker> getPlacedCheckers() {
        return board.getPlacedCheckers();
    }

    public Player getCurrentPlayer() {
        return curPlayer;
    }

    private void swapCurrentPlayer() {
        if (curPlayer.equals(player1))
            curPlayer = player2;
        else
            curPlayer = player1;
    }

}
