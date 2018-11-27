package tiago.ninemenmorris.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.ListPreference;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import tiago.ninemenmorris.R;
import tiago.ninemenmorris.model.Game;

public class GameSettingsActivity extends AppCompatActivity {

    private static final String TAG = "GameSettings";

    private TextView txtPlayer1;
    private TextView txtPlayer2;
    private Spinner spinnerUnplaced;
    private Spinner spinnerFlying;
    private Spinner spinnerDefaet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_settings);

        // Fetc preferences and resources
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Resources res = getResources();
        List<String> unplacedList = Arrays.asList(res.getStringArray(R.array.pref_unplaced_checkers));
        List<String> flyingList = Arrays.asList(res.getStringArray(R.array.pref_flying_cond));
        List<String> victoryList = Arrays.asList(res.getStringArray(R.array.pref_victory_cond));

        txtPlayer1 = findViewById(R.id.playerName1);
        txtPlayer2 = findViewById(R.id.playerName2);
        txtPlayer1.setText(prefs.getString("prefPlayer1name", "Player 1"));
        txtPlayer2.setText(prefs.getString("prefPlayer2name", "Player 2"));

        spinnerUnplaced = findViewById(R.id.unplacedCheckerSpin);
        spinnerUnplaced.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                unplacedList));
        spinnerUnplaced.setSelection(getSelectionIndex(unplacedList, prefs.getString("unplaced_checkers", "0")));

        spinnerFlying = findViewById(R.id.unplacedFlyingCondSpin);
        spinnerFlying.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                flyingList));
        spinnerFlying.setSelection(getSelectionIndex(flyingList, prefs.getString("flying_cond", "0")));

        spinnerDefaet = findViewById(R.id.winCondSpin);
        spinnerDefaet.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                victoryList));
        spinnerDefaet.setSelection(getSelectionIndex(victoryList, prefs.getString("victory_cond", "0")));
    }

    private int getSelectionIndex(List<String> list, String current) {
        int firstIndex = Integer.parseInt(list.get(0));
        int currentIndex = Integer.parseInt(current);
        return currentIndex - firstIndex;
    }

    public void startGame(View view) {
        String playerName1 = txtPlayer1.getText().toString();
        String playerName2 = txtPlayer2.getText().toString();
        Log.d(TAG, playerName1 + " vs " + playerName2);

        if (playerName1.isEmpty()) {
            Toast.makeText(this, "Invalid player 1 entry.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (playerName2.isEmpty()) {
            Toast.makeText(this, "Invalid player 2 entry.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (playerName2.equals(playerName1)) {
            Toast.makeText(this, "Player 1 must be different from player 2.", Toast.LENGTH_SHORT).show();
            return;
        }
        Game game = Game.getInstance();
        game.player1.name = playerName1;
        game.player2.name = playerName2;
        game.setStartingCheckers(Integer.parseInt(spinnerUnplaced.getSelectedItem().toString()));
        game.setFlyingCondition(Integer.parseInt(spinnerFlying.getSelectedItem().toString()));
        game.setLoseCondition(Integer.parseInt(spinnerDefaet.getSelectedItem().toString()));
        Game.getInstance().start();
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }
}
