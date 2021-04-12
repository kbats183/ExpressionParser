package expression.wrapper;

import expression.exceptions.DivisionByZeroEvaluateException;
import expression.exceptions.OverflowEvaluateException;

public class IntegerWrapper extends AbstractWrapper<Integer> {
    public IntegerWrapper(Integer value) {
        super(value);
    }

    public IntegerWrapper(final String value) {
        super(Integer.parseInt(value));
    }

    @Override
    protected Integer addImplements(Integer y) {
        int x = value;
        if ((y > 0 && x > 0 && Integer.MAX_VALUE - x < y) || (y < 0 && x <= 0 && y < Integer.MIN_VALUE - x)) {
            throw new OverflowEvaluateException("add");
        }
        return x + y;
    }

    @Override
    protected Integer subtractImplements(Integer y) {
        int x = value;
        if ((x >= 0 && y < 0 && x - Integer.MAX_VALUE > y) || (x < 0 && y > 0 && x - Integer.MIN_VALUE < y)) {
            throw new OverflowEvaluateException("subtract");
        }
        return value - y;
    }

    @Override
    protected Integer multiplyImplements(Integer y) {
        int x = value;
        if ((x > 0 && y > 0 && Integer.MAX_VALUE / y < x)
                || (x > 0 && y < 0 && Integer.MIN_VALUE / x > y)
                || (x < 0 && y > 0 && Integer.MIN_VALUE / y > x)
                || (x < 0 && y < 0 && (x == Integer.MIN_VALUE || Integer.MAX_VALUE / x > y))) {
            throw new OverflowEvaluateException("multiply");
        }
        return value * y;
    }

    @Override
    protected Integer divideImplements(Integer y) {
        if (y == 0) {
            throw new DivisionByZeroEvaluateException();
        } else if (value == Integer.MIN_VALUE && y == -1) {
            throw new OverflowEvaluateException("divide");
        }
        return value / y;
    }

    @Override
    protected Integer modImplements(Integer y) {
        if (y == 0) {
            throw new DivisionByZeroEvaluateException();
        }
        return value % y;
    }

    @Override
    protected Integer negateImplements() {
        if (value == Integer.MIN_VALUE) {
            throw new OverflowEvaluateException("negate");
        }
        return -value;
    }

    @Override
    protected Integer absImplements() {
        return value >= 0 ? value : -value;
    }

    @Override
    protected Integer squareImplements() {
        if (value > Math.sqrt(Integer.MAX_VALUE) || value <= -Math.sqrt(Integer.MAX_VALUE)) {
            throw new OverflowEvaluateException("square");
        }
        return value * value;
    }

    @Override
    public AbstractWrapper<Integer> newWrapper(Integer value) {
        return new IntegerWrapper(value);
    }
}