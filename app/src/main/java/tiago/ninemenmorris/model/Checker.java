package tiago.ninemenmorris.model;

import java.util.ArrayList;
import java.util.List;

public class Checker {

    protected Color color;
    protected boolean draggable;
    protected Position position;

    public Checker(Position position, Color color, boolean draggable) {
        this.position = position;
        this.color = color;
        this.draggable = draggable;
    }

    public Checker(Color color, boolean draggable) {
        this.color = color;
        this.draggable = draggable;
    }

    public Checker() {}

    public Color getColor() {
        return color;
    }

    public boolean isDraggable() {
        return draggable;
    }

    public Position getPosition() {
        return position;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setDraggable(boolean draggable) {
        this.draggable = draggable;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "{" + position + ',' + color + ',' + draggable  + '}';
    }
}
