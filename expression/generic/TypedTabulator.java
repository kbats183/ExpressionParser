package expression.generic;

import expression.exceptions.EvaluateExpressionException;
import expression.exceptions.ParsingException;
import expression.expression.GenericExpression;
import expression.parser.ExpressionParser;
import expression.wrapper.AbstractWrapper;

import java.util.function.Function;

public class TypedTabulator<T extends Number> {
    private final ExpressionParser<T> parser;
    private final Function<Integer, AbstractWrapper<T>> intToTypeConverter;

    public TypedTabulator(Function<String, AbstractWrapper<T>> stringParser, Function<Integer, AbstractWrapper<T>> intToTypeConverter) {
        this.parser = new ExpressionParser<>(stringParser);
        this.intToTypeConverter = intToTypeConverter;
    }

    private AbstractWrapper<T> wrapInt(int value) {
        return intToTypeConverter.apply(value);
    }

    public Object[][][] tabulate(final String stringExpression, final int x1, final int x2,
                                 final int y1, final int y2, final int z1, final int z2) throws ParsingException {
        GenericExpression<T> expression = parser.parse(stringExpression);
        final Object[][][] result = new Object[x2 - x1 + 1][y2 - y1 + 1][z2 - z1 + 1];
        for (int x = x1; x <= x2; x++) {
            for (int y = y1; y <= y2; y++) {
                for (int z = z1; z <= z2; z++) {
                    try {
                        result[x - x1][y - y1][z - z1] = expression.evaluate(wrapInt(x), wrapInt(y), wrapInt(z)).getValue();
                    } catch (final EvaluateExpressionException ignored) {
                    }
                }
            }
        }
        return result;
    }
}