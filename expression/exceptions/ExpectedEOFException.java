package expression.exceptions;

public class ExpectedEOFException extends ExpectedParsingException {
    public ExpectedEOFException(String actual, String position) {
        super("end of file", actual, position);
    }

    public ExpectedEOFException(char actual, String position) {
        super("end of file", actual, position);
    }
}
