package expression.expression;

import expression.wrapper.AbstractWrapper;
import expression.exceptions.DivisionByZeroEvaluateException;

public class Mod<T extends Number> extends AbstractBinaryOperation<T> {
    public Mod(GenericExpression<T> firstArgument, GenericExpression<T> secondArgument) {
        super(firstArgument, secondArgument);
    }

    @Override
    public OperationPriority getPriority() {
        return OperationPriority.MULTIPLICATIVE;
    }

    @Override
    protected AbstractWrapper<T> executeOperation(AbstractWrapper<T> first, AbstractWrapper<T> second) {
        try {
            return first.mod(second);
        } catch (ArithmeticException exception) {
            throw new DivisionByZeroEvaluateException(this);
        }
    }

    @Override
    protected String getOperatorSymbol() {
        return "mod";
    }
}
