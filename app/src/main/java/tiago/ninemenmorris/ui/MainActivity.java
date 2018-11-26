package tiago.ninemenmorris.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import tiago.ninemenmorris.R;
import tiago.ninemenmorris.model.Game;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startGame(View view) {
        Game.getInstance().start();
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    public void loadGame(View view) {

    }
}
