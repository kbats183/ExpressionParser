package expression.exceptions;

import expression.expression.GenericExpression;

public class OverflowEvaluateException extends EvaluateExpressionException {
    public OverflowEvaluateException(String operation) {
        super(operation + " operation got type overflow");
    }

    public OverflowEvaluateException(String operation, GenericExpression<?> expression) {
        super(operation + " operation got integer type overflow: " + expression);
    }
}
