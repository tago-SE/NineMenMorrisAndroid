package tiago.ninemenmorris.ui.vm;

import android.arch.lifecycle.ViewModel;
import android.util.Log;

public class MainViewModel extends ViewModel {

    private static final String TAG = "MainVM";

    public MainViewModel() {}

    void configureBoard(int x, int y, int w, int h) {

    }

    public void drop(int x, int y) {
        Log.w(TAG, "Dropped: (" + x + "," + y + ")");
    }
}
