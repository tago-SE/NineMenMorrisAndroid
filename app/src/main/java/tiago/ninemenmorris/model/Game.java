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

    private static final int FLYING_REQ     = 3;
    private static final int NUM_CHECKERS   = 4;
    private int unplacedRed = NUM_CHECKERS;
    private int unplacedBlue = NUM_CHECKERS;

    public static Game getInstance() {
        return ourInstance;
    }

    private Game() {
        player1 = new Player("Player 1", Color.RED);
        player2 = new Player("Player 2", Color.BLUE);
        player1.setStatePlacing();
        player2.setStatePlacing();
        gameState = GameState.PAUSED;
        board = new Board();
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
            System.out.println("MATCHING HORIZONTAL!");
            returnValue = true;
        } else if (board.allAdjacentMatchingColor(p, c, board.VERTICAL)) {
            System.out.println("MATCHING VERTICAL!");
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
        Checker checker = board.moveChecker(source, destination, getPlacedCheckers(curPlayer.color) < FLYING_REQ);
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
        else
            System.out.println("removing state");
        Checker checker = board.removeChecker(position, curPlayer.color);
        if (checker == null)
            return null;
        else
            System.out.println("failed board action");
        curPlayer.setStatePlacing();
        swapCurrentPlayer();

        return board.checkers();
    }

    public Player getCurrentPlayer() {
        return curPlayer;
    }

    private void swapCurrentPlayer() {


        /*
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
        */
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
