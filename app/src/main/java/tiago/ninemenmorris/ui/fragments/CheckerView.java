package tiago.ninemenmorris.ui.fragments;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import tiago.ninemenmorris.R;
import tiago.ninemenmorris.model.Position;

@SuppressLint("AppCompatCustomView")
public class CheckerView extends ImageView {

    public boolean draggable = false;
    public final Position position;
    public final int x;
    public final int y;
    public final int size;
    public final Rect rect;

    public CheckerView(Context context, int x, int y, int size, Position position) {
        super(context);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setLayoutParams(params);
        getLayoutParams().height = size;
        getLayoutParams().width = size;
        setX(x);
        setY(y);
        this.position = position;
        this.x = x;
        this.y = y;
        this.size = size;
        this.rect = new Rect(x, y, x + size, y + size);
    }

    public void paintRed() {
        setBackgroundResource(R.drawable.glossyred);
}

    public void paintBlue() {
        setBackgroundResource(R.drawable.glossyblue);
    }

    public void hide() {
        setVisibility(INVISIBLE);
    }

    public void show() {
        setVisibility(VISIBLE);
    }

    public boolean isShown() {
        return getVisibility() == INVISIBLE;
    }

    public boolean isHidden() {
        return getVisibility() == INVISIBLE;
    }

    @Override
    public String toString() {
        return "CheckerView{" +
                "draggable=" + draggable +
                ", position=" + position +
                ", x=" + x +
                ", y=" + y +
                ", size=" + size +
                '}';
    }
}
