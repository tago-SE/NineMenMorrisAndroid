package tiago.ninemenmorris.ui.vm;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;
import android.arch.lifecycle.LiveData;
import java.util.Collection;
import java.util.List;

import tiago.ninemenmorris.model.Checker;
import tiago.ninemenmorris.model.Color;
import tiago.ninemenmorris.model.Game;
import tiago.ninemenmorris.model.Player;
import tiago.ninemenmorris.model.Position;

public class MainViewModel extends ViewModel {

    private static final String TAG = "MainVM";

    // Player data change
    public final MutableLiveData<Player> player0LiveData = new MutableLiveData<>();
    public final MutableLiveData<Player> player1LiveData = new MutableLiveData<>();
    // Remaining checker
    public final MutableLiveData<Integer> remainingRedLiveData = new MutableLiveData<>();
    public final MutableLiveData<Integer> remainingBlueLiveData = new MutableLiveData<>();
    // Checker Board changes
    public final MutableLiveData<Collection<Checker>> checkerLiveData = new MutableLiveData<>();
    // Observable for winning players
    public final SingleLiveEvent<Player> winnerLiveData = new SingleLiveEvent<>();
    // Observable for removed checkers
    public final SingleLiveEvent<Checker> removedChecker = new SingleLiveEvent<>();
    // Singleton instance of mode class Game
    private Game game;

    public MainViewModel() {
        game = Game.getInstance();
        checkerLiveData.setValue(game.getCheckers());
        winnerLiveData.setValue(null);

    }

    public void refresh() {
        postPlayerData(game.player1);
        postPlayerData(game.player2);
        postBoardData(game.getCheckers());
    }

    public void start() {
        game.start();
        postBoardData(game.getCheckers());
        postPlayerData(game.player1);
        postPlayerData(game.player2);
        winnerLiveData.setValue(null);
    }

    public boolean select(Position source, Position destination) {
        if (source == null) {
            return placeChecker(destination);
        } else {
            return moveChecker(source, destination);
        }
    }

    private boolean placeChecker(Position target) {
        Player p = game.getCurrentPlayer();
        Collection<Checker> checkers = game.placeChecker(target);
        if (checkers != null){
            postPlayerData(p);
            postBoardData(checkers);
            return true;
        }
        return false;
    }

    public boolean attemtRemove(Position position) {
        Player p = game.getCurrentPlayer();
        Collection<Checker> checkers = game.removeChecker(position);
        if (checkers != null) {
            // informs of the deleted checker
            removedChecker.postValue(game.getLastRemovedChecker());
            // informs that the board needs to be updated
            postBoardData(checkers);
            // informs that player data needs to be updated
            postPlayerData(p);
            if (game.isGameOver()) {
                winnerLiveData.setValue(game.getWinner());
            }
            return true;
        }
        return false;

    }

    private boolean moveChecker(Position source, Position target) {
        Player p = game.getCurrentPlayer();
        Collection<Checker> checkers = game.moveChecker(source, target);
        if (checkers != null){
            postPlayerData(p);
            postBoardData(checkers);
            return true;
        }
        return false;
    }


    private void postPlayerData(Player p) {
        if (p.color == Color.Red){
            remainingRedLiveData.postValue(game.getUnplacedCheckers(p.color));
        } else{
            remainingBlueLiveData.postValue(game.getUnplacedCheckers(p.color));
        }
        // Update that player data has changed
        player0LiveData.postValue(game.player1);
        player1LiveData.postValue(game.player2);
    }

    private void postBoardData(Collection<Checker> checkers) {
        checkerLiveData.postValue(checkers);
    }

    public Player getPlayerRed() {
        return game.player1;
    }

    public Player getPlayerBlue() {
        return game.player2;
    }

    public Player getCurrentPlayer() {
        return game.getCurrentPlayer();
    }

    public boolean isOccupied(Position p) {
        return game.getBoard().isOccupied(p);
    }




}
