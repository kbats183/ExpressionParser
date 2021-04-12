package expression;

import expression.exceptions.ParsingException;
import expression.generic.GenericTabulator;

import java.util.Scanner;
import java.util.Set;

public class Tabulator {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        final Set<String> validEvaluationMode = Set.of("i", "d", "bi", "u", "s", "l");

        System.out.println("Type evaluation mode. You can use:");
        System.out.println("\tinteger with overflow detection (i),");
        System.out.println("\tinteger with out overflow detection (u),");
        System.out.println("\tdouble with out overflow detection (d),");
        System.out.println("\tbig integer with out overflow detection (bi),");
        System.out.println("\tshort integer with out overflow detection (s),");
        System.out.println("\tlong integer with out overflow detection (l)");

        String evaluationMode;
        do {
            evaluationMode = scanner.nextLine();
            if (!validEvaluationMode.contains(evaluationMode)) {
                System.err.println("Invalid evaluation mode. Input example: bi");
            }
        } while (!validEvaluationMode.contains(evaluationMode));

        System.out.println("Type you mathematics expression. You can use integer constants; variable x, y, z; operations +, -, *, /, abs, mod, square; brackets.");
        System.out.println("Example: abs(5 * x / (32 - y)) mod -(z * sqrt(5))");

        final String stringExpression = scanner.nextLine();

        final GenericTabulator tabulator = new GenericTabulator();

        try {
            System.out.println("Type x1, x2, y1, y2, z1, z2. Your expression will evaluate for all x in [x1, x2], y in [y1, y2], z in [z1, z2].");
            int[] variableValues = new int[6];
            int variableValuesCounter = 0;
            while (variableValuesCounter < 6) {
                if (scanner.hasNextInt()) {
                    variableValues[variableValuesCounter++] = scanner.nextInt();
                } else {
                    scanner.next();
                }
            }
            final int x1 = variableValues[0];
            final int x2 = variableValues[1];
            final int y1 = variableValues[2];
            final int y2 = variableValues[3];
            final int z1 = variableValues[4];
            final int z2 = variableValues[5];
            final Object[][][] result = tabulator.tabulate(evaluationMode, stringExpression, x1, x2, y1, y2, z1, z2);

            for (int x = x1; x <= x2; x++) {
                System.out.printf("for x = %d\n", x);
                for (int y = y1; y <= y2; y++) {
                    System.out.printf("for y = %d and y in [%d, %d]: ", y, z1, z2);
                    for (int z = z1; z <= z2; z++) {
                        System.out.print("\t" + result[x - x1][y - y1][z - z1]);
                    }
                    System.out.println();
                }
            }
        } catch (ParsingException exception) {
            System.err.println("ParsingException: " + exception.getMessage());
        }
        scanner.close();
    }
}
