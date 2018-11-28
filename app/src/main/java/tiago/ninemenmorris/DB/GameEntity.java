package tiago.ninemenmorris.DB;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "gameEntities")
public class GameEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;
    public int flyingCondition;
    public int loseCondition;
    public String timestamp;
    public String player1;
    public String playerState1;
    public String playerColor1;
    public int unplaced1;
    public String player2;
    public String playerState2;
    public String playerColor2;
    public int unplaced2;
    public int playerTurn;
    public boolean gameOver;

    public GameEntity(){

    }

    @Deprecated
    public GameEntity(boolean gameOver){
        this.gameOver = gameOver;
    }
}
