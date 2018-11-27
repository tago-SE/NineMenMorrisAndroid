package tiago.ninemenmorris;

import org.junit.Test;

import tiago.ninemenmorris.model.Board;
import tiago.ninemenmorris.model.Checker;
import tiago.ninemenmorris.model.Color;
import tiago.ninemenmorris.model.Position;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    //@Test
    public void addition_isCorrect() {
        Board board = new Board();
        //System.out.println(board.toString());
    }

    //@Test
    public void method_checkers() {
        Board board = new Board();
        System.out.println(board.checkers());
        Checker c = board.checkers().get(0);
        c.setColor(Color.Red);
        System.out.println(board.checkers());
    }

    @Test
    public void board_adjacency() {
        Board b = new Board();
        assertEquals(b.positionsAreAdjacent(Position.A1, Position.D1), true);
        assertEquals(b.positionsAreAdjacent(Position.A1, Position.D2), false);
    }
}