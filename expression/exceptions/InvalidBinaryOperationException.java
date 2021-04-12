package expression.exceptions;

public class InvalidBinaryOperationException extends ParsingException {
    public InvalidBinaryOperationException(String actual, String position) {
        super(actual + " is invalid binary operation symbol at pos " + position);
    }
}
