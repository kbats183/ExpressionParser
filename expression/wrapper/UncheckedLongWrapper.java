package expression.wrapper;

public class UncheckedLongWrapper extends AbstractWrapper<Long> {
    public UncheckedLongWrapper(Long value) {
        super(value);
    }

    public UncheckedLongWrapper(int value) {
        super((long) value);
    }

    public UncheckedLongWrapper(final String value) {
        super(Long.parseLong(value));
    }

    @Override
    protected Long addImplements(Long secondArgument) {
        return value + secondArgument;
    }

    @Override
    protected Long subtractImplements(Long secondArgument) {
        return value - secondArgument;
    }

    @Override
    protected Long multiplyImplements(Long secondArgument) {
        return value * secondArgument;
    }

    @Override
    protected Long divideImplements(Long secondArgument) {
        return value / secondArgument;
    }

    @Override
    protected Long modImplements(Long secondArgument) {
        return value % secondArgument;
    }

    @Override
    protected Long negateImplements() {
        return -value;
    }

    @Override
    protected Long absImplements() {
        return value >= 0 ? value : -value;
    }

    @Override
    protected Long squareImplements() {
        return value * value;
    }

    @Override
    public AbstractWrapper<Long> newWrapper(Long value) {
        return new UncheckedLongWrapper(value);
    }
}