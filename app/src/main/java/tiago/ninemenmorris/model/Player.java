package tiago.ninemenmorris.model;

public class Player {

    public final String playerName;
    public int wins;
    public boolean activeTurn;

    public Player(String playerName) {
        this.playerName = playerName;
        wins = 0; // default
        activeTurn = false;
    }

    @Override
    public String toString() {
        return "Player{" +
                "playerName='" + playerName + '\'' +
                ", wins=" + wins +
                ", activeTurn=" + activeTurn +
                '}';
    }
}
