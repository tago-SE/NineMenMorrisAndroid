package tiago.ninemenmorris.ui;

import android.arch.lifecycle.Observer;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Point;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

import tiago.ninemenmorris.R;
import tiago.ninemenmorris.model.Checker;
import tiago.ninemenmorris.model.Color;
import tiago.ninemenmorris.model.Position;
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

    private Hashtable<Position, Point> pointMap = null;
    private int checkerSize;

    private final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.w(TAG, "onCreate:");
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

        Log.e(TAG, "BW" + boardView.getWidth());
        Log.e(TAG, "BX" + boardView.getX());
        Log.e(TAG, "BY" + boardView.getY());

        Log.w(TAG, "(" + screenSize.x + "," + screenSize.y + ")");

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
                        for (CheckerView cv : checkerViewList) {
                            if (cv.rect.contains(x, y)) {
                                if (mainViewModel.isOccupied(cv.position))
                                    return false; // unsuccessful drop
                                // get stored source position inside clip data if any exists
                                String data = (String) event.getClipData().getItemAt(0).getText();
                                Position source = null;
                                Position target = cv.position;
                                if (!data.equals(""))
                                    source = Position.valueOf(data);
                                return mainViewModel.select(source, target);
                            }
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
                Log.e(TAG, "V: " + x + " " + y);
                CheckerView checkerView = getCheckerViewAtPosition(x, y);
                if (checkerView == null)
                    return false;
                mainViewModel.attemtRemove(checkerView.position);
                return true;
            }
        });
        /*
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int x = (int) (v.getX() + v.getX());
                int y = (int) (v.getY() + v.getY());

                Log.e(TAG, "V: " + x + " " + y);

               // mainViewModel.attemtRemove();
            }
        });
        */
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mainViewModel.refresh();
    }

    private void runOnStart() {
        mapCoordinates();
        handleObservedCheckers();
        mainViewModel.start(); // Should be moved to the "start button"
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
                    view.startDragAndDrop(data, shadowBuilder, null, 0);
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    private CheckerView getCheckerViewAtPosition(int x, int y) {
        for (CheckerView cv : checkerViewList) {
            if (cv.rect.contains(x, y))
                return cv;
        }
        return null;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            boardView.post(new Runnable() {
                @Override
                public void run() {
                    ViewGroup.LayoutParams boardParam = boardView.getLayoutParams();
                    boardParam.height = boardView.getWidth();
                    boardParam.width = boardView.getWidth();
                    boardView.setLayoutParams(boardParam);
                    //setupBoardNodes();
                    //mapCoordinates();
                    runOnStart();
                    boardView.postInvalidate();
                    //mainViewModel.refresh();
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
                    //setupBoardNodes();
                   //mapCoordinates();
                    runOnStart();
                    boardView.postInvalidate();
                    //mainViewModel.refresh();
                }
            });
        }
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
            if (color == Color.RED) {
                checkerView.paintRed();
                checkerView.show();
            } else if (color == Color.BLUE) {
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


    private void mapCoordinates() {
        if (pointMap != null)
            return;
        Log.w(TAG, "Mapping coordinates...");

        Log.e(TAG, "BW" + boardView.getWidth());
        Log.e(TAG, "BX" + boardView.getX());
        Log.e(TAG, "BY" + boardView.getY());

        // Setup
        checkerSize = (int) (boardView.getWidth()*circleFactor);
        int leftX = (int) boardView.getX() - checkerSize/2;
        int midX = leftX + boardView.getWidth()/2;
        int rightX = leftX + boardView.getWidth();
        int topY = (int) boardView.getY() - checkerSize/2;
        int midY =  topY + boardView.getWidth()/2;
        int botY = topY + boardView.getWidth();
        int d1 = (int) (boardView.getWidth()*0.17);
        // Map coordinates
        //pointMap = new Hashtable<>();
        // Top horizontal
        System.out.println("leftX " + leftX);
        pointMap = new Hashtable<>();
        Point p = new Point(leftX, topY);
        System.out.println(p);
        pointMap.put(Position.UNPLACED, p);
        System.out.println(pointMap.get(Position.UNPLACED));

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
