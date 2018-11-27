package tiago.ninemenmorris.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

public class Board {

    private final List<Checker> placedCheckers = new ArrayList<>();
    private final Hashtable<Position, Checker> checkerPosMap = new Hashtable<>();
    private static Hashtable<String, List<Position>> adjacencyMap = null;
    public static final String HORIZONTAL   = "H";
    public static final String VERTICAL     = "V";


    public Board() {
        for (Position p : Position.values()) {
            if (p != Position.UNPLACED) {
                Checker c = new Checker(p, Color.Invis, false);
                checkerPosMap.put(p, c);
            }
        }

        if (adjacencyMap == null) {
            adjacencyMap = new Hashtable<>();
            // Horizontal mappings
            mapAdjacency(Position.A1, HORIZONTAL, Position.D1, Position.G1);
            mapAdjacency(Position.B2, HORIZONTAL, Position.D2, Position.F2);
            mapAdjacency(Position.C3, HORIZONTAL, Position.D3, Position.E3);
            mapAdjacency(Position.A4, HORIZONTAL, Position.B4, Position.C4);
            mapAdjacency(Position.E4, HORIZONTAL, Position.F4, Position.G4);
            mapAdjacency(Position.C5, HORIZONTAL, Position.D5, Position.E5);
            mapAdjacency(Position.B6, HORIZONTAL, Position.D6, Position.F6);
            mapAdjacency(Position.A7, HORIZONTAL, Position.D7, Position.G7);
            // Vertical mappings
            mapAdjacency(Position.A1, VERTICAL, Position.A4, Position.A7);
            mapAdjacency(Position.B2, VERTICAL, Position.B4, Position.B6);
            mapAdjacency(Position.C3, VERTICAL, Position.C4, Position.C5);
            mapAdjacency(Position.D1, VERTICAL, Position.D2, Position.D3);
            mapAdjacency(Position.D5, VERTICAL, Position.D6, Position.D7);
            mapAdjacency(Position.E3, VERTICAL, Position.E4, Position.E5);
            mapAdjacency(Position.F2, VERTICAL, Position.F4, Position.F6);
            mapAdjacency(Position.G1, VERTICAL, Position.G4, Position.G7);
        }
    }

    public boolean isOccupied(Position p) {
        return checkerPosMap.get(p).color != Color.Invis;
    }

    public List<Checker> checkers() {
        return new ArrayList(checkerPosMap.values());
    }

    public Collection<Checker> getPlacedCheckers() {
        return placedCheckers;
    }

    private List<Position> getAdjacent(Position srcsPosition, String angle) {
        return adjacencyMap.get(srcsPosition + angle);
    }

    public Checker placeChecker(Position p, Color c) {
        Checker checker = checkerPosMap.get(p);
        if (checker.color != Color.Invis && c != Color.Invis)
            return null;
        checker.color = c;
        placedCheckers.add(checker);
        return checker;
    }

    public Checker moveChecker(Position source, Position destination, boolean flying) {
        Checker destChecker = checkerPosMap.get(destination);
        Checker sourceChecker = checkerPosMap.get(source);
        // Check for adjacent blabla ignore if flying
        if ((sourceChecker.color == Color.Invis && destChecker.color != Color.Invis) ||
                (!flying && !getAdjacent(source, HORIZONTAL).contains(destination) && !getAdjacent(source, VERTICAL).contains(destination)))
            return null;
        destChecker.color = sourceChecker.color;
        sourceChecker.color = Color.Invis;
        placedCheckers.remove(sourceChecker);
        placedCheckers.add(destChecker);
        return destChecker;
    }

    public int countPlacedCheckersByColor(Color color) {
        int counter = 0;
        for (Checker c : placedCheckers) {
            if (c.color == color)
                counter++;
        }
        return counter;
    }

    public Checker removeChecker(Position position, Color color) {
        Checker checker = checkerPosMap.get(position);
        if (checker.color == Color.Invis || checker.color == color)
            return null;
        placedCheckers.remove(checker);
        checker.color = Color.Invis;
        return checker;
    }

    public boolean allAdjacentMatchingColor(Position p, Color c, String angle) {
        List<Position> adjacent = getAdjacent(p, angle);
        Checker c1 = checkerPosMap.get(p);
        Checker c2 = checkerPosMap.get(adjacent.get(0));
        Checker c3 = checkerPosMap.get(adjacent.get(1));
        return c1.color == c2.color && c2.color == c3.color;
    }

    @Override
    public String toString() {
        return "Board2{" +
                "checkers=" + checkerPosMap.values() +
                '}';
    }

    private static void mapAdjacency(Position p1, String angle, Position p2, Position p3) {
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
