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

    private int lastRemaining = -1;

    public PlayerFragment() {
    }

    public void injectViewModel(MainViewModel vm) {
        mainViewModel = vm;
    }

    public void injectPlayer(Player player) {
        if (mainViewModel == null)
            throw new IllegalStateException("ViewModel must be injected first.");
        if (player.color == Color.Red) {
            handlePlayerRedLiveData();
            checkerView.paintRed();
        }
         else if (player.color == Color.Blue) {
            handlePlayerBlueLiveData();
            checkerView.paintBlue();
         }
        else throw new IllegalStateException("No player color defined.");
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

        // Get window dimension
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point windowSize = new Point();
        display.getSize(windowSize);
        final int checkerSize = (int) ((windowSize.x > windowSize.y? windowSize.y: windowSize.x)*checkerFactor);

        playerText = view.findViewById(R.id.playerName);
        pointsText = view.findViewById(R.id.points);
        remainingText = view.findViewById(R.id.remianing);
        FrameLayout frameLayout = view.findViewById(R.id.frameLayout);

        checkerView = new CheckerView(getContext(), 0, 0, checkerSize, Position.UNPLACED);
        checkerView.show();
        checkerView.draggable = false;
        frameLayout.addView(checkerView);
        setupDrag();    // Configure checker draggability
        return view;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupDrag() {
        checkerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (checkerView.draggable && event.getAction() == MotionEvent.ACTION_DOWN) {
                    ClipData data = ClipData.newPlainText("", "");
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(checkerView);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        checkerView.startDragAndDrop(data, shadowBuilder, null, 0);
                    } else {
                        checkerView.startDrag(data, shadowBuilder, null, 0);
                    }
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
                updatePlayer(player);
            }
        });
        // Handle remaining checkers to place event
        mainViewModel.remainingRedLiveData.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer remaining) {
                updateRemaining(remaining);
            }
        });
    }

    private void handlePlayerBlueLiveData() {
        mainViewModel.player1LiveData.observe(this, new Observer<Player>(){
            @Override
            public void onChanged(@Nullable Player player) {
                updatePlayer(player);
            }
        });
        // Handle remaining checkers to place event
        mainViewModel.remainingBlueLiveData.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer remaining) {
                updateRemaining(remaining);
            }
        });
    }

    private void updateRemaining(int remaining) {
        lastRemaining = remaining;
        remainingText.setText("" + remaining);
    }

    private void updatePlayer(Player player) {
        if (player == null)
            return;
        if (player.activeTurn) {
            playerText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            if (lastRemaining != 0)
                checkerView.draggable = true;
            if (player.isInRemoveState())
                checkerView.draggable = false;
        } else {
            playerText.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            checkerView.draggable = false;
        }
        playerText.setText(player.name);
        pointsText.setText("" + player.wins);
    }

}
