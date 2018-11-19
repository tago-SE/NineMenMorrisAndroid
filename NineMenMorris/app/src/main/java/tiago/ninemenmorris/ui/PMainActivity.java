package tiago.ninemenmorris.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import tiago.ninemenmorris.R;
import tiago.ninemenmorris.ui.PmainFragment;

public class PMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pmain_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, PmainFragment.newInstance())
                    .commitNow();
        }
    }
}
