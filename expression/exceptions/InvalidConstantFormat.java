package expression.exceptions;

public class InvalidConstantFormat extends ParsingException {
    public InvalidConstantFormat(String actual, String position) {
        super("invalid constant format: " + actual + " at " + position);
    }
}
