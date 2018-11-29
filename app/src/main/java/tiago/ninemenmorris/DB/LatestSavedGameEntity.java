package tiago.ninemenmorris.DB;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "latestSavedGame")
public class LatestSavedGameEntity {

    @PrimaryKey
    public int id;

    public int gameId;

    public LatestSavedGameEntity(int gameId){ this.gameId = gameId; }
}
