package tiago.ninemenmorris.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

public class Board {

    private static final String TAG = "Board";

    private List<Checker> unplacedBlues = new ArrayList<>();
    private List<Checker> unplacedReds = new ArrayList<>();
    private Hashtable<Position, Checker> placedCheckers = new Hashtable<>();
    private static Hashtable<String, List<Position>> adjacencyMap = null;
    private static final int NUM_CHECKERS = 1;

    public static final String HORIZONTAL   = "H";
    public static final String VERTICAL     = "V";

    public Board() {
        for (int i = 0; i < NUM_CHECKERS; i++) {
            unplacedReds.add(new Checker(Color.RED, false));
            unplacedBlues.add(new Checker(Color.BLUE, false));
        }
        if (adjacencyMap == null) {
            adjacencyMap = new Hashtable<>();
            // Horizontal mappings
            map(Position.A1, HORIZONTAL, Position.D1, Position.G1);
            map(Position.B2, HORIZONTAL, Position.D2, Position.F2);
            map(Position.C3, HORIZONTAL, Position.D3, Position.E3);
            map(Position.A4, HORIZONTAL, Position.B4, Position.C4);
            map(Position.E4, HORIZONTAL, Position.F4, Position.G4);
            map(Position.C5, HORIZONTAL, Position.D5, Position.E5);
            map(Position.B6, HORIZONTAL, Position.D6, Position.F6);
            map(Position.A7, HORIZONTAL, Position.D7, Position.G7);
            // Vertical mappings
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

    public boolean isOccupied(Position position) {
        return placedCheckers.containsKey(position);
    }

    public Checker placeChecker(Position position, Color color) {
        if (isOccupied(position))
            return null;
        Checker checker;
        if (color == Color.RED && unplacedReds.size() > 0)
            checker = unplacedReds.remove(0);
        else if (color == Color.BLUE && unplacedBlues.size() > 0)
            checker = unplacedBlues.remove(0);
        else
            return null;
        placedCheckers.put(position, checker);
        checker.position = position;
        // Debug
        Log.w(TAG, "placed checker {" + color.toString() + "@" + position.toString() + "}");
        return checker;
    }

    public Checker moveChecker(Position source, Position destination) {
        Checker checker = placedCheckers.get(source);
        if (checker == null)
            return checker;
        if (isOccupied(destination))
            return null;
        placedCheckers.remove(source);
        placedCheckers.put(destination, checker);
        checker.position = destination;
        return checker;
    }

    public Collection<Checker> getPlacedCheckers() {
        return placedCheckers.values();
    }

    public void remove(Position position) {
        placedCheckers.remove(position);
    }

    public Checker get(Position position) {
        return placedCheckers.get(position);
    }

    public List<Position> getAdjacent(Position srcsPosition, String angle) {
        return adjacencyMap.get(srcsPosition + angle);
    }

    public int getNumUnplacedReds() {
        return unplacedReds.size();
    }

    public int getNumUnplacedBlues() {
        return unplacedBlues.size();
    }

    private void map(Position p1, String angle, Position p2, Position p3) {
        List<Position> l1 = new ArrayList<>();
        l1.add(p2);
        l1.add(p3);
        adjacencyMap.put(p1 + angle, l1);

        List<Position> l2 = new ArrayList<>();
        l2.add(p1);
        l2.add(p3);
        adjacencyMap.put(p2 + angle, l2);

        List<Position> l3 = new ArrayList<>();
        l3.add(p1);
        l3.add(p2);
        adjacencyMap.put(p3 + angle, l3);
    }
}
