package tiago.ninemenmorris.ui.vm;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import java.util.Collection;
import java.util.List;

import tiago.ninemenmorris.model.Checker;
import tiago.ninemenmorris.model.Color;
import tiago.ninemenmorris.model.Game;
import tiago.ninemenmorris.model.Player;
import tiago.ninemenmorris.model.Position;

public class MainViewModel extends ViewModel {

    private static final String TAG = "MainVM";

    // Current player change
    @Deprecated
    public final MutableLiveData<Player> currentPlayerLiveData = new MutableLiveData<>();
    // Player data change
    public final MutableLiveData<Player> player0LiveData = new MutableLiveData<>();
    public final MutableLiveData<Player> player1LiveData = new MutableLiveData<>();
    // Remaining checker
    public final MutableLiveData<Integer> remainingRedLiveData = new MutableLiveData<>();
    public final MutableLiveData<Integer> remainingBlueLiveData = new MutableLiveData<>();
    // Checker Board changes
    public final MutableLiveData<Collection<Checker>> checkerLiveData = new MutableLiveData<>();

    private Game game;



    public MainViewModel() {
        game = Game.getInstance();
        checkerLiveData.setValue(game.getCheckers());

    }

    static boolean startOnceOnly = true;

    public void refresh() {
        //checkerLiveData.postValue(game.getPlacedCheckers());
        //remainingRedLiveData.postValue(game.getNumberOfUnplacedCheckers(game.player1));
        //remainingBlueLiveData.postValue(game.getNumberOfUnplacedCheckers(game.player2));
    }

    public void start() {
       if (!startOnceOnly)
            return;
        startOnceOnly = false;

        Log.w(TAG, "start...");
        // Update player info


        game.start();
        postBoardData(game.getCheckers());
        postPlayerData(game.player1);
        postPlayerData(game.player2);
        //player0LiveData.postValue(game.player1);
        //player1LiveData.postValue(game.player2);
        //remainingRedLiveData.postValue(game.getNumberOfUnplacedCheckers(game.player1));
        //remainingBlueLiveData.postValue(game.getNumberOfUnplacedCheckers(game.player2));

        //checkerLiveData.postValue(game.getCheckers());
        /*

        */
        // Update current active player
       // currentPlayerLiveData.postValue(game.getCurrentPlayer());
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
        if (p.color == Color.RED){
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

    public Collection<Checker> getCheckers() {
        return game.getCheckers();
    }

    public Player getPlayerRed() {
        return game.player1;
    }

    public Player getPlayerBlue() {
        return game.player2;
    }

    public boolean isOccupied(Position p) {
        return game.getBoard().isOccupied(p);
    }
}
