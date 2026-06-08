package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MoveUnitTest {

    @Test
    public void testDefaultConstructor() {
        Move m = new Move();

        assertEquals(0, m.sourceX);
        assertEquals(0, m.sourceY);
        assertEquals(0, m.destX);
        assertEquals(0, m.destY);
    }

    @Test
    public void testParameterizedConstructor() {
        Move m = new Move(1, 2, 3, 4);

        assertEquals(1, m.sourceX);
        assertEquals(2, m.sourceY);
        assertEquals(3, m.destX);
        assertEquals(4, m.destY);
    }

    @Test
    public void testCopyConstructor() {
        Move original = new Move(5, 6, 7, 8);
        Move copy = new Move(original);

        assertEquals(5, copy.sourceX);
        assertEquals(6, copy.sourceY);
        assertEquals(7, copy.destX);
        assertEquals(8, copy.destY);
        assertNotSame(original, copy);
    }

    @Test
    public void testReverse() {
        Move original = new Move(1, 2, 8, 9);
        Move reversed = original.reverse();

        assertEquals(8, reversed.sourceX);
        assertEquals(9, reversed.sourceY);
        assertEquals(1, reversed.destX);
        assertEquals(2, reversed.destY);
        assertNotSame(original, reversed);
    }

    @Test
    public void testToString() {
        Move m = new Move(0, 4, 3, 4);
        String expected = "[X:0 Y:4] -> [X:3 Y:4]";

        assertEquals(expected, m.toString());
    }

    @Test
    public void testHumanReadable_WithBaseCoordinates() {
        // (0, 0) maps to 'A' and (0+1) -> A1
        // (2, 1) maps to 'B' and (2+1) -> B3
        Move m = new Move(0, 0, 2, 1);

        assertEquals("A1 -> B3", m.humanReadable());
    }

    @Test
    public void testHumanReadable_WithUpperBoundaryCoordinates() {
        Move m = new Move(8, 8, 0, 0);

        assertEquals("I9 -> A1", m.humanReadable());
    }

    @Test
    public void testHumanReadable_WithMaxAlphabetBoundary() {
        Move m = new Move(98, 25, 99, 25);

        assertEquals("Z99 -> Z100", m.humanReadable());
    }
}