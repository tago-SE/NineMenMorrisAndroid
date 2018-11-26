package tiago.ninemenmorris.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Game {

    private static final Game ourInstance = new Game();
    public int id;
    public final Player player1;
    public final Player player2;
    private Player curPlayer;
    private Board board;
    private boolean gameOver;

    private static final int FLYING_REQ     = 3;
    private static final int NUM_CHECKERS   = 9;
    private int unplacedRed;
    private int unplacedBlue;

    public static Game getInstance() {
        return ourInstance;
    }

    private Game() {
        player1 = new Player("Player 1", Color.RED);
        player2 = new Player("Player 2", Color.BLUE);
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
        board = new Board();
        gameOver = false;
        player1.setStatePlacing();
        player2.setStatePlacing();
        unplacedBlue = NUM_CHECKERS;
        unplacedRed = NUM_CHECKERS;
    }

    public List<Checker> getCheckers() {
        return board.checkers();
    }

    public Board getBoard() {
        return board;
    }

    private boolean handleMatchingColors(Position p, Color c) {
        boolean returnValue = false;
        if (board.allAdjacentMatchingColor(p, c, board.HORIZONTAL)) {
            returnValue = true;
        } else if (board.allAdjacentMatchingColor(p, c, board.VERTICAL)) {
            returnValue = true;
        }
        if (returnValue) {
            curPlayer.setStateRemoving();
            for (Checker checker : board.getPlacedCheckers())
                checker.draggable = false;
        }
        return returnValue;
    }

    public Collection<Checker> placeChecker(Position position) {
        // Prevent placing new checkers if not in proper state
        if (!curPlayer.isInPlaceState())
            return null;
        Checker checker = board.placeChecker(position, curPlayer.color);
        if (checker == null)
            return null;
        addUnplacedCheckers(curPlayer.color, -1);
        if (!handleMatchingColors(position, curPlayer.color)) {
            swapCurrentPlayer();
        }
        return board.checkers();
    }


    public Collection<Checker> moveChecker(Position source, Position destination) {
        // Prevent moving checkers if not in proper state
        if (!curPlayer.isInPlaceState())
            return null;
        Checker checker = board.moveChecker(source, destination, getPlacedCheckers(curPlayer.color) <= FLYING_REQ);
        if (checker == null)
            return null;
        if (!handleMatchingColors(destination, curPlayer.color)) {
            swapCurrentPlayer();
        }
        return board.checkers();
    }

    public Collection<Checker> removeChecker(Position position) {
        if (!curPlayer.isInRemoveState())
            return null;
        Checker checker = board.removeChecker(position, curPlayer.color);
        if (checker == null)
            return null;
        curPlayer.setStatePlacing();
        swapCurrentPlayer();
        // Check remaining for new curPlayer
        if (board.countPlacedCheckersByColor(curPlayer.color) + getUnplacedCheckers(curPlayer.color) <= 2) {
            gameOver = true;
            player1.activeTurn = player2.activeTurn = false;
            for (Checker c : board.getPlacedCheckers()) {
                c.draggable = false;
            }
        }
        return board.checkers();
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public Player getWinner() {
        if (!gameOver)
            return null;
        if (curPlayer == player1)
            return player2;
        return player1;
    }

    public Player getCurrentPlayer() {
        return curPlayer;
    }

    private void swapCurrentPlayer() {
        if (curPlayer.equals(player1)) {
            curPlayer = player2;
            player1.activeTurn = false;
        }
        else {
            curPlayer = player1;
            player1.activeTurn = true;
        }
        player2.activeTurn = !player1.activeTurn;
        for (Checker c : board.getPlacedCheckers()) {
            // Conditions for making placed checkers draggable
            c.draggable = curPlayer.color == c.color && getUnplacedCheckers(curPlayer.color) == 0;
        }
    }

    private void addUnplacedCheckers(Color c, int value) {
        if (c == Color.RED)
            unplacedRed += value;
        else if (c == Color.BLUE)
            unplacedBlue += value;
    }

    public int getPlacedCheckers(Color color) {
        int counter = 0;
        for (Checker c : board.getPlacedCheckers()) {
            if (c.color == color)
                counter++;
        }
        return counter;
    }

    public int getUnplacedCheckers(Color c) {
        if (c == Color.RED)
            return unplacedRed;
        else if (c == Color.BLUE)
            return unplacedBlue;
        return 0;
    }
}
