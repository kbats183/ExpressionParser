package expression.expression;

import expression.wrapper.AbstractWrapper;

public class Add<T extends Number> extends AbstractBinaryOperation<T> {

    public Add(GenericExpression<T> firstArgument, GenericExpression<T> secondArgument) {
        super(firstArgument, secondArgument);
    }

    @Override
    protected AbstractWrapper<T> executeOperation(AbstractWrapper<T> first, AbstractWrapper<T> second) {
        return first.add(second);
    }

    @Override
    protected String getOperatorSymbol() {
        return "+";
    }

    @Override
    public OperationPriority getPriority() {
        return OperationPriority.ADDITIVE;
    }
}
