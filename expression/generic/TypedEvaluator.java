package expression.generic;

import expression.exceptions.EvaluateExpressionException;
import expression.exceptions.ParsingException;
import expression.expression.GenericExpression;
import expression.parser.ExpressionParser;
import expression.wrapper.AbstractWrapper;

import java.util.function.Function;

public class TypedEvaluator<T extends Number> {
    private final ExpressionParser<T> parser;
    private final Function<Integer, AbstractWrapper<T>> intToTypeConverter;
    private GenericExpression<T> currentExpression;

    public TypedEvaluator(Function<String, AbstractWrapper<T>> stringParser, Function<Integer, AbstractWrapper<T>> intToTypeConverter) {
        this.parser = new ExpressionParser<>(stringParser);
        this.intToTypeConverter = intToTypeConverter;
    }

    private AbstractWrapper<T> wrapInt(int value) {
        return intToTypeConverter.apply(value);
    }

    public void parse(final String stringExpression) throws ParsingException {
        currentExpression = parser.parse(stringExpression);
    }

    public T evaluate(final int x, final int y, final int z) {
        return currentExpression.evaluate(wrapInt(x), wrapInt(y), wrapInt(z)).getValue();

    }

    public T evaluate(final String stringExpression, final int x, final int y, final int z) throws ParsingException {
        parse(stringExpression);
        try {
            return evaluate(x, y, z);
        } catch (final EvaluateExpressionException ignored) {
            return null;
        }
    }

    public GenericExpression<T> getCurrentExpression() {
        return currentExpression;
    }
}
