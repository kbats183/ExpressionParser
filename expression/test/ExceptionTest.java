package expression.test;

import expression.wrapper.IntegerWrapper;
import expression.exceptions.EvaluateExpressionException;
import expression.exceptions.ParsingException;
import expression.expression.*;
import expression.parser.ExpressionParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

public class ExceptionTest {
    private final ExpressionParser<Integer> parser = new ExpressionParser<>(IntegerWrapper::new);

    private GenericExpression<Integer> parse(final String ex) throws ParsingException {
        return parser.parse(ex);
    }

    private void valid(final Object expected, final String ex) throws ParsingException {
        Assertions.assertEquals(expected, parse(ex));
    }

    private IntegerWrapper wInt(final int x) {
        return new IntegerWrapper(x);
    }

    private Const<Integer> constByInt(final int x) {
        return new Const<>(wInt(x));
    }

    private void exception(final String input) {
        try {
            final GenericExpression<Integer> value = parse(input);
            value.evaluate(wInt(0), wInt(0), wInt(0));
            Assertions.fail("Expected fail, found " + value + " for " + input);
        } catch (EvaluateExpressionException | ParsingException e) {
            System.out.print("Expected error");
            System.out.println("    " + e.getMessage());
        }
    }

    private void exception(final GenericExpression<Integer> expression) {
        try {
            expression.evaluate(wInt(0), wInt(0), wInt(0));
            Assertions.fail("Expected fail, found " + expression);
        } catch (EvaluateExpressionException ignored) {
        }
    }

    @Test
    public void testOverflow() throws ParsingException {
        valid(constByInt(4), "   4  ");
        List<GenericExpression<Integer>> exs;
        exs = List.of(
                new Add<>(constByInt(Integer.MAX_VALUE), constByInt(1)),
                new Add<>(constByInt(Integer.MIN_VALUE), constByInt(-1)),
                new Add<>(constByInt(Integer.MAX_VALUE / 2), constByInt(2 + Integer.MAX_VALUE / 2)),
                new Add<>(constByInt(-2147483648 / 2), constByInt(-2147483648 / 2 - 1)),
                new Subtract<>(constByInt(Integer.MAX_VALUE), constByInt(-1)),
                new Subtract<>(constByInt(Integer.MIN_VALUE), constByInt(1)),
                new Subtract<>(constByInt(Integer.MIN_VALUE / 2), constByInt(-1 + Integer.MIN_VALUE)),
                new Subtract<>(constByInt(Integer.MAX_VALUE / 2), constByInt(-Integer.MAX_VALUE)),
                new Multiply<>(constByInt(Integer.MIN_VALUE), constByInt(2)),
                new Multiply<>(constByInt(Integer.MAX_VALUE), constByInt(2)),
                new Multiply<>(constByInt(Integer.MIN_VALUE), constByInt(-1)),
                new Multiply<>(constByInt(Integer.MAX_VALUE / 2 + 2), constByInt(2)),
                new Multiply<>(constByInt(123), constByInt(Integer.MAX_VALUE)),
                new Divide<>(constByInt(123), new Variable<>("x")),
                new UnaryNegate<>(new Add<>(constByInt(Integer.MIN_VALUE), constByInt(0)))
        );

        for (GenericExpression<Integer> ex : exs) {
            exception(ex);
            exception(parse(ex.toString()));
        }
    }

    @Test
    public void testParsing() {
        exception("@");
        exception("1 + 2 + %");
        exception("(1+2 ");
        exception("(1 % 2)");
        exception("dgs (1 % 2)");
        exception("x * y ((z - 1) / 10)");
        exception("((2+2-))");
        exception("((2+2-))-0/(--2)*555");
        exception("-2147483649");
        exception("2147483648");
    }
}
