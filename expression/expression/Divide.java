package expression.expression;

import expression.wrapper.AbstractWrapper;
import expression.exceptions.DivisionByZeroEvaluateException;

public class Divide<T extends Number> extends AbstractBinaryOperation<T> {

    public Divide(GenericExpression<T> firstArgument, GenericExpression<T> secondArgument) {
        super(firstArgument, secondArgument);
    }

    @Override
    protected AbstractWrapper<T> executeOperation(AbstractWrapper<T> first, AbstractWrapper<T> second) {
        try {
            return first.divide(second);
        } catch (ArithmeticException exception) {
            throw new DivisionByZeroEvaluateException(this);
        }
    }

    @Override
    protected String getOperatorSymbol() {
        return "/";
    }

    @Override
    public OperationPriority getPriority() {
        return OperationPriority.MULTIPLICATIVE;
    }
}
