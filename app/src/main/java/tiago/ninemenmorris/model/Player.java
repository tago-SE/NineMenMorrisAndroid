package tiago.ninemenmorris.model;

public class Player {

    public String name;
    public Color color;
    public int wins;
    public boolean activeTurn;
    private State state;

    private enum State {
        PLACING, REMOVING;
    }

    public Player(String playerName, Color color) {
        this.name = playerName;
        this.color = color;
        wins = 0;
        activeTurn = false;
        state = State.PLACING;
}

    public void setStatePlacing() {
        state = State.PLACING;
    }

    public void setStateRemoving() {
        state = State.REMOVING;
    }

    public boolean isInRemoveState() {
        return state == State.REMOVING;
    }

    public boolean isInPlaceState() {
        return state == State.PLACING;
    }

    @Override
    public String toString() {
        return "Player{" +
                "playerName='" + name + '\'' +
                ", color=" + color +
                ", wins=" + wins +
                ", activeTurn=" + activeTurn +
                '}';
    }
}
