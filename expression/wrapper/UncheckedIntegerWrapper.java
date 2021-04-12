package expression.wrapper;

public class UncheckedIntegerWrapper extends AbstractWrapper<Integer> {
    public UncheckedIntegerWrapper(Integer value) {
        super(value);
    }

    public UncheckedIntegerWrapper(final String value) {
        super(Integer.parseInt(value));
    }

    @Override
    protected Integer addImplements(Integer secondArgument) {
        return value + secondArgument;
    }

    @Override
    protected Integer subtractImplements(Integer secondArgument) {
        return value - secondArgument;
    }

    @Override
    protected Integer multiplyImplements(Integer secondArgument) {
        return value * secondArgument;
    }

    @Override
    protected Integer divideImplements(Integer secondArgument) {
        return value / secondArgument;
    }

    @Override
    protected Integer modImplements(Integer secondArgument) {
        return value % secondArgument;
    }

    @Override
    protected Integer negateImplements() {
        return -value;
    }

    @Override
    protected Integer absImplements() {
        return value >= 0 ? value : -value;
    }

    @Override
    protected Integer squareImplements() {
        return value * value;
    }

    @Override
    public AbstractWrapper<Integer> newWrapper(Integer value) {
        return new UncheckedIntegerWrapper(value);
    }
}