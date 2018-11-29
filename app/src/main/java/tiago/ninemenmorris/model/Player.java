package tiago.ninemenmorris.model;

public class Player {

    public String name;
    public Color color;
    public int wins;
    public boolean activeTurn;
    private State state;
    private int remaining;

    public enum State {
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

    public void setState(State state){this.state = state; }

    public boolean isInRemoveState() {
        return state == State.REMOVING;
    }

    public boolean isInPlaceState() {
        return state == State.PLACING;
    }

    public State getState(){ return state; }
    public String getStateString() { return state.toString(); }

    public int getRemaining() {
        return remaining;
    }

    public void setRemaining(int remaining) {
        this.remaining = remaining;
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
