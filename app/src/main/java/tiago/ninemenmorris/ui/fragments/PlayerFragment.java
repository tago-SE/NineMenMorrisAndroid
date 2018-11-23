package tiago.ninemenmorris.ui.fragments;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.arch.lifecycle.Observer;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import  android.support.v4.app.Fragment;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import tiago.ninemenmorris.R;
import tiago.ninemenmorris.model.Color;
import tiago.ninemenmorris.model.Player;
import tiago.ninemenmorris.model.Position;
import tiago.ninemenmorris.ui.vm.MainViewModel;

public class PlayerFragment extends Fragment {

    private static final String TAG = "PlayerFrag";

    // size of checker relative to screen dimension
    private static final double checkerFactor = 1./8.;

    private MainViewModel mainViewModel;

    private CheckerView checkerView;
    private TextView playerText;
    private TextView pointsText;
    private TextView remainingText;




    public PlayerFragment() {
    }

    public void injectViewModel(MainViewModel vm) {
        mainViewModel = vm;
    }

    public void injectPlayer(Player player) {
        double x = checkerView.getX();
        double y = checkerView.getY();
        Log.w(TAG, "X: " + x);
        Log.w(TAG, "Y: " + y);

        /*CheckerView cv = new CheckerView(getContext(), (int) x, (int) y, 200, Position.UNPLACED);

        cv.paintBlue();
        cv.show();
        linearLayout.addView(cv);
        */
        if (mainViewModel == null)
            throw new IllegalStateException("ViewModel must be injected first.");
        if (player.color == Color.RED) {
            handlePlayerRedLiveData();
            checkerView.paintRed();
            //checkerView.setBackgroundResource(R.drawable.glossyred);
        }
         else if (player.color == Color.BLUE) {
            handlePlayerBlueLiveData();
            checkerView.paintBlue();
            //checkerView.setBackgroundResource(R.drawable.glossyblue);
         }
        else
            throw new IllegalStateException("No player color defined.");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_player, container, false);

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point windowSize = new Point();
        display.getSize(windowSize);
        final int checkerSize = (int) ((windowSize.x > windowSize.y? windowSize.y: windowSize.x)*checkerFactor);
        /*
        checkerView = view.findViewById(R.id.checker);
        checkerView.post(new Runnable() {
            @Override
            public void run() {
                Log.w(TAG, "resizing checker: " + checkerSize);
                checkerView.getLayoutParams().height = checkerSize;
                checkerView.getLayoutParams().width= checkerSize;
            }
        });
        setupDrag(checkerView);
        */
        LinearLayout linearLayout = view.findViewById(R.id.linearLayout);
        playerText = view.findViewById(R.id.playerName);
        pointsText = view.findViewById(R.id.points);
        remainingText = view.findViewById(R.id.remianing);
        FrameLayout frameLayout = view.findViewById(R.id.frameLayout);
        checkerView = new CheckerView(getContext(), 0, 0, 180, Position.UNPLACED);
        checkerView.show();
        checkerView.draggable = true;
        frameLayout.addView(checkerView);
        setupDrag();    // Configure checker draggability
        return view;
    }

      /* // fragment size ???
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screenH = displaymetrics.heightPixels;
        screenW = displaymetrics.widthPixels;
        */
        /*
        boardView.post(new Runnable() {
                @Override
                public void run() {
                    ViewGroup.LayoutParams boardParam = boardView.getLayoutParams();
                    int w = boardView.getWidth();
                    int h = boardView.getHeight();
                    Log.w(TAG, "W: " + w);
                    Log.w(TAG, "H: " + h);
                    setupBoard(boardView, w);
                    boardParam.height = w;
                    boardParam.width = w;
                    boardView.setLayoutParams(boardParam);
                    boardView.postInvalidate();
                }
            });
         */


    private void setupDrag() {
        checkerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (checkerView.draggable && event.getAction() == MotionEvent.ACTION_DOWN) {
                    ClipData data = ClipData.newPlainText("", "");
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(checkerView);
                    checkerView.startDragAndDrop(data, shadowBuilder, checkerView, 0);
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    private void handlePlayerRedLiveData() {
        mainViewModel.player0LiveData.observe(this, new Observer<Player>(){
            @Override
            public void onChanged(@Nullable Player player) {
                update(player);
            }
        });
        mainViewModel.remainingRedLiveData.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer remaining) {
                remainingText.setText("" + remaining);
            }
        });
    }

    private void handlePlayerBlueLiveData() {
        mainViewModel.player1LiveData.observe(this, new Observer<Player>(){
            @Override
            public void onChanged(@Nullable Player player) {
                update(player);
            }
        });
        mainViewModel.remainingBlueLiveData.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer remaining) {
                remainingText.setText("" + remaining);
            }
        });
    }

    private void update(Player player) {
        if (player.activeTurn) {
            playerText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        }
        playerText.setText(player.playerName);
        pointsText.setText("" + player.wins);
    }

}
