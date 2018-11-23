package tiago.ninemenmorris.ui;

import android.annotation.SuppressLint;
import  android.support.v4.app.Fragment;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ClipData;
import android.content.res.Configuration;
import android.graphics.Point;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import tiago.ninemenmorris.R;
import tiago.ninemenmorris.model.Position;
import tiago.ninemenmorris.ui.fragments.BoardFragment;
import tiago.ninemenmorris.ui.fragments.CheckerView;
import tiago.ninemenmorris.ui.fragments.PlayerFragment;
import tiago.ninemenmorris.ui.vm.MainViewModel;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    // size of checker relative to screen dimension
    private static final double circleFactor = 1./8.;

    private ImageView boardView;
    private ConstraintLayout layout;
    private PlayerFragment player0Frag;
    private PlayerFragment player1Frag;

    private MainViewModel mainViewModel;

    private Point screenSize;
    private int boardSize;
    private int checkerSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.w(TAG,  "onCreate:");
        setContentView(R.layout.pmain_activity);

        layout = findViewById(R.id.container);
        boardView = findViewById(R.id.board);

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        player0Frag = (PlayerFragment) getSupportFragmentManager().findFragmentById(R.id.player0frag);
        player0Frag.injectViewModel(mainViewModel, 0);
        player1Frag = (PlayerFragment) getSupportFragmentManager().findFragmentById(R.id.player1frag);
        player1Frag.injectViewModel(mainViewModel, 1);

        Display display = getWindowManager().getDefaultDisplay();
        screenSize = new Point();
        display.getSize(screenSize);
        Log.w(TAG, "(" + screenSize.x + "," + screenSize.y + ")");

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            boardView.post(new Runnable() {
                @Override
                public void run() {
                    ViewGroup.LayoutParams boardParam = boardView.getLayoutParams();
                    boardParam.height = boardView.getWidth();
                    boardParam.width = boardView.getWidth();
                    boardView.setLayoutParams(boardParam);
                    setupBoardNodes();
                    boardView.postInvalidate();
                }
            });

        } else {
            boardView.post(new Runnable() {
                @Override
                public void run() {
                    ViewGroup.LayoutParams boardParam = boardView.getLayoutParams();
                    boardParam.width = boardView.getHeight();
                    boardParam.height = boardView.getHeight();
                    boardView.setLayoutParams(boardParam);
                    setupBoardNodes();
                    boardView.postInvalidate();

                }
            });

        }




        //boardView = findViewById(R.id.board);
        //boardView.setBackgroundResource(R.drawable.nmboard);

        // Create shared view model for all sub-fragments
        //BoardFragment boardFragment = new BoardFragment();
        //getSupportFragmentManager().beginTransaction().replace(R.id.board, boardFragment).commit();

        //boardFragment.injectMainViewModel(mainViewModel);

        //PlayerFragment playerOneFrag  = (PlayerFragment) getFragmentManager().findFragmentById(R.id.player1frag);
        //PlayerFragment playerTwoFrag  = (PlayerFragment) getFragmentManager().findFragmentById(R.id.player2frag);



        //boardFragment.injectMainViewModel(mainViewModel);

        // Starts the game
        new Runnable(){
            @Override
            public void run() {
                mainViewModel.start();
            }
        }.run();
    }

    public void setupBoardNodes() {
        Log.w("BOARD", "X: " + boardView.getLeft());
        Log.w("BOARD", "Y: " + boardView.getTop());
        Log.w("BOARD", "W: " + boardView.getWidth());
        Log.w("BOARD", "H: " + boardView.getHeight());
        boardSize = boardView.getWidth();
        checkerSize = (int) (boardSize*circleFactor);

        // setup;
        int leftX = (int) boardView.getX() - checkerSize/2;
        int midX = leftX + boardView.getWidth()/2;
        int rightX = leftX + boardView.getWidth();
        int topY = (int) boardView.getY() - checkerSize/2;
        int midY =  topY + boardView.getWidth()/2;
        int botY = topY + boardView.getWidth();
        int d1 = (int) (boardView.getWidth()*0.17);

        // Top horizontal
        layout.addView(createChecker(leftX, topY, checkerSize, Position.A7));
        layout.addView(createChecker(midX, topY, checkerSize, Position.D7));
        layout.addView(createChecker(rightX, topY, checkerSize, Position.G7));
        // Bot horizontal
        layout.addView(createChecker(leftX, botY, checkerSize, Position.A1));
        layout.addView(createChecker(midX, botY, checkerSize, Position.D1));
        layout.addView(createChecker(rightX, botY, checkerSize, Position.G1));
        // Left mid vertical
        layout.addView(createChecker(leftX, midY, checkerSize, Position.A4));
        layout.addView(createChecker(leftX + d1, midY, checkerSize, Position.B4));
        layout.addView(createChecker(leftX + 2*d1, midY, checkerSize, Position.C4));
        layout.addView(createChecker(rightX, midY, checkerSize, Position.G4));
        layout.addView(createChecker(rightX - d1, midY, checkerSize, Position.F4));
        layout.addView(createChecker(rightX - 2*d1, midY, checkerSize, Position.E4));
        // Mid horizontal
        layout.addView(createChecker(midX, topY + d1, checkerSize, Position.D6));
        layout.addView(createChecker(midX, topY + 2*d1, checkerSize, Position.D5));
        layout.addView(createChecker(midX, botY - d1, checkerSize, Position.D2));
        layout.addView(createChecker(midX, botY - 2*d1, checkerSize, Position.D3));
        // Inner corners
        layout.addView(createChecker(midX - d1, (botY - 2*d1), checkerSize, Position.C3));
        layout.addView(createChecker(midX + d1, (botY - 2*d1), checkerSize, Position.E3));
        layout.addView(createChecker(midX + d1, topY + 2*d1, checkerSize, Position.E5));
        layout.addView(createChecker(midX - d1, topY + 2*d1, checkerSize, Position.C5));
        // Middle corner
        layout.addView(createChecker(midX - 2*d1, topY + d1, checkerSize, Position.B6));
        layout.addView(createChecker(midX + 2*d1, topY + d1, checkerSize, Position.F6));
        layout.addView(createChecker(midX - 2*d1, botY - d1, checkerSize, Position.B2));
        layout.addView(createChecker(midX + 2*d1, botY - d1, checkerSize, Position.F2));


    }

    @SuppressLint("ClickableViewAccessibility")
    private View createChecker(int x, int y, int size, Position position) {
        size = size*2;
        final CheckerView view = new CheckerView(this, x, y, size, position);
        view.show();
        view.draggable = false;
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (view.draggable && event.getAction() == MotionEvent.ACTION_DOWN) {
                    ClipData data = ClipData.newPlainText("", "");
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                    view.startDragAndDrop(data, shadowBuilder, view, 0);
                    return true;
                } else {
                    return false;
                }
            }
        });
        view.setOnDragListener(new View.OnDragListener() {
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
                        Log.w(TAG, "Dropped inside button: " + view.position);
                        mainViewModel.place(view.position);
                        break;
                    case DragEvent.ACTION_DRAG_ENDED:
                        break;
                    default:
                }
                return false;
            }
        });
        return view;
    }
}
