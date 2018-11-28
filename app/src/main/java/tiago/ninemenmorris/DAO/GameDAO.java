package tiago.ninemenmorris.DAO;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import tiago.ninemenmorris.DB.GameEntity;

@Dao
public interface GameDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertGame(GameEntity entity);

    @Query("SELECT * FROM gameEntities")
    List<GameEntity> getAllGames();

    @Query("SELECT * FROM gameEntities WHERE id = :gameId")
    List<GameEntity> getGameById(int gameId);

    @Query("DELETE FROM gameEntities WHERE id = :gameId")
    void deleteGame (int gameId);
}
