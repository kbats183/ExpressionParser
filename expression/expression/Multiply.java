package expression.expression;

import expression.wrapper.AbstractWrapper;

public class Multiply<T extends Number> extends AbstractBinaryOperation<T> {

    public Multiply(GenericExpression<T> firstArgument, GenericExpression<T> secondArgument) {
        super(firstArgument, secondArgument);
    }

    @Override
    protected AbstractWrapper<T> executeOperation(AbstractWrapper<T> first, AbstractWrapper<T> second) {
        return first.multiply(second);
    }

    @Override
    protected String getOperatorSymbol() {
        return "*";
    }

    @Override
    public OperationPriority getPriority() {
        return OperationPriority.MULTIPLICATIVE;
    }
}
