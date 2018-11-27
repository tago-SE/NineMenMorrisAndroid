package tiago.ninemenmorris.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import tiago.ninemenmorris.R;
import tiago.ninemenmorris.model.Game;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Game.getInstance().start();
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }


    public void newGame(View view) {
        Log.e(TAG, "Not implemented.");
    }

    public void loadGame(View view) {
        Log.e(TAG, "Not implemented.");
    }
}
