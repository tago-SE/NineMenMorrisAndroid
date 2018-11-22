package tiago.ninemenmorris.model;

public class Board {

    public final int width;
    public final int height;
    public final int topX;
    public final int topY;
    public final int checkerHitBox;

    public Board(int topX, int topY, int w, int h) {
        this.width = w;
        this.height = h;
        this.topX = topX;
        this.topY = topY;
        checkerHitBox = w/8;
        System.out.println(toString());
    }

    public boolean drop(int x, int y) {
        return true;
    }

    @Override
    public String toString() {
        return "Board{" +
                "width=" + width +
                ", height=" + height +
                ", topX=" + topX +
                ", topY=" + topY +
                ", checkerHitBox=" + checkerHitBox +
                '}';
    }
}
