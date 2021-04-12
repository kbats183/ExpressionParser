package expression.test;

import expression.wrapper.AbstractWrapper;
import expression.wrapper.IntegerWrapper;
import expression.exceptions.ParsingException;
import expression.expression.*;
import expression.parser.ExpressionParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class ParserTest {
    private final ExpressionParser<Integer> parser = new ExpressionParser<>(IntegerWrapper::new);
    private final Random random = new Random();

    private AbstractWrapper<Integer> fromInt(int x) {
        return new IntegerWrapper(x);
    }

    private Object parse(final String ex) throws ParsingException {
        return parser.parse(ex);
    }

    private void valid(final Object expected, final String ex) throws ParsingException {
        Assertions.assertEquals(expected, parse(ex));
    }

    private void invalid(final Object expected, final String ex) throws ParsingException {
        Assertions.assertNotEquals(expected, parse(ex));
    }

    private void exception(final String input) {
        try {
            final Object value = parse(input);
            Assertions.fail("Expected fail, found " + input + " for " + input);
        } catch (ParsingException ignored) {

        }
    }

    private GenericExpression<Integer> genValue() {
        return genValue(true);
    }

    private GenericExpression<Integer> genMultiplicative() {
        if (random.nextInt(3) == 0) {
            return genValue();
        }
        if (random.nextBoolean()) {
            return new Multiply<Integer>(genMultiplicative(), genValue());
        } else {
            return new Subtract<>(genMultiplicative(), genValue());
        }
    }

    private GenericExpression<Integer> randomAdditive(GenericExpression<Integer> f, GenericExpression<Integer> s) {
        if (random.nextBoolean()) {
            return new Add<>(f, s);
        } else {
            return new Subtract<>(f, s);
        }
    }

    private GenericExpression<Integer> genHardAdditive() {
        GenericExpression<Integer> f;
        if (random.nextInt(3) == 0) {
            f = genHardAdditive();
        } else {
            f = genMultiplicative();
        }
        GenericExpression<Integer> s = genMultiplicative();
        if (random.nextBoolean()) {
            f = randomAdditive(f, genMultiplicative());
        }

        if (random.nextBoolean()) {
            return randomAdditive(f, s);
        } else {
            return randomAdditive(randomAdditive(f, s), genHardAdditive());
        }

    }

    private GenericExpression<Integer> genValue(boolean mbNegative) {
        if (random.nextInt(3) == 0) {
            String[] vars = new String[]{"x", "y", "z"};
            return new Variable<>(vars[random.nextInt(3)]);
        } else {
            if (mbNegative && random.nextInt(3) == 0) {
                return new Const<>(fromInt(-random.nextInt()));
            }
            return new Const<>(fromInt(random.nextInt()));
        }
    }

    private String genWhitespace() {
        int n = random.nextInt(10);
        char[] r = new char[n];
        char[] ws = new char[]{' ', '\t'};
        for (int i = 0; i < n; i++) {
            r[i] = ws[random.nextInt(2)];
        }
        return new String(r);
    }

    @Test
    public void testVarAndConst() throws ParsingException {
        valid(new Variable<Integer>("x"), "x");
        valid(new Variable<Integer>("y"), "y");
        invalid(new Variable<Integer>("z"), "y");
        valid(new Variable<Integer>("z"), "z");
        valid(new Const<>(fromInt(123)), "123");
        for (int i = 0; i < 25; i++) {
            GenericExpression<Integer> ex = genValue(false);
            valid(ex, ex.toString());
        }
        exception("12 + xy ");
    }

    @Test
    public void testMinus() throws ParsingException {
        valid(new Const<Integer>(fromInt(-1)), "-1");
        valid(new Const<Integer>(fromInt(2)), "--2");
        valid(new Const<Integer>(fromInt(-3)), "-----3");
        invalid(new Const<Integer>(fromInt(3)), "-----3");
    }

    @Test
    public void testMultiplicativeEasy() throws ParsingException {
        valid(new Multiply<>(new Const<>(fromInt(1)), new Const<>(fromInt(2))), "1*2");
        invalid(new Multiply<>(new Const<>(fromInt(3)), new Variable<>("x")), "3   /   x");
        valid(new Divide<>(new Const<>(fromInt(3)), new Variable<>("x")), "3   /   x");
        for (int i = 0; i < 20; i++) {
            GenericExpression<Integer> arg1 = genValue(true), arg2 = genValue(true);
            valid(new Multiply<>(arg1, arg2), genWhitespace() + arg1.toString() + genWhitespace() + '*' + genWhitespace() + arg2.toString());
            valid(new Divide<>(arg1, arg2), genWhitespace() + arg1.toString() + genWhitespace() + '/' + genWhitespace() + arg2.toString());
        }
    }

    @Test
    public void testBrackets() throws ParsingException {
        valid(new Const<>(fromInt(1)), "(1)");
        valid(new Const<>(fromInt(123)), "((123))");

        valid(new Multiply<>(new Const<>(fromInt(4)), new Variable<>("x")), "((4) * ((x)))");
        valid(new Multiply<>(new Const<>(fromInt(5)), new Divide<>(new Const<>(fromInt(6)), new Variable<>("y"))), "((5) * ((6/y)))");
        invalid(new Divide<>(new Multiply<>(new Const<>(fromInt(5)), new Const<>(fromInt(6))), new Variable<>("y")),
                " (  ( 5) *  ( (  6 /   4  )  )  )  ");
        valid(new Divide<>(new Variable<>("z"), new Const<>(fromInt(13))), "(\tz) / (--13) ");
    }

    @Test
    public void testMultiplicativeMedium() throws ParsingException {
        valid(new Divide<>(new Multiply<>(new Const<>(fromInt(1)), new Const<>(fromInt(2))), new Variable<>("x")), "1*2/x");
        for (int i = 0; i < 25; i++) {
            GenericExpression<Integer> a1 = genValue(), a2 = genValue(), a3 = genValue();
            GenericExpression<Integer> mp1 = new Multiply<Integer>(a1, a2);
            GenericExpression<Integer> mp2 = new Multiply<Integer>(mp1, a3);
            GenericExpression<Integer> dv1 = new Divide<Integer>(a1, a2);
            GenericExpression<Integer> dv2 = new Divide<Integer>(new Multiply<Integer>(dv1, a3), a1);
            List<GenericExpression<Integer>> ts = List.of(mp1, mp2, dv1, dv2);
            for (GenericExpression<Integer> t : ts) {
                valid(t, t.toString());
            }
        }
    }

    @Test
    public void testAdditiveEasy() throws ParsingException {
        valid(new Add<>(new Const<>(fromInt(1)), new Const<>(fromInt(2))), "1  + 2");
        valid(new Subtract<>(new Const<>(fromInt(3)), new Const<>(fromInt(4))), "  3 -" + genWhitespace() + "4");
        valid(new Add<>(new Variable<>("x"), new Multiply<>(new Const<>(fromInt(2)), new Const<>(fromInt(2)))), "x + 2 *2");
        for (int i = 0; i < 20; i++) {
            GenericExpression<Integer> arg1 = genValue(), arg2 = genValue();
            valid(new Add<>(arg1, arg2), genWhitespace() + arg1.toString() + genWhitespace() + '+' + genWhitespace() + arg2.toString());
            valid(new Subtract<>(arg1, arg2), genWhitespace() + arg1.toString() + genWhitespace() + '-' + genWhitespace() + arg2.toString());
        }
    }

    @Test
    public void testAdditiveMedium() throws ParsingException {
        for (int i = 0; i < 25; i++) {
            GenericExpression<Integer> a1 = genValue(), a2 = genValue(), a3 = genValue();
            GenericExpression<Integer> ad1 = new Add<Integer>(a1, a2);
            GenericExpression<Integer> sb1 = new Subtract<Integer>(ad1, a3);
            GenericExpression<Integer> sb2 = new Subtract<Integer>(a1, a2);
            GenericExpression<Integer> ad2 = new Add<Integer>(new Add<>(sb2, a3), a1);
            List<GenericExpression<Integer>> ts = List.of(ad1, sb1, sb2, ad2);
            for (GenericExpression<Integer> t : ts) {
                valid(t, t.toString());
            }
        }
    }

    @Test
    public void testMultiplicativeHard() throws ParsingException {
        valid(new Add<>(new Variable<>("x"), new Multiply<>(new Const<>(fromInt(1)), new Variable<>("y"))), "x+1*y");
        valid(new Subtract<>(new Variable<>("x"),
                        new Multiply<>(new Multiply<>(new Const<>(fromInt(1)), new Variable<>("y")), new Const<>(fromInt(2)))),
                "x-1*y*2");
        for (int i = 0; i < 40; i++) {
            GenericExpression<Integer> mp1 = genMultiplicative();
            GenericExpression<Integer> mp2 = genMultiplicative();
            GenericExpression<Integer> mp3 = genMultiplicative();
            List<GenericExpression<Integer>> ts = List.of(
                    new Add<>(genValue(), mp1),
                    new Subtract<>(mp1, genValue()),
                    new Add<>(mp1, mp2),
                    new Subtract<>(mp1, mp2),
                    new Add<>(new Subtract<>(mp1, mp2), mp3)
            );
            for (GenericExpression<Integer> t : ts) {
                valid(t, t.toString());
            }
            valid(new Add<>(mp1, new Subtract<>(mp2, mp3)),
                    "\t " + mp1.toString() + "  + (  ( " + mp2.toString() + genWhitespace() +
                            "-(" + genWhitespace() + mp3.toString() + ") ) )");
        }
    }

    @Test
    public void testAdditiveHard() throws ParsingException {
        for (int i = 0; i < 100; i++) {
            GenericExpression<Integer> arg1 = genHardAdditive();
            GenericExpression<Integer> arg2 = genHardAdditive();
            GenericExpression<Integer> arg3 = genHardAdditive();
            GenericExpression<Integer> at1 = randomAdditive(arg1, arg2);
            GenericExpression<Integer> mp1 = new Multiply<>(arg1, arg2);
            GenericExpression<Integer> mp2 = new Divide<>(mp1, arg3);
            GenericExpression<Integer> at2 = randomAdditive(arg1, mp2);
            List<GenericExpression<Integer>> ts = List.of(arg1, arg2, arg3, at1, mp1, mp2, at2);
            for (GenericExpression<Integer> t : ts) {
                valid(t, t.toString());
            }
        }
    }

    @Test void specialVariableName() throws ParsingException {
        parser.setValidVariableName(Set.of("x", "y", "wx", "wy"));
        valid(new Variable<>("x"), "  x  ");
        valid(new Variable<>("y"), " --y  ");
        valid(new Variable<>("wx"), "wx  ");
        valid(new Variable<>("wy"), "wy");
        exception("z");
        exception(" wz");
        exception("xx");
        exception("ww");
        parser.setValidVariableName(Set.of("x", "y", "z"));
    }

    @Test void specialBrackets() throws ParsingException {
        parser.setValidBrackets('[', ']');
        valid(new Add<>(new Variable<>("x"), new Const<>(fromInt(123))), "  [x + [[123]]] ");
        parser.setValidBrackets('(', ')');
    }
}
