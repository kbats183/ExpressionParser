package expression.exceptions;

public class ExpectedUnaryOperationException extends ExpectedParsingException {
    public ExpectedUnaryOperationException(String actual, String position) {
        super("unary operation symbol", actual, position);
    }

    public ExpectedUnaryOperationException(String expected, char actual, String position) {
        super(expected, actual, position);
    }
}
