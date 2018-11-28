package tiago.ninemenmorris.DB;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "playerEntities")
public class PlayerEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public String color;
    public String state;
    public int playerTag; //tag = 1 for p1, 2 for p2
    public boolean activeTurn;
    public int gameId;

    public PlayerEntity(String name, String color, String state, int playerTag, boolean activeTurn, int gameId){

        this.name = name;
        this.color = color;
        this.state = state;
        this.playerTag = playerTag;
        this.activeTurn = activeTurn;
        this.gameId = gameId;
    }


}
