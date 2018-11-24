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
        if ((int) (Math.random()*100) < 50) {
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


    public Collection<Checker> moveChecker(Position source, Position destination) {
        Checker movedChecker = board.moveChecker(source, destination);
        if (movedChecker != null) {
            List<Checker> list = new ArrayList<>();
            list.addAll(board.getPlacedCheckers());
            Checker dummyChecker = new Checker(Color.TRANSPARENT, false);
            dummyChecker.position = source;
            list.add(dummyChecker);
            return list;
        }
        return null;

    }

    public Collection<Checker> placeChecker(Position position) {
        Checker placedChecker = board.placeChecker(position, curPlayer.color);
        if (placedChecker != null) {
            swapCurrentPlayer();
            return getPlacedCheckers();
        }
        return null;
    }

    public Collection<Checker> getPlacedCheckers() {
        return board.getPlacedCheckers();
    }

    public Player getCurrentPlayer() {
        return curPlayer;
    }


    @Deprecated
    private void swapCurrentPlayer() {
        // this needs to be fixed slightly
        if (getNumberOfUnplacedCheckers(player1) == 0 || getNumberOfUnplacedCheckers(player2) == 0) {
            Collection<Checker> checkers = board.getPlacedCheckers();

            for (Checker checker : checkers) {
                if (curPlayer.color == checker.color) {
                    checker.draggable = false;
                } else {
                    checker.draggable = true;
                }
            }
        }
        if (curPlayer.equals(player1)) {
            curPlayer = player2;
            player1.activeTurn = false;
        }
        else {
            curPlayer = player1;
            player1.activeTurn = true;
        }
        player2.activeTurn = !player1.activeTurn;
    }

}
