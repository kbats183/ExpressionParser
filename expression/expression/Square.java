package expression.expression;

import expression.wrapper.AbstractWrapper;

public class Square<T extends Number> extends AbstractUnaryOperation<T> {
    public Square(GenericExpression<T> argument) {
        super(argument);
    }

    @Override
    protected AbstractWrapper<T> executeOperation(AbstractWrapper<T> x) {
        return x.square();
    }

    @Override
    protected String getOperatorSymbol() {
        return "square";
    }
}
