package tiago.ninemenmorris.DB;

import tiago.ninemenmorris.model.Game;

public class DBHandler {
    private static final DBHandler ourInstance = new DBHandler();

    public static DBHandler getInstance() {
        return ourInstance;
    }

    private DBHandler() {
    }


    //create database
    //get instance of database

    //method for saving into the database
    //method for loading into the database
    public void save(Game game) {

    }

    public void delete(Game game) {
        // game.id;
    }



    // build method
}
