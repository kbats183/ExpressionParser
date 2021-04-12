package expression.expression;

import expression.wrapper.AbstractWrapper;

public class Abs<T extends Number> extends AbstractUnaryOperation<T> {
    public Abs(GenericExpression<T> argument) {
        super(argument);
    }

    @Override
    protected AbstractWrapper<T> executeOperation(AbstractWrapper<T> x) {
        return x.abs();
    }

    @Override
    protected String getOperatorSymbol() {
        return "abs";
    }
}
