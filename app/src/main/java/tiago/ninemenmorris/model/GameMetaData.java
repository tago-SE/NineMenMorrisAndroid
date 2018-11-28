package tiago.ninemenmorris.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GameMetaData {

    private int id;
    private String player1;
    private String player2;
    private String timestamp;

    @Deprecated // Temporary
    private static int counter = 0;

    @Deprecated
    public GameMetaData() {
        // Remove
        Date date = new Date();
        timestamp = date.toString();
        player1 = "Tiago";
        player2 = "Ahmed";
        id = ++counter;
    }

    public GameMetaData(int id, String player1, String player2, String timestamp) {
        this.id = id;
        this.player1 = player1;
        this.player2 = player2;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "GameMetaData{" +
                "id=" + id +
                ", player1='" + player1 + '\'' +
                ", player2='" + player2 + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
