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
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import tiago.ninemenmorris.R;
import tiago.ninemenmorris.model.Player;
import tiago.ninemenmorris.ui.vm.MainViewModel;

public class PlayerFragment extends Fragment {

    private static final String TAG = "PlayerFrag";

    // size of checker relative to screen dimension
    private static final double checkerFactor = 1./8.;

    private MainViewModel mainViewModel;

    private ImageView checkerView;
    private TextView playerText;
    private TextView pointsText;
    private TextView remainingText;

    public PlayerFragment() {
    }

    public void injectViewModel(MainViewModel vm, int playerIndex) {
        mainViewModel = vm;
        if (playerIndex == 0) {
            vm.player0LiveData.observe(this, new Observer<Player>(){
                @Override
                public void onChanged(@Nullable Player player) {
                    update(player);
                }
            });
            vm.remainingRedLiveData.observe(this, new Observer<Integer>() {
                @Override
                public void onChanged(@Nullable Integer remaining) {
                    remainingText.setText("" + remaining);
                }
            });
        } else if (playerIndex == 1) {
            vm.player1LiveData.observe(this, new Observer<Player>(){
                @Override
                public void onChanged(@Nullable Player player) {
                    update(player);
                }
            });
            vm.remainingBlueLiveData.observe(this, new Observer<Integer>() {
                @Override
                public void onChanged(@Nullable Integer remaining) {
                    remainingText.setText("" + remaining);
                }
            });
        } else
            throw new IndexOutOfBoundsException("Invalid player index");
    }

    private void update(Player player) {
        if (player.activeTurn) {
            playerText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        }
        playerText.setText(player.playerName);
        pointsText.setText("" + player.wins);
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
        playerText = view.findViewById(R.id.playerName);
        pointsText = view.findViewById(R.id.points);
        remainingText = view.findViewById(R.id.remianing);
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


    private void setupDrag(final View view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ClipData data = ClipData.newPlainText("", "");
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                    view.startDragAndDrop(data, shadowBuilder, view, 0);
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

}
