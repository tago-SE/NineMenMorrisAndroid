package tiago.ninemenmorris.ui.fragments;

import android.arch.lifecycle.ViewModel;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;

import tiago.ninemenmorris.R;
import tiago.ninemenmorris.ui.vm.MainViewModel;

@Deprecated
public class BoardFragment extends Fragment {

    private static final float circleScale = (float) 1/15;
    private static final double diagonal = 1.8;    // Distance diagonal nodes;
    private static final double diagonal2 = 1.9;     // factor to get second diagonal node


    private static final String TAG = "BoardFragment";

    private MainViewModel mainViewModel;
    private ConstraintLayout layout;
    private ImageView boardView;

    private ImageView lastAddedView;
    private static int addedViewCounter;

    private static final double sin45 = 0.7071067811865475;
    private static final double cos45 = 0.7071067811865476;
    private static final double sin135 = 0.7071067811865476;
    private static final double cos135 = -0.7071067811865475;
    private static final double sin225 = -0.7071067811865475;
    private static final double cos225 = -0.7071067811865477;
    private static final double sin315 = -0.7071067811865477;
    private static final double cos315 = 0.7071067811865474;


    public BoardFragment() {
        // Required empty public constructor
    }

    public void injectMainViewModel(ViewModel viewMode) {
        this.mainViewModel = (MainViewModel) viewMode;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_board, container, false);
        final int orientation = getResources().getConfiguration().orientation;
        Log.w(TAG, "onCreateView (" + orientation + ")");
        boardView = view.findViewById(R.id.boardView);

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            boardView.post(new Runnable() {
                @Override
                public void run() {
                    int w = boardView.getWidth();
                    int h = boardView.getHeight();
                    Log.w(TAG, "W: " + w);
                    Log.w(TAG, "H: " + h);
                    setupBoard(boardView, w);
                }
            });
        } else {
            boardView.post(new Runnable() {
                @Override
                public void run() {
                    int w = boardView.getWidth();
                    int h = boardView.getHeight();
                    Log.w(TAG, "W: " + w);
                    Log.w(TAG, "H: " + h);
                    setupBoard(boardView, h);
                }
            });
        }



        /*
        // Find and paint the board
        boardView = view.findViewById(R.id.boardSurface);
        boardView.setBackgroundResource(R.drawable.nmboard);
        // Find constraint

        layout = view.findViewById(R.id.boardLayout);
        // Setup dimension depending on screen orientation

        */
        /*
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            boardView.post(new Runnable() {
                @Override
                public void run() {
                    setupBoard(boardView, boardView.getWidth());
                }
            });
        } else {
            boardView.post(new Runnable() {
                @Override
                public void run() {
                    setupBoard(boardView, boardView.getHeight());
                }
            });
        }
        */
        return view;
    }

    private void setupBoard(View boardView, int boardSize) {
        ViewGroup.LayoutParams boardParam = boardView.getLayoutParams();
        boardParam.height = boardSize;
        boardParam.width = boardSize;
        boardView.setLayoutParams(boardParam);
        boardView.postInvalidate();
        return;
        /*
        int size = (int) (boardSize*circleScale);

        int leftX = -size/6;
        int midX = boardSize/2 - size/2;
        int rightX = boardSize - size*5/6;
        int topY = leftX;
        int midY = boardSize/2 - size/2;
        int botY = boardSize - size*5/6;

        // Distance between levels
        int d = (int) (boardSize*circleScale*1.16);

        // Debug
        addedViewCounter = 0;

        // Outer
        // left column
        layout.addView(createViewHolder(leftX, topY, size));
        layout.addView(createViewHolder(leftX, midY, size));
        layout.addView(createViewHolder(leftX, botY, size));
        // Outer middle
        layout.addView(createViewHolder(midX, topY, size));
        layout.addView(createViewHolder(midX, botY, size));
        // Right column
        layout.addView(createViewHolder(rightX, topY, size));
        layout.addView(createViewHolder(rightX, midY, size));
        layout.addView(createViewHolder(rightX, botY, size));

        layout.addView(createViewHolder(midX, topY + d, size));

        /*
        //Diagonals
        // Center top left edges
        layout.addView(createViewHolder(leftX + (int) (sin45*size*diagonal),
                topY + (int) (cos45*size*diagonal), size));
        int topLeft = (int) lastAddedView.getX();
        layout.addView(createViewHolder(leftX + (int) (sin45*size*diagonal*diagonal2),
                topY + (int) (cos45*size*diagonal*diagonal2), size));
        int topLeft2 = (int) lastAddedView.getX();
        // Center bot left edges
        layout.addView(createViewHolder(leftX - (int) (sin315*size*diagonal),
                botY - (int) (cos315*size*diagonal), size));
        layout.addView(createViewHolder(leftX - (int) (sin315*size*diagonal*diagonal2),
                botY - (int) (cos315*size*diagonal*diagonal2), size));
        // Center top right edges
        layout.addView(createViewHolder(rightX - (int) (cos315*size*diagonal),
                topY - (int) (sin315*size*diagonal), size));
        layout.addView(createViewHolder(rightX - (int) (cos315*size*diagonal*diagonal2),
                topY - (int) (sin315*size*diagonal*diagonal2), size));
        // Center bot right edges
        layout.addView(createViewHolder(rightX - (int) (sin45*size*diagonal),
                botY - (int) (cos45*size*diagonal), size));
        int botRight = (int) lastAddedView.getX();
        layout.addView(createViewHolder(rightX - (int) (sin45*size*diagonal*diagonal2),
                botY - (int) (cos45*size*diagonal*diagonal2), size));
        int botRight2 = (int) lastAddedView.getX();
        //Inner
        layout.addView(createViewHolder(midX, topLeft, size));
        layout.addView(createViewHolder(topLeft, midY, size));
        layout.addView(createViewHolder(midX, topLeft2, size));
        layout.addView(createViewHolder(topLeft2, midY, size));
        layout.addView(createViewHolder(midX, botRight, size));
        layout.addView(createViewHolder(midX, botRight2, size));
        layout.addView(createViewHolder(botRight, midY, size));
        layout.addView(createViewHolder(botRight2, midY, size));
        */
    }

    private View createViewHolder(int x, int y, int size) {
        ImageView view = new ImageView(getActivity());
        view.setBackgroundResource(R.drawable.glossyblue);

        view.setPadding(0, 0, 0, 0);
        //ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(44, 44);
        //btn.setLayoutParams(params);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(params);
        view.getLayoutParams().height = size;
        view.getLayoutParams().width = size;
        view.setX(x);
        view.setY(y);
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
                        Log.w(TAG, "Dropped inside button");
                        //mainViewModel.drop((int) event.getX(), (int) event.getY());
                        break;
                    case DragEvent.ACTION_DRAG_ENDED:
                        break;
                    default:
                }
                return false;
            }
        });
        view.setId(0);
        lastAddedView = view;
        addedViewCounter++;
        Log.w(TAG, ":createdCheckerPos " + addedViewCounter);
        return view;
    }
}
