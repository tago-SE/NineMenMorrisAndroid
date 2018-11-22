package tiago.ninemenmorris.ui.fragments;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Fragment;
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

public class PlayerFragment extends Fragment {

    private static final String TAG = "PlayerFrag";

    // size of checker relative to screen dimension
    private static final double circleFactor = 1./8.;

    private ImageView checkerView;
    private TextView playerText;
    private TextView pointsText;
    private TextView remainingText;

    public PlayerFragment() {
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
        Point size = new Point();
        display.getSize(size);
        final int circleSize = (int) ((size.x > size.y? size.y: size.x)*circleFactor);
        checkerView = view.findViewById(R.id.checker);
        checkerView.post(new Runnable() {
            @Override
            public void run() {
                Log.w(TAG, "resizing checker: " + circleSize);
                checkerView.getLayoutParams().height = circleSize;
                checkerView.getLayoutParams().width= circleSize;
            }
        });

        setupDrag(checkerView);



        playerText = view.findViewById(R.id.playerName);

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

        return view;
    }

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
