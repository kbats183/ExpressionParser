package expression.exceptions;

import expression.expression.GenericExpression;

public class OperationByBadArgumentEvaluateException extends EvaluateExpressionException {
    public OperationByBadArgumentEvaluateException(String message) {
        super(message);
    }

    public OperationByBadArgumentEvaluateException(String operation, String argument) {
        this(operation + " by " + argument);
    }

    public OperationByBadArgumentEvaluateException(String operation, String argument, GenericExpression expression) {
        this(operation + " by " + argument + ": " + expression);
    }
}
