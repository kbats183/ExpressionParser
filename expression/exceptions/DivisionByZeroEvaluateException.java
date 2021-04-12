package expression.exceptions;

import expression.expression.GenericExpression;

public class DivisionByZeroEvaluateException extends OperationByBadArgumentEvaluateException {
    public DivisionByZeroEvaluateException() {
        super("divide", "zero");
    }

    public DivisionByZeroEvaluateException(GenericExpression expression) {
        super("divide", "zero", expression);
    }
}
