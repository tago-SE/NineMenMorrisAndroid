package tiago.ninemenmorris.ui.vm;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import java.util.Collection;

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
    }

    static boolean startOnceOnly = true;

    public void refresh() {
        checkerLiveData.postValue(game.getPlacedCheckers());
        remainingRedLiveData.postValue(game.getNumberOfUnplacedCheckers(game.player1));
        remainingBlueLiveData.postValue(game.getNumberOfUnplacedCheckers(game.player2));
    }

    public void start() {
        if (!startOnceOnly)
            return;
        startOnceOnly = false;

        Log.w(TAG, "start...");
        // Update player info

        checkerLiveData.setValue(null);


        game.start();
        player0LiveData.postValue(game.player1);
        player1LiveData.postValue(game.player2);
        remainingRedLiveData.postValue(game.getNumberOfUnplacedCheckers(game.player1));
        remainingBlueLiveData.postValue(game.getNumberOfUnplacedCheckers(game.player2));
        /*

        */


        // Update current active player
        currentPlayerLiveData.postValue(game.getCurrentPlayer());
    }

    public Player getPlayerRed() {
        return game.player1;
    }

    public Player getPlayerBlue() {
        return game.player2;
    }

    public void placeChecker(Position position) {
        Player p = game.getCurrentPlayer();
        Checker placedChecker = game.placeChecker(position);
        if (placedChecker != null){

            if (p.color == Color.RED){
                remainingRedLiveData.postValue(game.getNumberOfUnplacedCheckers(p));
                checkerLiveData.postValue(game.getPlacedCheckers());
            } else{
                remainingBlueLiveData.postValue(game.getNumberOfUnplacedCheckers(p));
                checkerLiveData.postValue(game.getPlacedCheckers());
            }

        }
    }
}
