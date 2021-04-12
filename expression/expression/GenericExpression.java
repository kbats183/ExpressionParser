package expression.expression;

import expression.wrapper.AbstractWrapper;

public interface GenericExpression<T extends Number> {
    default OperationPriority getPriority() {
        return OperationPriority.UNARY;
    }

    AbstractWrapper<T> evaluate(AbstractWrapper<T> x, AbstractWrapper<T> y, AbstractWrapper<T> z);
}
