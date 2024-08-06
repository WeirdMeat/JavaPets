import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    @DisplayName("drawing numbers")
    void drawNumber() {
        assertEquals(Main.drawNumber(0), "     ");
        assertEquals(Main.drawNumber(5), "  5  ");
        assertEquals(Main.drawNumber(10), " 10  ");
        assertEquals(Main.drawNumber(253), " 253 ");
        assertEquals(Main.drawNumber(2048), "2048 ");
        assertEquals(Main.drawNumber(10000), "10000");
    }

    @Test
    @DisplayName("find all empties")
    void findAllEmpties() {
        int[][] field = {{0, 1, 0}, {2, 2, 3}, {0, 0, 0}};
        assertArrayEquals(Main.findAllEmpties(field).get(0), new int[]{0, 0});
        assertArrayEquals(Main.findAllEmpties(field).get(1), new int[]{0, 2});
        assertArrayEquals(Main.findAllEmpties(field).get(2), new int[]{2, 0});
        assertArrayEquals(Main.findAllEmpties(field).get(3), new int[]{2, 1});
        assertArrayEquals(Main.findAllEmpties(field).get(4), new int[]{2, 2});
    }

    @Test
    @DisplayName("moving rows")
    void moveRowLeft() {
        int[] actualRow = new int[]{4, 4, 8, 2, 4, 2, 8, 0, 0, 0, 0, 0, 0, 0, 0};
        int[] newRow = new int[]{2, 2, 2, 2, 4, 4, 2, 4, 0, 0, 2, 0, 4, 0, 4};
        Main.moveRowLeft(newRow);
        assertArrayEquals(newRow, actualRow, "Array Equal Test");
    }

    @Test
    @DisplayName("moving field")
    void move() {
        int[][] actualField = {{4, 8, 4}, {0, 4, 0}, {0, 0, 0}};
        int[][] newField = {{2, 4, 0}, {0, 4, 4}, {2, 4, 0}};
        Main.move(newField, "W");
        assertArrayEquals(newField[0], actualField[0]);
        assertArrayEquals(newField[1], actualField[1]);
        assertArrayEquals(newField[2], actualField[2]);
    }

    @Test
    @DisplayName("checking if ends")
    void checkEnd() {
        int[][] endField = {{4, 8, 4}, {2, 4, 2}, {4, 2, 16}};
        assertTrue(Main.checkEnd(endField));
        int[][] nonEndEqualField = {{4, 8, 4}, {2, 4, 2}, {4, 2, 2}};
        assertFalse(Main.checkEnd(nonEndEqualField));
        int[][] nonEndZeroField = {{4, 8, 4}, {2, 4, 0}, {4, 2, 16}};
        assertFalse(Main.checkEnd(nonEndZeroField));
    }
}