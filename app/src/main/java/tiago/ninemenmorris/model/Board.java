package tiago.ninemenmorris.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

public class Board {

    private final List<Checker> placedCheckers = new ArrayList<>();
    private final Hashtable<Position, Checker> checkerPosMap = new Hashtable<>();
    private static Hashtable<String, List<Position>> millMap = null;
    private static Hashtable<Position, List<Position>> adjacencyMap = null;
    public static final String HORIZONTAL   = "H";
    public static final String VERTICAL     = "V";


    public Board() {
        for (Position p : Position.values()) {
            if (p != Position.UNPLACED) {
                Checker c = new Checker(p, Color.Invis, false);
                checkerPosMap.put(p, c);
            }
        }

        // Maps vertical and horizontal mills
        if (millMap == null) {
            millMap = new Hashtable<>();
            // Horizontal mappings
            mapMills(Position.A1, HORIZONTAL, Position.D1, Position.G1);
            mapMills(Position.B2, HORIZONTAL, Position.D2, Position.F2);
            mapMills(Position.C3, HORIZONTAL, Position.D3, Position.E3);
            mapMills(Position.A4, HORIZONTAL, Position.B4, Position.C4);
            mapMills(Position.E4, HORIZONTAL, Position.F4, Position.G4);
            mapMills(Position.C5, HORIZONTAL, Position.D5, Position.E5);
            mapMills(Position.B6, HORIZONTAL, Position.D6, Position.F6);
            mapMills(Position.A7, HORIZONTAL, Position.D7, Position.G7);
            // Vertical mappings
            mapMills(Position.A1, VERTICAL, Position.A4, Position.A7);
            mapMills(Position.B2, VERTICAL, Position.B4, Position.B6);
            mapMills(Position.C3, VERTICAL, Position.C4, Position.C5);
            mapMills(Position.D1, VERTICAL, Position.D2, Position.D3);
            mapMills(Position.D5, VERTICAL, Position.D6, Position.D7);
            mapMills(Position.E3, VERTICAL, Position.E4, Position.E5);
            mapMills(Position.F2, VERTICAL, Position.F4, Position.F6);
            mapMills(Position.G1, VERTICAL, Position.G4, Position.G7);
        }
        // Maps node adjacency
        if (adjacencyMap == null) {
            adjacencyMap = new Hashtable<>();
            mapAdjacency(Position.A1, Position.A4, Position.D1);
            mapAdjacency(Position.D1, Position.A1, Position.G1, Position.D2);
            mapAdjacency(Position.G1, Position.D1, Position.G4);
            mapAdjacency(Position.B2, Position.B4, Position.D2);
            mapAdjacency(Position.D2, Position.D1, Position.D3, Position.B2, Position.F2);
            mapAdjacency(Position.F2, Position.F4, Position.D2);
            mapAdjacency(Position.C3, Position.D3, Position.E3);
            mapAdjacency(Position.D3, Position.C3, Position.E3, Position.D2);
            mapAdjacency(Position.E3, Position.E4, Position.D3);
            mapAdjacency(Position.A4, Position.B4, Position.A7, Position.A1);
            mapAdjacency(Position.B4, Position.B2, Position.C4, Position.B6, Position.A4);
            mapAdjacency(Position.C4, Position.C3, Position.B4, Position.C5);
            mapAdjacency(Position.E4, Position.E3, Position.E5, Position.F4);
            mapAdjacency(Position.F4, Position.E4, Position.F2, Position.F6, Position.G4);
            mapAdjacency(Position.G4, Position.G1, Position.G7, Position.F4);
            mapAdjacency(Position.C5, Position.C4, Position.D5);
            mapAdjacency(Position.D5, Position.C5, Position.E5, Position.D6);
            mapAdjacency(Position.E5, Position.E4, Position.D5);
            mapAdjacency(Position.B6, Position.B4, Position.D6);
            mapAdjacency(Position.D6, Position.B6, Position.F6, Position.D5, Position.D7);
            mapAdjacency(Position.F6, Position.F4, Position.D6);
            mapAdjacency(Position.A7, Position.D7, Position.A4);
            mapAdjacency(Position.D7, Position.A7, Position.G7, Position.D6);
            mapAdjacency(Position.G7, Position.G4, Position.D7);
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

    private List<Position> getAdjacentMill(Position srcsPosition, String angle) {
        return millMap.get(srcsPosition + angle);
    }

    public boolean positionsAreAdjacent(Position p1, Position p2) {
        if (p1 == null || p2 == null)
            return false;
        List<Position> list = adjacencyMap.get(p1);
        if (list == null)
            return false;
        return adjacencyMap.get(p1).contains(p2);
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
                (!flying && !positionsAreAdjacent(source, destination)))
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

    public boolean foundMatchingMill(Position p, Color c, String angle) {
        List<Position> adjacent = getAdjacentMill(p, angle);
        Checker c1 = checkerPosMap.get(p);
        Checker c2 = checkerPosMap.get(adjacent.get(0));
        Checker c3 = checkerPosMap.get(adjacent.get(1));
        return c1.color == c2.color && c2.color == c3.color;
    }

    /**
     *  Saves 3 positions as a horizontal or vertical mill.
     */
    private static void mapMills(Position p1, String angle, Position p2, Position p3) {
        List<Position> l1 = new ArrayList<>();
        l1.add(p2);
        l1.add(p3);
        millMap.put(p1 + angle, l1);
        List<Position> l2 = new ArrayList<>();
        l2.add(p1);
        l2.add(p3);
        millMap.put(p2 + angle, l2);
        List<Position> l3 = new ArrayList<>();
        l3.add(p1);
        l3.add(p2);
        millMap.put(p3 + angle, l3);
    }

    private static void mapAdjacency(Position source, Position... positions) {
        List<Position> list = new ArrayList<>();
        String s = "";
        for (Position p : positions) {
            list.add(p);
        }
        adjacencyMap.put(source, list);
        System.out.println("mapAdjacency - " + source + ": " + list);
    }

    @Override
    public String toString() {
        return "Board2{" +
                "checkers=" + checkerPosMap.values() +
                '}';
    }
}
