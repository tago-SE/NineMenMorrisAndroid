package tiago.ninemenmorris.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import tiago.ninemenmorris.DB.DBHandler;

public class Game {

    private static final Game ourInstance = new Game();
    private int id;
    public final Player player1;
    public final Player player2;
    private Player curPlayer;
    private Board board;
    private boolean gameOver;
    // Requirement for flying movement
    private int flyingCondition             = 3;
    // Number of starting checkers
    private int loseCondition               = 2;
    // Starting checkers
    private int startingCheckers            = 9;
    // Number of unplaced red checkers
    private int unplacedRed;
    // Number of unplaced blue checkers
    private int unplacedBlue;
    // Last removed checker
    private Checker lastRemovedChecker;


    public static Game getInstance() {
        return ourInstance;
    }

    private Game() {
        player1 = new Player("Player 1", Color.Red);
        player2 = new Player("Player 2", Color.Blue);
    }

    public void setStartingCheckers(int startingCheckers) {
        this.startingCheckers = startingCheckers;
    }

    public void setId(int id){ this.id = id; }
    public void setGameOver(boolean gameOver){ this.gameOver = gameOver; }
    public int getId() { return id;}

    public void setFlyingCondition(int flyingCondition) {
        this.flyingCondition = flyingCondition;
    }

    public void setLoseCondition(int loseCondition) {
        this.loseCondition = loseCondition;
    }

    public Checker getLastRemovedChecker() {
        return lastRemovedChecker;
    }

    public int getFlyingCondition() {
        return flyingCondition;
    }

    public int getLoseCondition() {
        return loseCondition;
    }

    public int getUnplacedRed() {
        return unplacedRed;
    }

    public void setUnplacedRed(int unplacedRed) {
        this.unplacedRed = unplacedRed;
    }

    public int getUnplacedBlue() {
        return unplacedBlue;
    }

    public void setUnplacedBlue(int unplacedBlue) {
        this.unplacedBlue = unplacedBlue;
    }

    public void setCheckers(List<Checker> checkers) {
        board = new Board(checkers);
    }

    public void start() {
        if ((int) (Math.random()*100) < 50) {
            curPlayer = player1;
            player1.activeTurn = true;
            player2.activeTurn = false;
        }
        else {
            curPlayer = player2;
            player2.activeTurn = true;
            player1.activeTurn = false;
        }
        board = new Board();
        gameOver = false;
        player1.setStatePlacing();
        player2.setStatePlacing();
        unplacedBlue = startingCheckers;
        unplacedRed = startingCheckers;
        id = 0;
        saveGameState();
    }

    private void saveGameState() {
        // Saves the started game in the database
        (new Thread() {
            @Override
            public void run(){
                DBHandler.getInstance().insertGame();
            }
        }).start();
    }

    public List<Checker> getCheckers() {
        return board.checkers();
    }

    public Board getBoard() {
        return board;
    }

    private boolean handleMatchingColors(Position p, Color c) {
        boolean returnValue = false;
        if (board.foundMatchingMill(p, c, board.HORIZONTAL)) {
            returnValue = true;
        } else if (board.foundMatchingMill(p, c, board.VERTICAL)) {
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
        saveGameState();
        return board.checkers();
    }


    public Collection<Checker> moveChecker(Position source, Position destination) {
        // Prevent moving checkers if not in proper state
        if (!curPlayer.isInPlaceState())
            return null;
        Checker checker = board.moveChecker(source, destination, getPlacedCheckers(curPlayer.color) <= flyingCondition);
        if (checker == null)
            return null;
        if (!handleMatchingColors(destination, curPlayer.color)) {
            swapCurrentPlayer();
        }
        saveGameState();
        return board.checkers();
    }

    public Collection<Checker> removeChecker(Position position) {
        if (!curPlayer.isInRemoveState())
            return null;
        lastRemovedChecker = board.removeChecker(position, curPlayer.color);
        if (lastRemovedChecker == null)
            return null;
        curPlayer.setStatePlacing();
        swapCurrentPlayer();
        // Check remaining for new curPlayer
        if (board.countPlacedCheckersByColor(curPlayer.color) + getUnplacedCheckers(curPlayer.color) <= loseCondition) {
            gameOver = true;
            player1.activeTurn = player2.activeTurn = false;
            for (Checker c : board.getPlacedCheckers()) {
                c.draggable = false;
            }
        }
        saveGameState();
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
    public void setCurrentPlayer(Player p){
        curPlayer = p;
        if (curPlayer.equals(player1)) {
            player1.activeTurn = true;
            player2.activeTurn = false;
        } else {
            player1.activeTurn = false;
            player2.activeTurn = true;
        }
        for (Checker c : board.getPlacedCheckers()) {
            // Conditions for making placed checkers draggable
            c.draggable = curPlayer.color == c.color && getUnplacedCheckers(curPlayer.color) == 0;
        }
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
        if (c == Color.Red)
            unplacedRed += value;
        else if (c == Color.Blue)
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
        if (c == Color.Red)
            return unplacedRed;
        else if (c == Color.Blue)
            return unplacedBlue;
        return 0;
    }

    public void saveGame() throws Exception {
        //DBHandler.getInstance().save(this);
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id + '}';
    }
}
