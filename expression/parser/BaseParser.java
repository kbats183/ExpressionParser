package expression.parser;

import expression.exceptions.ExpectedParsingException;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class BaseParser {
    public static final char END = '\0';
    protected final CharSource source;
    protected char ch = 0xffff;

    protected BaseParser(final CharSource source) {
        this.source = source;
    }

    protected void nextChar() {
        ch = source.hasNext() ? source.next() : END;
    }

    protected boolean test(char expected) {
        if (ch == expected) {
            nextChar();
            return true;
        }
        return false;
    }

    protected boolean eof() {
        return test(END);
    }

    protected boolean between(final char from, final char to) {
        return from <= ch && ch <= to;
    }

    protected boolean expected(String s) throws ExpectedParsingException {
        for (int i = 0; i < s.length(); i++) {
            if (!test(s.charAt(i))) {
                throw new ExpectedParsingException(s.substring(i), eof() ? "EOF" : Character.toString(ch), source.getPosition());
            }
        }
        return true;
    }

    protected boolean isWhitespace() {
        return ch == ' ' || ch == '\t';
    }

    protected void skipWhitespace() {
        while (isWhitespace()) {
            nextChar();
        }
    }
}
