package expression;

import expression.exceptions.EvaluateExpressionException;
import expression.exceptions.ParsingException;
import expression.generic.TypedEvaluator;
import expression.wrapper.*;

import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        final Map<String, TypedEvaluator<? extends Number>> typedEvaluatorMap = Map.of(
                "i", new TypedEvaluator<>(IntegerWrapper::new, IntegerWrapper::new),
                "d", new TypedEvaluator<>(DoubleWrapper::new, DoubleWrapper::new),
                "bi", new TypedEvaluator<>(BigIntegerWrapper::new, BigIntegerWrapper::new),
                "u", new TypedEvaluator<>(UncheckedIntegerWrapper::new, UncheckedIntegerWrapper::new),
                "s", new TypedEvaluator<>(UncheckedShortWrapper::new, UncheckedShortWrapper::new),
                "l", new TypedEvaluator<>(UncheckedLongWrapper::new, UncheckedLongWrapper::new)
        );

        System.out.println("Type evaluation mode. You can use:");
        System.out.println("\tinteger with overflow detection (i),");
        System.out.println("\tinteger with out overflow detection (u),");
        System.out.println("\tdouble with out overflow detection (d),");
        System.out.println("\tbig integer with out overflow detection (bi),");
        System.out.println("\tshort integer with out overflow detection (s),");
        System.out.println("\tlong integer with out overflow detection (l)");

        TypedEvaluator<? extends Number> evaluator;

        do {
            final String userMode = scanner.nextLine();
            evaluator = typedEvaluatorMap.get(userMode);
            if (evaluator == null) {
                System.err.println("Invalid evaluation mode. Input example: bi");
            }
        } while (evaluator == null);

        System.out.println("Type you mathematics expression. You can use integer constants; variable x, y, z; operations +, -, *, /, abs, mod, square; brackets.");
        System.out.println("Example: abs(5 * x / (32 - y)) mod -(z * sqrt(5))");

        final String stringExpression = scanner.nextLine();

        try {
            evaluator.parse(stringExpression);
            System.out.printf("Parser read expression: %s\n", evaluator.getCurrentExpression().toString());
            do {
                System.out.println("Type x, y, z variable for evaluate expression: ");
                int[] variableValues = new int[3];
                int variableValuesCounter = 0;
                while (variableValuesCounter < 3) {
                    if (scanner.hasNextInt()) {
                        variableValues[variableValuesCounter++] = scanner.nextInt();
                    } else {
                        scanner.next();
                    }
                }
                try {
                    Number result = evaluator.evaluate(variableValues[0], variableValues[1], variableValues[2]);
                    System.out.printf("Expression value if x = %d, y = %d, z = %d is %s\n", variableValues[0], variableValues[1], variableValues[2], result);
                } catch (EvaluateExpressionException exception) {
                    System.out.println("EvaluateExpressionException: " + exception.getMessage());
                }
                System.out.println("Evaluate this expression again? [Y, n]");
            } while (scanner.next().toLowerCase().equals("y"));
        } catch (ParsingException exception) {
            System.err.println("ParsingException: " + exception.getMessage());
        }
        scanner.close();
    }
}
