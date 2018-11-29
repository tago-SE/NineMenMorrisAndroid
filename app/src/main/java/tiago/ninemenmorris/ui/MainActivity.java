package tiago.ninemenmorris.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import tiago.ninemenmorris.DB.DBHandler;
import tiago.ninemenmorris.R;
import tiago.ninemenmorris.model.Game;
import tiago.ninemenmorris.model.GameMetaData;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Main";
    private final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Setup default game configuration based on preferences
        final Game game = Game.getInstance();
        final DBHandler db = DBHandler.buildInstance(this);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        game.player1.name = prefs.getString("prefPlayer1name", "Player 1");
        game.player2.name = prefs.getString("prefPlayer2name", "Player 2");

        game.setLoseCondition(Integer.parseInt(prefs.getString("victory_cond", "2")));
        game.setFlyingCondition(Integer.parseInt(prefs.getString("flying_cond", "3")));
        game.setStartingCheckers(Integer.parseInt(prefs.getString("unplaced_checkers", "9")));

        // Starts a new game or the last saved session
        (new Start()).execute();
    }

    /**
     * Starts a new game or the last saved game if unfinished
     */
    private class Start extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... voids) {
            return DBHandler.getInstance().loadLastSavedGameState();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                // The last session was not complete and is restored
                Log.w(TAG, "Last session was restored");
            } else {
                // No previous session was retored and a new one is created
                Log.w(TAG, "New session was created");
                Game.getInstance().start();
            }
            Intent intent = new Intent(context, GameActivity.class);
            startActivity(intent);
            finish();   // prevents the stack from returning to MainActivity
        }
    }


}
