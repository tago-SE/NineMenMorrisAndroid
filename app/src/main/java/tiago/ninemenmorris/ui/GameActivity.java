package tiago.ninemenmorris.ui;

import android.animation.ObjectAnimator;
import android.arch.lifecycle.Observer;
import android.content.ClipDescription;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ClipData;
import android.content.res.Configuration;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

import tiago.ninemenmorris.DB.DBHandler;
import tiago.ninemenmorris.R;
import tiago.ninemenmorris.model.Checker;
import tiago.ninemenmorris.model.Color;
import tiago.ninemenmorris.model.Game;
import tiago.ninemenmorris.model.Player;
import tiago.ninemenmorris.model.Position;
import tiago.ninemenmorris.ui.fragments.CheckerView;
import tiago.ninemenmorris.ui.fragments.PlayerFragment;
import tiago.ninemenmorris.ui.vm.MainViewModel;

public class GameActivity extends AppCompatActivity {

    private static final String TAG = "GameActivity";

    // size of checker relative to screen dimension
    private static final double circleFactor = 1./8.;
    private static final int VICTORY_DELAY = 5000;
    private Hashtable<Position, View> nodeMap = new Hashtable<>();
    private List<CheckerView> checkerViewList = new ArrayList<>();

    private ImageView boardView;
    private ConstraintLayout layout;
    private PlayerFragment player0Frag;
    private PlayerFragment player1Frag;
    private MainViewModel mainViewModel;

    private Hashtable<Position, Point> pointMap = null;
    private int checkerSize;
    private int boardSize;
    private Point screenSize;

    private final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        screenSize = new Point();
        display.getSize(screenSize);
        handleObservedWinner();
    }

    /*
    @Override
    protected void onStop() {
        super.onStop();

        Thread saveGameThread = new Thread(){

            @Override
            public void run(){
                mainViewModel.saveGame();
                Log.e(TAG, "IN THREAD");
            }
        };

        saveGameThread.start();

        Log.e(TAG, "THREAD FINISHED");
    }*/

    @Override
    protected void onDestroy(){
        super.onDestroy();


        Thread saveGameThread = new Thread(){

            @Override
            public void run(){
                mainViewModel.saveGame();
                Log.e(TAG, "IN THREAD");
            }
        };

        saveGameThread.start();

        Log.e(TAG, "THREAD FINISHED");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mainViewModel.refresh();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.restart:
                mainViewModel.start();
                return true;
            case R.id.newgame:
                startActivity(new Intent(this, GameSettingsActivity.class));
                return true;
            case R.id.loadgame:
                // Load Game Activity
                startActivity(new Intent(this, LoadActivity.class));
                return true;
            case R.id.gamepref:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    private void setupDrag(final CheckerView view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    // Save the position as clip data to be retrieved on drop
                    ClipData.Item item = new ClipData.Item("" + view.position);
                    ClipData data = new ClipData("", new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);
                    // Start drag
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        view.startDragAndDrop(data, shadowBuilder, null, 0);
                    } else {
                        view.startDrag(data, shadowBuilder, null, 0);
                    }
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        (new Handler()).post(new Runnable() {
            @Override
            public void run() {
                // Changes the boardSize to match the screen dimensions
                boardSize = (int) ((screenSize.x > screenSize.y ? screenSize.y : screenSize.x)*0.72);
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    ViewGroup.LayoutParams boardParam = boardView.getLayoutParams();
                    boardParam.height = boardSize;
                    boardParam.width = boardSize;
                    boardView.setLayoutParams(boardParam);
                    boardView.invalidate();
                } else {
                    ViewGroup.LayoutParams boardParam = boardView.getLayoutParams();
                    boardParam.height = boardSize;
                    boardParam.width = boardSize;
                    boardView.setLayoutParams(boardParam);
                    boardView.invalidate();
                }
                // This section of the code needs to run post onStart so that boardView.width and
                // boardView.height has been properly updated.
                (new Handler()).post(new Runnable() {
                    @Override
                    public void run() {
                       // Setup after boardView was enlarged
                        SetupPostBoardViewRescale();
                    }
                });
            }
        });
    }

    private void SetupPostBoardViewRescale() {
        mapCoordinatesOnScreen();
        handleObservedCheckers();
        handleObservedRemovedCheckers();
        layout.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        return true;
                    case DragEvent.ACTION_DRAG_ENDED:
                        return true;
                    case DragEvent.ACTION_DRAG_EXITED:
                        return false;
                    case DragEvent.ACTION_DRAG_LOCATION:
                        return true;
                    case DragEvent.ACTION_DRAG_ENTERED:
                        return false;
                    case DragEvent.ACTION_DROP:
                        int x = (int) (event.getX() + layout.getX());
                        int y = (int) (event.getY() + layout.getY());
                        Log.w(TAG, "Dropped at: " + x + "," + y);
                        CheckerView checkerView = getCheckerViewAtPosition(x, y);
                        if (checkerView == null)
                            return false;
                        // Handle Drop Checker event
                        if (checkerView.rect.contains(x, y)) {
                            if (mainViewModel.isOccupied(checkerView.position))
                                return false; // unsuccessful drop
                            // get stored source position inside clip data if any exists
                            String data = (String) event.getClipData().getItemAt(0).getText();
                            Position source = null;
                            Position target = checkerView.position;
                            if (!data.equals(""))
                                source = Position.valueOf(data);
                            if (mainViewModel.select(source, target)) {
                                // Follow up actions if place / move was successful
                                Log.w(TAG, "OnPlace/Move");
                                Player player = mainViewModel.getCurrentPlayer();
                                if (player.isInRemoveState()) {
                                    Toast.makeText(context, player.color + " may remove opponent checker.", 4).show();
                                }
                                return true;
                            }
                            return false;
                        }
                        return false; // No droppable nodes found
                    default:
                        return true;
                }
            }
        });
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int x = (int) event.getX();
                int y = (int) event.getY();
                CheckerView checkerView = getCheckerViewAtPosition(x, y);
                if (checkerView == null)
                    return false;
                if (mainViewModel.attemtRemove(checkerView.position)) {
                    // Follow up actions if the remove was successful
                    Log.w(TAG, "OnRemove");
                }
                return true;
            }
        });
        mainViewModel.refresh();
    }

    private CheckerView getCheckerMatching(Position p) {
        for (CheckerView cv : checkerViewList) {
            if (cv.position == p)
                return cv;
        }
        return null;
    }

    private CheckerView getCheckerViewAtPosition(int x, int y) {
        for (CheckerView cv : checkerViewList) {
            if (cv.rect.contains(x, y))
                return cv;
        }
        return null;
    }

    private void handleObservedWinner() {
        mainViewModel.winnerLiveData.observe(this, new Observer<Player>() {
            @Override
            public void onChanged(@Nullable Player player) {
                if (player == null) return;
                Toast.makeText(context, player.name + " has won!", 4).show();
            }
        });
    }

    private void handleObservedCheckers() {
        mainViewModel.checkerLiveData.observe(this, new Observer<Collection<Checker>>() {
            @Override
            public void onChanged(@Nullable Collection<Checker> checkers) {
                if (checkers == null)
                    return;
                paintBoard(checkers);
            }
        });
    }

    private void handleObservedRemovedCheckers() {
        mainViewModel.removedChecker.observe(this, new Observer<Checker>() {
            @Override
            public void onChanged(@Nullable Checker checker) {
                if (checker == null) return;
                CheckerView cv = getCheckerMatching(checker.getPosition());
                checkerViewList.remove(cv);
                ObjectAnimator anim = ObjectAnimator.ofFloat(cv, "translationY", screenSize.y);
                anim.setDuration(1000);
                anim.start();
            }
        });
    }

    private void paintBoard(Collection<Checker> checkers) {
        Log.d(TAG, "Update Board");
        // Remove previously placed CheckerViews
        for (CheckerView old : checkerViewList) {
            ((ViewManager) old.getParent()).removeView(old);
        }
        checkerViewList.clear();
        // Create new CheckerViews based on received data
        for (Checker checker : checkers) {
            Color color = checker.getColor();
            Position coordinate = checker.getPosition();
            Point p = pointMap.get(coordinate);
            final CheckerView checkerView = new CheckerView(context, p.x, p.y, checkerSize, coordinate);
            if (color == Color.Red) {
                checkerView.paintRed();
                checkerView.show();
            } else if (color == Color.Blue) {
                checkerView.paintBlue();
                checkerView.show();
            } else {
                checkerView.hide();
            }
            if (checker.isDraggable()) {
                setupDrag(checkerView);
            }
            checkerViewList.add(checkerView);
            layout.addView(checkerView);
        }
    }


    private void mapCoordinatesOnScreen() {
        if (pointMap != null)
            return;
        // Setup

        Log.w(TAG, ": Map coordinates");
        Log.w(TAG, "maxSize: " + boardSize);
        Log.w(TAG, "board width: " + boardView.getWidth());
        Log.w(TAG, "board height: " + boardView.getHeight());


        checkerSize = (int) (boardView.getWidth()*circleFactor);
        int leftX = (int) boardView.getX() - checkerSize/2;
        int midX = leftX + boardView.getWidth()/2;
        int rightX = leftX + boardView.getWidth();
        int topY = (int) boardView.getY() - checkerSize/2;
        int midY =  topY + boardView.getWidth()/2;
        int botY = topY + boardView.getWidth();
        int d1 = (int) (boardView.getWidth()*0.17);

        // Map coordinates
        pointMap = new Hashtable<>();
        // Top horizontal
        pointMap.put(Position.A7, new Point(leftX, topY));
        pointMap.put(Position.D7, new Point(midX, topY));
        pointMap.put(Position.G7, new Point(rightX, topY));
        // Bot horizontal
        pointMap.put(Position.A1, new Point(leftX, botY));
        pointMap.put(Position.D1, new Point(midX, botY));
        pointMap.put(Position.G1, new Point(rightX, botY));
        // Left mid vertical
        pointMap.put(Position.A4, new Point(leftX, midY));
        pointMap.put(Position.B4, new Point(leftX + d1, midY));
        pointMap.put(Position.C4, new Point(leftX + 2*d1, midY));
        pointMap.put(Position.G4, new Point(rightX, midY));
        pointMap.put(Position.F4, new Point(rightX - d1, midY));
        pointMap.put(Position.E4, new Point(rightX - 2*d1, midY));
        // Mid horizontal
        pointMap.put(Position.D6, new Point(midX, topY + d1));
        pointMap.put(Position.D5, new Point(midX, topY + 2*d1));
        pointMap.put(Position.D2, new Point(midX, botY - d1));
        pointMap.put(Position.D3, new Point(midX, botY - 2*d1));
        // Inner corners
        pointMap.put(Position.C3, new Point(midX - d1, botY - 2*d1));
        pointMap.put(Position.E3, new Point(midX + d1, botY - 2*d1));
        pointMap.put(Position.E5, new Point(midX + d1, topY + 2*d1));
        pointMap.put(Position.C5, new Point(midX - d1, topY + 2*d1));
        // Middle corner
        pointMap.put(Position.B6, new Point(midX - 2*d1, topY + d1));
        pointMap.put(Position.F6, new Point(midX + 2*d1, topY + d1));
        pointMap.put(Position.B2, new Point(midX - 2*d1, botY - d1));
        pointMap.put(Position.F2, new Point(midX + 2*d1, botY - d1));
    }


}
