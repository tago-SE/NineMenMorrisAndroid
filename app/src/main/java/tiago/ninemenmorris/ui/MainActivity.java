package tiago.ninemenmorris.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceView;
import android.widget.Button;

import tiago.ninemenmorris.R;
import tiago.ninemenmorris.ui.fragments.BoardFragment;
import tiago.ninemenmorris.ui.fragments.PlayerFragment;
import tiago.ninemenmorris.ui.vm.MainViewModel;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private SurfaceView boardView;
    private PlayerFragment playerOneFrag;
    private  PlayerFragment playerTwoFrag;

    private MainViewModel mainViewModel;

    private int screenW;
    private int screenH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.w(TAG,  "onCreate:");
        setContentView(R.layout.pmain_activity);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenW = size.x;
        screenH = size.y;
        Log.w(TAG, "(" + screenW + "," + screenH + ")");

        //boardView = findViewById(R.id.board);
        //boardView.setBackgroundResource(R.drawable.nmboard);

        // Create shared view model for all sub-fragments
        //BoardFragment boardFragment = new BoardFragment();
        //getSupportFragmentManager().beginTransaction().replace(R.id.board, boardFragment).commit();

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        //boardFragment.injectMainViewModel(mainViewModel);

        //PlayerFragment playerOneFrag  = (PlayerFragment) getFragmentManager().findFragmentById(R.id.player1frag);
        //PlayerFragment playerTwoFrag  = (PlayerFragment) getFragmentManager().findFragmentById(R.id.player2frag);



        //boardFragment.injectMainViewModel(mainViewModel);
        /*
        boardView.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                switch (event.getAction()) {

                    case DragEvent.ACTION_DRAG_STARTED:
                       return true;
                    case DragEvent.ACTION_DRAG_EXITED:
                        break;
                    case DragEvent.ACTION_DRAG_ENTERED:
                        break;
                    case DragEvent.ACTION_DROP:
                        mainViewModel.drop((int) event.getX(), (int) event.getY());
                        break;
                    case DragEvent.ACTION_DRAG_ENDED:
                        break;
                    default:
                }
                return false;
            }
        });

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            boardView.post(new Runnable() {
                @Override
                public void run() {
                    ViewGroup.LayoutParams boardParam = boardView.getLayoutParams();
                    boardParam.height = boardView.getWidth();
                    boardView.setLayoutParams(boardParam);
                    boardView.postInvalidate();
                    setupDropArea();
                }
            });

        } else {
            boardView.post(new Runnable() {
                @Override
                public void run() {
                    ViewGroup.LayoutParams boardParam = boardView.getLayoutParams();
                    boardParam.width = boardView.getHeight();
                    boardView.setLayoutParams(boardParam);
                    boardView.postInvalidate();
                    setupDropArea();
                }
            });
        }
        */
    }

    private void setupDropArea() {
        Log.w("BOARD", "X: " + boardView.getLeft());
        Log.w("BOARD", "Y: " + boardView.getTop());
        Log.w("BOARD", "W: " + boardView.getWidth());
        Log.w("BOARD", "H: " + boardView.getHeight());

        int left, top, right, bottom;
        left = top = right = bottom = 5;

        Button btn = new Button(this);
        btn.setId(0);
        btn.setPadding(left, top, right, bottom);
        btn.setTop(600);
        btn.setLeft(10);
        //boardView.addView(btn);
        //boardView
    }
}
