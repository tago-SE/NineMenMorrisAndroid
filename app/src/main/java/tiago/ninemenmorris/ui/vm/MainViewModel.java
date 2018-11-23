package tiago.ninemenmorris.ui.vm;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import tiago.ninemenmorris.model.Game;
import tiago.ninemenmorris.model.Player;
import tiago.ninemenmorris.model.Position;

public class MainViewModel extends ViewModel {

    // Current player change
    @Deprecated
    public final MutableLiveData<Player> currentPlayerLiveData = new MutableLiveData<>();
    // Player data change
    public final MutableLiveData<Player> player0LiveData = new MutableLiveData<>();
    public final MutableLiveData<Player> player1LiveData = new MutableLiveData<>();
    // Remaining checker
    public final MutableLiveData<Integer> remainingRedLiveData = new MutableLiveData<>();
    public final MutableLiveData<Integer> remainingBlueLiveData = new MutableLiveData<>();

    private Game game;

    private static final String TAG = "MainVM";

    public MainViewModel() {
        game = Game.getInstance();
    }

    public void start() {
        // Update player info
        player0LiveData.postValue(game.player1);
        player1LiveData.postValue(game.player2);
        remainingRedLiveData.postValue(game.getUnplacedCheckers(game.player1));
        remainingBlueLiveData.postValue(game.getUnplacedCheckers(game.player2));

        game.start();
        // Update current active player
        currentPlayerLiveData.postValue(game.getCurrentPlayer());
    }

    public void place(Position position) {
        Log.w(TAG, "Dropped: (" + position + ")");
    }
}
