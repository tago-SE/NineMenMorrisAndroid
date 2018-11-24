package tiago.ninemenmorris.ui;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.content.ClipDescription;
import android.support.annotation.Nullable;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import tiago.ninemenmorris.R;
import tiago.ninemenmorris.model.Checker;
import tiago.ninemenmorris.model.Color;
import tiago.ninemenmorris.model.Player;
import tiago.ninemenmorris.model.Position;
import tiago.ninemenmorris.ui.fragments.BoardFragment;
import tiago.ninemenmorris.ui.fragments.CheckerView;
import tiago.ninemenmorris.ui.fragments.PlayerFragment;
import tiago.ninemenmorris.ui.vm.MainViewModel;

public class GameActivity extends AppCompatActivity {

    private static final String TAG = "GameActivity";

    // size of checker relative to screen dimension
    private static final double circleFactor = 1./8.;
    private Hashtable<Position, View> nodeMap = new Hashtable<>();
    private List<CheckerView> checkerViewList = new ArrayList<>();

    private ImageView boardView;
    private ConstraintLayout layout;
    private PlayerFragment player0Frag;
    private PlayerFragment player1Frag;
    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.w(TAG,  "onCreate:");
        setContentView(R.layout.pmain_activity);

        layout = findViewById(R.id.container);
        boardView = findViewById(R.id.board);

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        // Setup Player Fragments
        player0Frag = (PlayerFragment) getSupportFragmentManager().findFragmentById(R.id.player0frag);
        player0Frag.injectViewModel(mainViewModel);
        player0Frag.injectPlayer(mainViewModel.getPlayerRed());

        player1Frag = (PlayerFragment) getSupportFragmentManager().findFragmentById(R.id.player1frag);
        player1Frag.injectViewModel(mainViewModel);
        player1Frag.injectPlayer(mainViewModel.getPlayerBlue());

        // Get screen dimensions
        Display display = getWindowManager().getDefaultDisplay();
        Point screenSize = new Point();
        display.getSize(screenSize);

        Log.w(TAG, "(" + screenSize.x + "," + screenSize.y + ")");


        // Updates UI on observed checker changes

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
                Log.w(TAG, "start");
                mainViewModel.start();
            }
        }.run();
    }

    private void setupBoardNodes() {
        int checkerSize = (int) (boardView.getWidth()*circleFactor);
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
        // Checker update handler
        handleObservedCheckers();
    }

    private void handleObservedCheckers() {
        mainViewModel.checkerLiveData.observe(this, new Observer<Collection<Checker>>() {
            @Override
            public void onChanged(@Nullable Collection<Checker> checkers) {
                if (checkers == null)
                    return;
                for (Checker checker : checkers) {
                    CheckerView view = (CheckerView) nodeMap.get(checker.getPosition());
                    if (checker.getColor() == Color.RED) {
                        view.paintRed();
                        view.show();
                    }
                    else if (checker.getColor() == Color.BLUE) {
                        view.paintBlue();
                        view.show();
                    } else {
                        view.hide();
                    }
                    view.draggable = checker.isDraggable();
                }
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mainViewModel.refresh();
    }

    @SuppressLint("ClickableViewAccessibility")
    private View createChecker(int x, int y, int size, Position position) {
        final CheckerView view = new CheckerView(this, x, y, size, position);
        view.show();
        view.draggable = false;
        // Maps the view to the given position
        nodeMap.put(position, view);
        checkerViewList.add(view);
        Log.d(TAG, "created: " + view.toString());

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (view.draggable && event.getAction() == MotionEvent.ACTION_DOWN) {
                    // Save the position as clip data to be retrieved on drop
                    ClipData.Item item = new ClipData.Item("" + view.position);
                    ClipData data = new ClipData("", new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);
                    // Start drag
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
                        // get stored position inside clip data if any exists
                        String data = (String) event.getClipData().getItemAt(0).getText();
                        Position sourcePosition = null;
                        if (!data.equals(""))
                            sourcePosition = Position.valueOf(data);

                        mainViewModel.dropChecker(sourcePosition, view.position);
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

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "ONSTOP CALLED");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "ONSTART CALLED");
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
                    mainViewModel.refresh();
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
                    mainViewModel.refresh();
                }
            });
        }
    }


}
