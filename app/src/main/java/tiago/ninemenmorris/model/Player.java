package tiago.ninemenmorris.model;

public class Player {

    public final String playerName;
    public final Color color;
    public int wins;
    public boolean activeTurn;
    private State state;

    private static enum State {
        PLACING, REMOVING;
    }

    public Player(String playerName, Color color) {
        this.playerName = playerName;
        this.color = color;
        wins = 0; // default
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
                "playerName='" + playerName + '\'' +
                ", color=" + color +
                ", wins=" + wins +
                ", activeTurn=" + activeTurn +
                '}';
    }
}
