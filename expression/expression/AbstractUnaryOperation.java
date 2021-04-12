package expression.expression;

import expression.wrapper.AbstractWrapper;

public abstract class AbstractUnaryOperation<T extends Number> implements GenericExpression<T> {
    protected final GenericExpression<T> argument;

    public AbstractUnaryOperation(GenericExpression<T> argument) {
        this.argument = argument;
    }

    @Override
    public AbstractWrapper<T> evaluate(AbstractWrapper<T> x, AbstractWrapper<T> y, AbstractWrapper<T> z) {
        return executeOperation(argument.evaluate(x, y, z));
    }

    protected abstract AbstractWrapper<T> executeOperation(AbstractWrapper<T> x);

    @Override
    public String toString() {
        return "(" + getOperatorSymbol() + argument.toString() + ")";
    }

    protected abstract String getOperatorSymbol();

    @Override
    public int hashCode() {
        return (argument.hashCode() * 37 + getOperatorSymbol().hashCode()) * 13;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        AbstractUnaryOperation<?> that = (AbstractUnaryOperation<?>) object;
        return this.getOperatorSymbol().equals(that.getOperatorSymbol())
                && this.argument.equals(that.argument);
    }

    @Override
    public OperationPriority getPriority() {
        return OperationPriority.UNARY;
    }
}
