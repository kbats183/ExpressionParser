package expression.exceptions;

public class ExpectedParsingException extends ParsingException {
    public ExpectedParsingException(String expected, String actual, String position) {
        super("expected " + expected + " but encountered " + actual + " at pos " + position);
    }

    public ExpectedParsingException(String expected, char actual, String position) {
        this(expected, Character.toString(actual), position);
    }
}
