package tiago.ninemenmorris.DB;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "gameEntities")
public class GameEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    //might not be used
    //boolean latestGame;

    public boolean gameOver;

    public GameEntity(boolean gameOver){
        this.gameOver = gameOver;
    }
}
