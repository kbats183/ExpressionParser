package expression.wrapper;

import java.util.function.Function;

public abstract class AbstractWrapper<T extends Number> {
    final T value;

    protected AbstractWrapper(final T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    public AbstractWrapper<T> add(AbstractWrapper<T> secondArgument) {
        return newWrapper(addImplements(secondArgument.getValue()));
    }

    public AbstractWrapper<T> subtract(AbstractWrapper<T> secondArgument) {
        return newWrapper(subtractImplements(secondArgument.getValue()));
    }

    public AbstractWrapper<T> multiply(AbstractWrapper<T> secondArgument) {
        return newWrapper(multiplyImplements(secondArgument.getValue()));
    }

    public AbstractWrapper<T> divide(AbstractWrapper<T> secondArgument) {
        return newWrapper(divideImplements(secondArgument.getValue()));
    }

    public AbstractWrapper<T> mod(AbstractWrapper<T> secondArgument) {
        return newWrapper(modImplements(secondArgument.getValue()));
    }

    public AbstractWrapper<T> negate() {
        return newWrapper(negateImplements());
    }

    public AbstractWrapper<T> abs() {
        return newWrapper(absImplements());
    }

    public AbstractWrapper<T> square() {
        return newWrapper(squareImplements());
    }

    abstract protected T addImplements(T secondArgument);
    abstract protected T subtractImplements(T secondArgument);
    abstract protected T multiplyImplements(T secondArgument);
    abstract protected T divideImplements(T secondArgument);
    abstract protected T modImplements(T secondArgument);
    abstract protected T negateImplements();
    abstract protected T absImplements();
    abstract protected T squareImplements();

    abstract public AbstractWrapper<T> newWrapper(T value);

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        return value.equals(((AbstractWrapper<?>) obj).getValue());
    }
}
