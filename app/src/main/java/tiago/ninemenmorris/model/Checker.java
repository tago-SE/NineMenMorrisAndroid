package tiago.ninemenmorris.model;

import java.util.ArrayList;
import java.util.List;

public class Checker {

    protected final Color color;
    protected boolean draggable;
    protected Position position;

    public Checker(Color color) {
        this.color = color;
        draggable = false;
    }

    public Color getColor() {
        return color;
    }

    public boolean isDraggable() {
        return draggable;
    }

    public Position getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return "Checker{" +
                "color=" + color +
                ", draggable=" + draggable +
                ", position=" + position +
                '}';
    }
}
