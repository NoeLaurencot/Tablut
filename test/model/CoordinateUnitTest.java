package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CoordinateUnitTest {

    @Test
    public void testDefaultConstructor() {
        Coordinate c = new Coordinate();
        assertEquals(0, c.getX());
        assertEquals(0, c.getY());
    }

    @Test
    public void testParameterizedConstructorAndGetters() {
        Coordinate c = new Coordinate(5, 10);
        assertEquals(5, c.getX());
        assertEquals(10, c.getY());
    }

    @Test
    public void testCopyConstructor() {
        Coordinate original = new Coordinate(3, 7);
        Coordinate copy = new Coordinate(original);

        assertEquals(3, copy.getX());
        assertEquals(7, copy.getY());
    }

    @Test
    public void testSetters() {
        Coordinate c = new Coordinate();
        c.setX(12);
        c.setY(24);

        assertEquals(12, c.getX());
        assertEquals(24, c.getY());
    }

    @Test
    public void testIncrement() {
        Coordinate c = new Coordinate(1, 2);
        c.increment(3, 4);

        assertEquals(4, c.getX());
        assertEquals(6, c.getY());
    }

    @Test
    public void testComputeManhattanDistance() {
        Coordinate start = new Coordinate(1, 1);
        Coordinate destination = new Coordinate(4, 5);

        int distance = start.computeManhattanDistance(destination);
        assertEquals(7, distance);
    }

    @Test
    public void testInvertedCopy() {
        Coordinate original = new Coordinate(8, 2);
        Coordinate inverted = original.invertedCopy();

        assertEquals(2, inverted.getX());
        assertEquals(8, inverted.getY());
        assertNotSame(original, inverted);
    }

    @Test
    public void testToString() {
        Coordinate c = new Coordinate(5, -3);
        assertEquals("X:5 Y: -3", c.toString());
    }
}