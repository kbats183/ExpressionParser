package expression.wrapper;


public class DoubleWrapper extends AbstractWrapper<Double> {
    public DoubleWrapper(double v) {
        super(v);
    }

    public DoubleWrapper(String v) {
        super(Double.parseDouble(v));
    }

    @Override
    protected Double addImplements(Double secondArgument) {
        return value + secondArgument;
    }

    @Override
    protected Double subtractImplements(Double secondArgument) {
        return value - secondArgument;
    }

    @Override
    protected Double multiplyImplements(Double secondArgument) {
        return value * secondArgument;
    }

    @Override
    protected Double divideImplements(Double secondArgument) {
        return value / secondArgument;
    }

    @Override
    protected Double modImplements(Double secondArgument) {
        return value % secondArgument;
    }

    @Override
    protected Double negateImplements() {
        return -value;
    }

    @Override
    protected Double absImplements() {
        return value >= 0 ? value : -value;
    }

    @Override
    protected Double squareImplements() {
        return value * value;
    }

    @Override
    public AbstractWrapper<Double> newWrapper(Double value) {
        return new DoubleWrapper(value);
    }
}
