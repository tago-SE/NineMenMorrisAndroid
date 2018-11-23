package tiago.ninemenmorris;

import org.junit.Test;

import java.util.List;

import tiago.ninemenmorris.model.Board;
import tiago.ninemenmorris.model.Position;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        Board board = new Board();
        List<Position> adj = board.getAdjacent(Position.A1, "H");
    }
}