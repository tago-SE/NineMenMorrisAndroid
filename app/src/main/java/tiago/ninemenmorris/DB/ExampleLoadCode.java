package tiago.ninemenmorris.DB;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import tiago.ninemenmorris.model.GameMetaData;

public class ExampleLoadCode {

    public Collection<GameMetaData> getGames() {
        List<GameMetaData> games = new ArrayList<>();
        games.add(new GameMetaData());
        games.add(new GameMetaData());
        games.add(new GameMetaData());
        return games;
    }
}
