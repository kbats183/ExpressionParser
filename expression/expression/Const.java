package expression.expression;

import expression.wrapper.AbstractWrapper;

public class Const<T extends Number> implements GenericExpression<T> {
    final AbstractWrapper<T> value;

    public Const(AbstractWrapper<T> value) {
        this.value = value;
    }

    public AbstractWrapper<T> getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        return this.value.equals(((Const<?>) object).getValue());
    }

    @Override
    public AbstractWrapper<T> evaluate(AbstractWrapper<T> x, AbstractWrapper<T> y, AbstractWrapper<T> z) {
        return value;
    }
}
