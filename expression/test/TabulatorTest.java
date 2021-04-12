package expression.test;

import expression.generic.GenericTabulator;
import expression.generic.Tabulator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class TabulatorTest {
    private void ae(Object expected, Object actual) {
        Assertions.assertEquals(expected, actual);
    }

    private <T> void aev(TripleFunction<?> function, Object[][][] actual) {
        for (int x = 0; x <= 10; x++) {
            for (int y = 0; y <= 10; y++) {
                for (int z = 0; z <= 10; z++) {
                    ae(function.apply(x, y, z), actual[x][y][z]);
                }
            }
        }
    }

    private <T> void aev(TripleFunction<?> function, String actualExpression, String mode) throws Exception {
        int x1 = 0;
        int x2 = 10;
        int y1 = 0;
        int y2 = 10;
        int z1 = 0;
        int z2 = 10;
        Object[][][] actual = tabulator.tabulate(mode, actualExpression, x1, x2, y1, y2, z1, z2);
        for (int x = x1; x <= x2; x++) {
            for (int y = y1; y <= y2; y++) {
                for (int z = z1; z <= z2; z++) {
                    ae(function.apply(x, y, z), actual[x - x1][y - y1][z - z1]);
                }
            }
        }
    }

    private <T> void aev(TripleFunction<?> function, String actualExpression) throws Exception {
        aev(function, actualExpression, "i");
    }

    Tabulator tabulator = new GenericTabulator();

    @Test
    void testEasy() throws Exception {
        Object[][][] r1 = tabulator.tabulate("i", "1", 0, 10, 0, 10, 0, 10);
        ae(11, r1.length);
        ae(11, r1[0].length);
        ae(11, r1[0][0].length);
        aev((x, y, z) -> 1, r1);
        aev((x, y, z) -> x + y - z, "x+y-z");
        aev((x, y, z) -> x * 2 - y + z * (-53), "x * 2 + (-1) * y + -53*z");
        aev((x, y, z) -> x * 2 - y + z * (-53), "x * 2 + (- 1)     * y + -53*z");
        aev((x, y, z) -> ((double) x / 2 + y * ((double) z / 3)), "x / 2 + y * (z/3)", "d");
    }

    private interface TripleFunction<T extends Number> {
        T apply(int x, int y, int z);
    }
}
