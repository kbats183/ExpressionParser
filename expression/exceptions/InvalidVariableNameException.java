package expression.exceptions;

public class InvalidVariableNameException extends ParsingException {

    public InvalidVariableNameException(String actual, String position) {
        super(actual + " is not valid variable name at pas " + position);
    }
}
