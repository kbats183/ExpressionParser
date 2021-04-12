package expression.expression;

import expression.wrapper.AbstractWrapper;

public abstract class AbstractBinaryOperation<T extends Number> implements GenericExpression<T> {
    protected final GenericExpression<T> firstArgument;
    protected final GenericExpression<T> secondArgument;

    public AbstractBinaryOperation(final GenericExpression<T> firstArgument, final GenericExpression<T> secondArgument) {
        this.firstArgument = firstArgument;
        this.secondArgument = secondArgument;
    }

    @Override
    public AbstractWrapper<T> evaluate(AbstractWrapper<T> x, AbstractWrapper<T> y, AbstractWrapper<T> z) {
        return executeOperation(firstArgument.evaluate(x, y, z), secondArgument.evaluate(x, y, z));
    }

    protected abstract AbstractWrapper<T> executeOperation(AbstractWrapper<T> first, AbstractWrapper<T> second);

    @Override
    public String toString() {
        return '(' + firstArgument.toString() + ' ' +
                getOperatorSymbol() + ' ' + secondArgument.toString() + ')';
    }

    protected abstract String getOperatorSymbol();

    @Override
    public int hashCode() {
        return (firstArgument.hashCode() * 31 + getOperatorSymbol().hashCode()) * 23 + secondArgument.hashCode() * 17;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()){
            return false;
        }
        AbstractBinaryOperation<?> that = (AbstractBinaryOperation<?>) object;
        return this.getOperatorSymbol().equals(that.getOperatorSymbol())
                && this.firstArgument.equals(that.firstArgument)
                && this.secondArgument.equals(that.secondArgument);
    }
}
