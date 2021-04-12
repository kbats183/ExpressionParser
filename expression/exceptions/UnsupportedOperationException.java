package expression.exceptions;

import expression.expression.GenericExpression;

public class UnsupportedOperationException extends EvaluateExpressionException {
    public UnsupportedOperationException(String operation, GenericExpression expression) {
        super("unsupported operation " + operation + " for " + expression);
    }
}
