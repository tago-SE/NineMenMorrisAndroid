package tiago.ninemenmorris;

import org.junit.Test;

import tiago.ninemenmorris.model.Board2;
import tiago.ninemenmorris.model.Checker;
import tiago.ninemenmorris.model.Color;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    //@Test
    public void addition_isCorrect() {
        Board2 board = new Board2();
        //System.out.println(board.toString());
    }

    @Test
    public void method_checkers() {
        Board2 board = new Board2();
        System.out.println(board.checkers());
        Checker c = board.checkers().get(0);
        c.setColor(Color.RED);
        System.out.println(board.checkers());
    }
}