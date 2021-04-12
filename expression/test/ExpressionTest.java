package expression.test;

import expression.exceptions.EvaluateExpressionException;
import expression.expression.*;
import expression.wrapper.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.function.Function;

public class ExpressionTest {
    private AbstractWrapper<Integer> wInt(int v) {
        return new IntegerWrapper(v);
    }
    private AbstractWrapper<Double> wDouble(double v) {
        return new DoubleWrapper(v);
    }
    private AbstractWrapper<BigInteger> wBInt(final BigInteger v) {
        return new BigIntegerWrapper(v);
    }

    private void assertToString(String expected, GenericExpression<?> expression) {
        Assertions.assertEquals(expected, expression.toString());
    }

    @Test
    public void toStringTest() {
        assertToString("x", new Variable<>("x"));
        assertToString("y", new Variable<>("y"));
        assertToString("z", new Variable<>("z"));
        assertToString("132423423", new Const<>(wInt(132423423)));
        assertToString("(-432534534)", new UnaryNegate<>(new Const<>(wInt(432534534))));
        assertToString("(-(-2285538))", new UnaryNegate<>(new UnaryNegate<>(
                new Const<>(wInt(2285538)))
        ));
        assertToString("-122423423534534534534545453423423423", new Const<>(wBInt(new BigInteger("-122423423534534534534545453423423423"))));
        assertToString("(x + 13)", new Add<>(new Variable<>("x"), new Const<>(wInt(13))));
        assertToString("(-13121 - y)", new Subtract<>(new Const<>(wInt(-13121)), new Variable<>("y")));
        assertToString("(3453453453465346346436423 * -223)", new Multiply<>(
                new Const<>(new BigIntegerWrapper("3453453453465346346436423")),
                new Const<>(new BigIntegerWrapper("-223"))));
        assertToString("(-(3424234 / -42322))", new UnaryNegate<>(new Divide<>(
                new Const<>(wInt(3424234)), new Const<>(wInt(-42322)))));

    }

    @Test
    public void evaluateTest() {
        assertEvaluate((x, y, z) -> 12, new Const<>(wInt(12)), IntegerWrapper::new);
        assertEvaluate((x, y, z) -> x, new Variable<>("x"), IntegerWrapper::new);
        assertEvaluate((x, y, z) -> y, new Variable<>("y"), IntegerWrapper::new);
        assertEvaluate((x, y, z) -> z, new Variable<>("z"), IntegerWrapper::new);
        assertEvaluate((x, y, z) -> x + z, new Add<>(new Variable<>("x"), new Variable<>("z")), IntegerWrapper::new);
        assertEvaluate((x, y, z) -> y - 456, new Subtract<>(new Variable<>("y"), new Const<>(wInt(456))), IntegerWrapper::new);
        assertEvaluate((x, y, z) -> 99 / z, new Divide<>(new Const<>(wDouble(99)), new Variable<>("z")), DoubleWrapper::new);
        assertEvaluate((x, y, z) -> x + y * Math.abs(z), new Add<>(new Variable<>("x"), new Multiply<>(new Variable<>("y"), new Abs<>(new Variable<>("z")))), UncheckedLongWrapper::new);
        assertEvaluate((x, y, z) -> x * (y / (245 % Math.abs(z))),
                new Multiply<>(new Variable<>("x"), new Divide<>(
                        new Variable<>("y"), new Mod<>(new Const<>(wDouble(245)), new Variable<>("z"))
                )), DoubleWrapper::new);
        assertEvaluate((x, y, z) -> x*x - (2  - y * Math.abs(z)), new Add<>(new Square<>(new Variable<>("x")), new UnaryNegate<>(
                new Subtract<>(new Const<>(wInt(2)), new Multiply<>(new Variable<>("y"), new Abs<>(new Variable<>("z"))))
        )), UncheckedIntegerWrapper::new);
    }

    private interface Evaluator<T extends Number> {
        T apply(T x, T y, T z);
    }

    private <T extends Number> void assertEvaluate(Evaluator<T> evaluator, GenericExpression<T> expression,
                                                   Function<Integer, AbstractWrapper<T>> conv) {
        for (int x = -50; x <= 50; x++) {
            for (int y = -50; y <= 50; y++) {
                for (int z = -50; z <= 50; z++) {
                    final AbstractWrapper<T> xW = conv.apply(x);
                    final AbstractWrapper<T> yW = conv.apply(y);
                    final AbstractWrapper<T> zW = conv.apply(z);
                    try {
                        Assertions.assertEquals(evaluator.apply(xW.getValue(), yW.getValue(), zW.getValue()), expression.evaluate(xW, yW, zW).getValue());
                    } catch (EvaluateExpressionException | ArithmeticException ignored) {
                    }
                }
            }
        }
    }

    @Test
    public void equalsTest() {
        Assertions.assertEquals(new Variable<>("xdsfds"), new Variable<>("xdsfds"));
        Assertions.assertEquals(new Const<>(new IntegerWrapper(12434)), new Const<>(new IntegerWrapper(12434)));
        Assertions.assertEquals(new Const<>(new IntegerWrapper(12434)), new Const<>(new IntegerWrapper(12434)));
    }
}
