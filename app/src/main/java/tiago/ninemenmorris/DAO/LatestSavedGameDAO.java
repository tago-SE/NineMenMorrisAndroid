package tiago.ninemenmorris.DAO;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import tiago.ninemenmorris.DB.LatestSavedGameEntity;

@Dao
public interface LatestSavedGameDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertLatestSavedGame(LatestSavedGameEntity... latestSavedGameEntities);

    @Query("SELECT * FROM latestSavedGame WHERE gameId = :gameId")
    List<LatestSavedGameEntity> getLatestSavedGameById(int gameId);

    @Query("SELECT * FROM latestSavedGame")
    List<LatestSavedGameEntity> getSavedGame();

    @Query("DELETE FROM latestSavedGame")
    void deleteAllSavedGames();
}
