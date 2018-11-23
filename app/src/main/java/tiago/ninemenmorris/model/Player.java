package tiago.ninemenmorris.model;

public class Player {

    public final String playerName;
    public final Color color;
    public int wins;
    public boolean activeTurn;

    public Player(String playerName, Color color) {
        this.playerName = playerName;
        this.color = color;
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
