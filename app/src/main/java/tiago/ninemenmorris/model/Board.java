package tiago.ninemenmorris.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

public class Board {

    private List<Checker> unplacedBlues;
    private List<Checker> unplacedReds;

    private static Hashtable<String, List<Position>> adjacencyMap = null;
    private static final String HORIZONTAL = "H";
    private static final String VERTICAL = "V";

    public void map(Position srcsPosition, String angle, Position p1, Position p2) {
        List<Position> list = new ArrayList<>();
        list.add(p1);
        list.add(p2);
        adjacencyMap.put(srcsPosition + angle, list);
    }

    public List<Position> getAdjacent(Position srcsPosition, String angle) {
        return adjacencyMap.get(srcsPosition + angle);
    }

    public Board() {
        unplacedBlues = new ArrayList<>();
        unplacedReds = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            unplacedReds.add(new Checker(Color.RED));
            unplacedBlues.add(new Checker(Color.BLUE));
        }

        if (adjacencyMap == null) {
            adjacencyMap = new Hashtable<>();
            map(Position.A1, HORIZONTAL, Position.D1, Position.G1);
            map(Position.A1, VERTICAL, Position.A4, Position.A7);
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
