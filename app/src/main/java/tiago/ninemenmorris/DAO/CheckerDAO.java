package tiago.ninemenmorris.DAO;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import tiago.ninemenmorris.DB.CheckerEntity;

@Dao
public interface CheckerDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertChecker(CheckerEntity... checkerEntities);

    @Query("SELECT * FROM checkerEntities")
    List<CheckerEntity> getAllCheckers();

    @Query("SELECT * FROM checkerEntities WHERE gameId = :gameId")
    List<CheckerEntity> getcheckersByGameId(int gameId);

    @Query("DELETE FROM checkerEntities WHERE gameId = :gameId")
    void deleteCheckersByGameId(int gameId);
}
