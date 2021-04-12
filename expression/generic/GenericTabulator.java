package expression.generic;

import expression.exceptions.ParsingException;
import expression.wrapper.*;

import java.util.Arrays;
import java.util.Map;

public class GenericTabulator implements Tabulator {

    @Override
    public Object[][][] tabulate(final String mode, final String expression,
                                 final int x1, final int x2, final int y1, final int y2, final int z1, final int z2)
            throws ParsingException {
        final Map<String, TypedTabulator<? extends Number>> typedTabulatorMap = Map.of(
                "i", new TypedTabulator<>(IntegerWrapper::new, IntegerWrapper::new),
                "d", new TypedTabulator<>(DoubleWrapper::new, DoubleWrapper::new),
                "bi", new TypedTabulator<>(BigIntegerWrapper::new, BigIntegerWrapper::new),
                "u", new TypedTabulator<>(UncheckedIntegerWrapper::new, UncheckedIntegerWrapper::new),
                "s", new TypedTabulator<>(UncheckedShortWrapper::new, UncheckedShortWrapper::new),
                "l", new TypedTabulator<>(UncheckedLongWrapper::new, UncheckedLongWrapper::new)
        );

        if (!typedTabulatorMap.containsKey(mode)) {
            return null;
        }
        return typedTabulatorMap.get(mode).tabulate(expression, x1, x2, y1, y2, z1, z2);
    }

    public static void main(final String[] args) {
        if (args.length < 2) {
            System.err.println("Expected two arguments: [evaluation mode] [expression]");
            return;
        }
        if (args[0].length() == 0) {
            System.err.println("Expected valid evaluation mode, but evaluation mode argument empty");
            return;
        }

        final Tabulator tabulator = new GenericTabulator();
        try {
            final Object[][][] result = tabulator.tabulate(args[0].substring(1), args[1], -2, 2, -2, 2, -2, 2);
            System.out.println(Arrays.deepToString(result));
        } catch (final Exception exception) {
            System.err.println(exception.getMessage());
        }
    }
}
