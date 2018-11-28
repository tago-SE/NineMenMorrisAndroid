package tiago.ninemenmorris.DB;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "checkerEntities")
public class CheckerEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String color;
    public boolean draggable;
    public String position;
    public int gameId;

    public CheckerEntity(String color, boolean draggable, String position, int gameId){
        this.color = color;
        this.draggable = draggable;
        this.position = position;
        this.gameId = gameId;
    }
}
