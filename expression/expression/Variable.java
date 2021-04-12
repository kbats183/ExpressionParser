package expression.expression;

import expression.wrapper.AbstractWrapper;

public class Variable<T extends Number> implements GenericExpression<T> {
    private final String name;

    public String getName() {
        return name;
    }

    public Variable(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Variable)){
            return false;
        }
        Variable<?> variable = (Variable<?>) object;
        return this.name.equals(variable.name);
    }

    @Override
    public AbstractWrapper<T> evaluate(AbstractWrapper<T> x, AbstractWrapper<T> y, AbstractWrapper<T> z) {
        switch (name) {
            case "x": return x;
            case "y": return y;
            case "z": return z;
            default: return x;
        }
    }
}
