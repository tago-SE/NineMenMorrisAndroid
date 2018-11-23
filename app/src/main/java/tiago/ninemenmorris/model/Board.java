package tiago.ninemenmorris.model;

import java.util.ArrayList;
import java.util.List;

public class Board {

    private List<Checker> unplacedBlues;
    private List<Checker> unplacedReds;

    public Board() {
        unplacedBlues = new ArrayList<>();
        unplacedReds = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            unplacedReds.add(new Checker(Color.RED));
            unplacedBlues.add(new Checker(Color.BLUE));
        }
    }

    public int getNumUnplacedReds() {
        return unplacedReds.size();
    }

    public int getNumUnplacedBlues() {
        return unplacedBlues.size();
    }

    public boolean drop(int x, int y) {
        return true;
    }


}
