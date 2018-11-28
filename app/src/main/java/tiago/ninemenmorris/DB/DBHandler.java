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
import tiago.ninemenmorris.DAO.PlayerDAO;
import tiago.ninemenmorris.model.Checker;
import tiago.ninemenmorris.model.Color;
import tiago.ninemenmorris.model.Game;
import tiago.ninemenmorris.model.GameMetaData;
import tiago.ninemenmorris.model.Player;

@Database(entities = {GameEntity.class, PlayerEntity.class, CheckerEntity.class}, version = 4, exportSchema = false)
public abstract class DBHandler extends RoomDatabase {

    private static final String TAG = "DBHandler";

    public abstract GameDAO gameDAO();
    public abstract PlayerDAO playerDAO();
    public abstract CheckerDAO checkerDAO();

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

    public void insertGame(Game game) {
        GameEntity ge = new GameEntity();
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
        Log.w(TAG, "insertGame()");
    }

    public List<GameMetaData> getAllGamesMetaData() {
        List<GameMetaData> list = new ArrayList<>();
        GameDAO dao = gameDAO();
        for (GameEntity ge : dao.getAllGames()) {
            list.add(new GameMetaData(ge.id, ge.player1, ge.player2, ge.timestamp));
        }
        return list;
    }

    //method for saving into the database
    public void save(Game game) {

        if(!game.isGameOver()){

            GameEntity gameEntity = new GameEntity(game.isGameOver());
            PlayerEntity player1Entity = new PlayerEntity(game.player1.name,
                    game.player1.color.toString(),
                    game.player1.getStateString(),
                    1,
                    game.player1.activeTurn,
                    gameEntity.id);

            PlayerEntity player2Entity = new PlayerEntity(game.player2.name,
                    game.player2.color.toString(),
                    game.player2.getStateString(),
                    2,
                    game.player2.activeTurn,
                    gameEntity.id);

            dbHandler.gameDAO().insertGame(gameEntity);
            dbHandler.playerDAO().insertPlayer(player1Entity);
            dbHandler.playerDAO().insertPlayer(player2Entity);
            List<Checker> checkerList = game.getCheckers();
            for (Checker c : checkerList){
                CheckerEntity checkerEntity = new CheckerEntity(c.getColor().toString(),
                        c.isDraggable(),
                        c.getPosition().toString(),
                        gameEntity.id);
                dbHandler.checkerDAO().insertChecker(checkerEntity);
            }
        }
    }

    public void load(Game game){
        List<GameEntity> theGame;
        List<PlayerEntity> thePlayers;
        List<CheckerEntity> theCheckers;

        try {
            theGame = dbHandler.gameDAO().getGameById(game.getId());
            game.setId(theGame.get(0).id);
            game.setGameOver(theGame.get(0).gameOver);

            thePlayers = dbHandler.playerDAO().getPlayersByGameId(game.getId());
            if (thePlayers.get(0).playerTag == 1 && thePlayers.get(1).playerTag == 2){

                game.player1.name = thePlayers.get(0).name;
                game.player2.name = thePlayers.get(1).name;

                game.player1.activeTurn = thePlayers.get(0).activeTurn;
                game.player2.activeTurn = thePlayers.get(1).activeTurn;
                if (game.player1.activeTurn){
                    game.setCurrentPlayer(game.player1);
                } else{
                    game.setCurrentPlayer(game.player2);
                }

                game.player1.color = Color.valueOf(thePlayers.get(0).color);
                game.player2.color = Color.valueOf(thePlayers.get(1).color);

                game.player1.setState(Player.State.valueOf(thePlayers.get(0).state));
                game.player2.setState(Player.State.valueOf(thePlayers.get(1).state));
            }

            theCheckers = dbHandler.checkerDAO().getcheckersByGameId(game.getId());
            //more work needs to be done here.

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //delete a game
    public void deleteGame(Game game) {
        if (game.isGameOver()){
            dbHandler.gameDAO().deleteGame(game.getId());
            dbHandler.playerDAO().deletePlayersByGameId(game.getId());
            dbHandler.checkerDAO().deleteCheckersByGameId(game.getId());
        }
    }
}
