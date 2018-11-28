package tiago.ninemenmorris.DAO;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import tiago.ninemenmorris.DB.PlayerEntity;

@Dao
public interface PlayerDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPlayer(PlayerEntity... playerEntities);

    @Query("SELECT * FROM playerEntities")
    List<PlayerEntity> getAllPlayers();

    @Query("SELECT * FROM playerEntities WHERE gameId = :gameId")
    List<PlayerEntity> getPlayersByGameId(int gameId);

    @Query("DELETE FROM playerEntities WHERE gameId = :gameId")
    void deletePlayersByGameId(int gameId);

}
