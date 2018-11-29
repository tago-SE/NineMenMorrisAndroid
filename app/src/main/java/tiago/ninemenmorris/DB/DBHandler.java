package tiago.ninemenmorris.DB;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import tiago.ninemenmorris.DAO.CheckerDAO;
import tiago.ninemenmorris.DAO.GameDAO;
import tiago.ninemenmorris.DAO.LatestSavedGameDAO;
import tiago.ninemenmorris.DAO.PlayerDAO;
import tiago.ninemenmorris.model.Checker;
import tiago.ninemenmorris.model.Color;
import tiago.ninemenmorris.model.Game;
import tiago.ninemenmorris.model.GameMetaData;
import tiago.ninemenmorris.model.Player;
import tiago.ninemenmorris.model.Position;

@Database(entities = {GameEntity.class, PlayerEntity.class, CheckerEntity.class, LatestSavedGameEntity.class}, version = 6, exportSchema = false)
public abstract class DBHandler extends RoomDatabase {

    private static final String TAG = "DBHandler";

    public abstract GameDAO gameDAO();
    public abstract PlayerDAO playerDAO();
    public abstract CheckerDAO checkerDAO();
    public abstract LatestSavedGameDAO latestSavedGameDAO();

    private static volatile DBHandler dbHandler;

    /**
     * Factory method
     * @param context
     * @return
     */
    public static DBHandler buildInstance (final Context context){
        if (dbHandler == null){
            synchronized (DBHandler.class){
                if (dbHandler == null){
                    dbHandler = Room.databaseBuilder(context.getApplicationContext(), DBHandler.class, "ninemenmorris_db").fallbackToDestructiveMigration().build();
                }
            }
        }
        return dbHandler;
    }

    /**
     * Access method
     * @return
     */
    public static DBHandler getInstance() { return dbHandler; }

    /**
     * Flushes the database of all records.
     */
    public void flush() {
        gameDAO().flush();
        playerDAO().flush();
        checkerDAO().flush();
    }

    private void insertCheckers(List<Checker> checkers, int gameId) {
        CheckerDAO dao = checkerDAO();
        for (Checker c : checkers) {
            CheckerEntity ce = new CheckerEntity(
                    c.getColor().toString(),
                    c.isDraggable(),
                    c.getPosition().toString(),
                    gameId
            );
            dao.insertChecker(ce);
        }
    }

    private List<Checker> getCheckers(int id) {
        CheckerDAO dao = checkerDAO();
        List<Checker> checkers = new ArrayList<>();
        for (CheckerEntity ce : dao.getcheckersByGameId(id)) {
            Checker checker = new Checker();
            checker.setDraggable(ce.draggable);
            checker.setColor(Color.valueOf(ce.color));
            checker.setPosition(Position.valueOf(ce.position));
            checkers.add(checker);
        }
        return checkers;
    }

    /**
     * Saves the current game session to the database.
     * @return true on success
     */
    public boolean insertGame() {
        Game game = Game.getInstance();
        GameEntity ge = new GameEntity();
        ge.id = game.getId();
        ge.gameOver = game.isGameOver();
        ge.flyingCondition = game.getFlyingCondition();
        ge.loseCondition = game.getLoseCondition();
        ge.timestamp = (new Date()).toString();
        ge.player1 = game.player1.name;
        ge.playerColor1 = game.player1.color.toString();
        ge.playerState1 = game.player1.getState().toString();
        ge.unplaced1 = game.getUnplacedRed();
        ge.player2 = game.player2.name;
        ge.playerColor2 = game.player2.color.toString();
        ge.playerState2 = game.player2.getState().toString();
        ge.unplaced2 = game.getUnplacedBlue();
        if (game.getCurrentPlayer().equals(game.player1))
            ge.playerTurn = 1;
        else
            ge.playerTurn = 2;
        int id = (int) gameDAO().insertGame(ge);
        game.setId(id);
        insertCheckers(game.getCheckers(), id);
        saveId(game.getId());
        return true;
    }

    /**
     * Loads a previous unfinished game sessions from the database.
     * @param id
     * @return true if found and unfinished
     */
    public boolean loadGame(int id) {
        Game game = Game.getInstance();
        GameDAO dao = gameDAO();
        GameEntity ge = dao.getGameById(id);
        if (ge == null)
            return false;
        if (ge.gameOver)
            return false;
        List<Checker> checkers = getCheckers(id);
        if (checkers.size() <= 0)
            return false;
        game.setId(id);
        game.setCheckers(checkers);
        game.setGameOver(false);
        game.setFlyingCondition(ge.flyingCondition);
        game.setLoseCondition(ge.loseCondition);
        // Player red
        game.player1.name = ge.player1;
        game.player1.color = Color.valueOf(ge.playerColor1);
        game.player1.setState(Player.State.valueOf(ge.playerState1));
        game.setUnplacedRed(ge.unplaced1);
        // Player blue
        game.player2.name = ge.player2;
        game.player2.color = Color.valueOf(ge.playerColor2);
        game.player2.setState(Player.State.valueOf(ge.playerState2));
        game.setUnplacedBlue(ge.unplaced2);
        if (ge.playerTurn == 1) {
            game.setCurrentPlayer(game.player1);
        } else {
            game.setCurrentPlayer(game.player2);
        }
        saveId(game.getId());
        return true;
    }

    public void saveId(int id) {
        latestSavedGameDAO().deleteAllSavedGames();
        latestSavedGameDAO().insertLatestSavedGame(new LatestSavedGameEntity(id));
    }

    /**
     * Loads the last saved session if it's unfinished
     */
    public boolean loadLastSavedGameState() {
        List<LatestSavedGameEntity> latestSavedGame = latestSavedGameDAO().getSavedGame();
        if (latestSavedGame == null || latestSavedGame.size() != 1){
            return false;
        }
        //Game game = Game.getInstance();
        int id = latestSavedGame.get(0).gameId;
       return loadGame(id);
    }

    /**
     * Fetches a list containing meta-data of all game sessions that have yet finished
     * @return List containing id and other information about unfinished games.
     */
    public List<GameMetaData> getAllGamesMetaData() {
        List<GameMetaData> list = new ArrayList<>();
        GameDAO dao = gameDAO();
        for (GameEntity ge : dao.getAllGames()) {
            if (!ge.gameOver)
                list.add(new GameMetaData(ge.id, ge.player1, ge.player2, ge.timestamp));
        }
        return list;
    }

    //delete a game
    public void deleteGame() {
        Game game = Game.getInstance();
        if (game.isGameOver()){
            dbHandler.gameDAO().deleteGame(game.getId());
            dbHandler.playerDAO().deletePlayersByGameId(game.getId());
            dbHandler.checkerDAO().deleteCheckersByGameId(game.getId());
        }
    }
}
