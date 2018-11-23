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

            //Horisonatl mappings
            map(Position.A1, HORIZONTAL, Position.D1, Position.G1);
            map(Position.B2, HORIZONTAL, Position.D2, Position.F2);
            map(Position.C3, HORIZONTAL, Position.D3, Position.E3);
            map(Position.A4, HORIZONTAL, Position.B4, Position.C4);
            map(Position.E4, HORIZONTAL, Position.F4, Position.G4);
            map(Position.C5, HORIZONTAL, Position.D5, Position.E5);
            map(Position.B6, HORIZONTAL, Position.D6, Position.F6);
            map(Position.A7, HORIZONTAL, Position.D7, Position.G7);

            //Vertical mappings
            map(Position.A1, VERTICAL, Position.A4, Position.A7);
            map(Position.B2, VERTICAL, Position.B4, Position.B6);
            map(Position.C3, VERTICAL, Position.C4, Position.C5);
            map(Position.D1, VERTICAL, Position.D2, Position.D3);
            map(Position.D5, VERTICAL, Position.D6, Position.D7);
            map(Position.E3, VERTICAL, Position.E4, Position.E5);
            map(Position.F2, VERTICAL, Position.F4, Position.F6);
            map(Position.G1, VERTICAL, Position.G4, Position.G7);

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
