package expression.wrapper;

public class UncheckedShortWrapper extends AbstractWrapper<Short> {
    public UncheckedShortWrapper(Short value) {
        super(value);
    }

    public UncheckedShortWrapper(int value) {
        super((short) value);
    }

    public UncheckedShortWrapper(final String value) {
        super(Short.parseShort(value));
    }

    @Override
    protected Short addImplements(Short secondArgument) {
        return (short) (value + secondArgument);
    }

    @Override
    protected Short subtractImplements(Short secondArgument) {
        return (short) (value - secondArgument);
    }

    @Override
    protected Short multiplyImplements(Short secondArgument) {
        return (short) (value * secondArgument);
    }

    @Override
    protected Short divideImplements(Short secondArgument) {
        return (short) (value / secondArgument);
    }

    @Override
    protected Short modImplements(Short secondArgument) {
        return (short) (value % secondArgument);
    }

    @Override
    protected Short negateImplements() {
        return (short) (-value);
    }

    @Override
    protected Short absImplements() {
        return (short) (value >= 0 ? value : -value);
    }

    @Override
    protected Short squareImplements() {
        return (short) (value * value);
    }

    @Override
    public AbstractWrapper<Short> newWrapper(Short value) {
        return new UncheckedShortWrapper(value);
    }
}