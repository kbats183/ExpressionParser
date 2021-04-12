package expression.expression;

import expression.wrapper.AbstractWrapper;

public class UnaryNegate<T extends Number> extends AbstractUnaryOperation<T> {
    public UnaryNegate(GenericExpression<T> argument) {
        super(argument);
    }

    protected AbstractWrapper<T> executeOperation(AbstractWrapper<T> x) {
        return x.negate();
    }

    @Override
    protected String getOperatorSymbol() {
        return "-";
    }
}
