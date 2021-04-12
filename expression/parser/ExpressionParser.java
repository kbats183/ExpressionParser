package expression.parser;

import expression.exceptions.*;
import expression.expression.*;
import expression.wrapper.AbstractWrapper;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.BiFunction;
import java.util.function.Function;

public class ExpressionParser<T extends Number> {
    public ExpressionParser(final Function<String, AbstractWrapper<T>> constantParser) {
        this.constantParser = constantParser;
    }

    public GenericExpression<T> parse(final String source) throws ParsingException {
        return parse(new StringSource(source));
    }

    public GenericExpression<T> parse(final CharSource source) throws ParsingException {
        return new Parser(source).parseExpression();
    }

    private Map<String, OperationPriority> binaryOperationPriorityBySymbol = Map.of(
            "*", OperationPriority.MULTIPLICATIVE,
            "/", OperationPriority.MULTIPLICATIVE,
            "+", OperationPriority.ADDITIVE,
            "-", OperationPriority.ADDITIVE,
            "mod", OperationPriority.MULTIPLICATIVE
    );

    private Map<String, BiFunction<GenericExpression<T>, GenericExpression<T>, AbstractBinaryOperation<T>>> binaryOperationBySymbol = Map.of(
            "*", Multiply::new,
            "/", Divide::new,
            "+", Add::new,
            "-", Subtract::new,
            "mod", Mod::new
    );

    private Map<String, Function<GenericExpression<T>, AbstractUnaryOperation<T>>> unaryOperationBySymbol = Map.of(
            "-", UnaryNegate::new,
            "abs", Abs::new,
            "square", Square::new
    );

    private Set<String> validVariableName = Set.of("x", "y", "z");
    private Set<Character> validVariableFirstChar = Set.of('x', 'y', 'z');
    private Set<Character> validSymbolOfConstant = Set.of('0', '1', '2', '3', '4', '5', '6', '7', '8', '9');

    private char validOpeningBracketsSymbol = '(';
    private char validClosingBracketsSymbol = ')';
    private char unaryNegateSymbol = '-';

    final Function<String, AbstractWrapper<T>> constantParser;

    protected class Parser extends BaseParser {
        public Parser(final CharSource source) {
            super(source);
            nextChar();
        }

        private String nextBinaryOperation = null;

        public GenericExpression<T> parseExpression() throws ParsingException {
            final GenericExpression<T> expression = parseOperation(OperationPriority.BASE);
            if (!eof()) {
                throw new ExpectedEOFException(ch, source.getPosition());
            } else if (nextBinaryOperation != null) {
                throw new ExpectedEOFException(nextBinaryOperation, source.getPosition());
            }
            return expression;
        }

        private GenericExpression<T> parseOperation(final OperationPriority currentPriority) throws ParsingException {
            if (currentPriority.equals(OperationPriority.UNARY)) {
                return parseUnaryOperation();
            }

            GenericExpression<T> currentExpression = parseOperation(currentPriority.nextPriority());
            skipWhitespace();

            while (!eof() && ch != validClosingBracketsSymbol) {
                final String operationSymbol = getNextBinaryOperationSymbol();
                final OperationPriority operationPriority = binaryOperationPriorityBySymbol.get(operationSymbol);

                if (!operationPriority.equals(currentPriority)) {
                    break;
                }
                skipNextBinaryOperation();
                GenericExpression<T> secondArgument = parseOperation(currentPriority.nextPriority());
                currentExpression = binaryOperationBySymbol.get(operationSymbol).apply(currentExpression, secondArgument);

                skipWhitespace();
            }

            return currentExpression;
        }

        private GenericExpression<T> parseVariable() throws InvalidVariableNameException {
            final StringBuilder variableNameBuilder = new StringBuilder();
            String variableName;
            do {
                variableNameBuilder.append(ch);
                variableName = variableNameBuilder.toString();
                nextChar();
            } while (!validVariableName.contains(variableName) && !eof() && between('a', 'z'));
            if (validVariableName.contains(variableName)) {
                return new Variable<>(variableName);
            } else {
                throw new InvalidVariableNameException(variableName, source.getPosition(variableName.length()));
            }
        }

        private GenericExpression<T> parseConstant(final boolean sign) throws InvalidConstantFormat {
            final StringBuilder stringBuilder = new StringBuilder();
            if (sign) {
                stringBuilder.append(unaryNegateSymbol);
            }
            do {
                stringBuilder.append(ch);
                nextChar();
            } while (validSymbolOfConstant.contains(ch));

            final String constant = stringBuilder.toString();
            try {
                return new Const<>(constantParser.apply(constant));
            } catch (final NumberFormatException e) {
                throw new InvalidConstantFormat(constant, source.getPosition(constant.length()));
            }
        }

        private GenericExpression<T> parseUnaryOperation() throws ParsingException {
            return parseUnaryOperation(false);
        }

        private GenericExpression<T> parseUnaryOperation(final boolean sign) throws ParsingException {
            skipWhitespace();
            if (test(unaryNegateSymbol)) {
                return parseUnaryOperation(!sign);
            }
            if (validSymbolOfConstant.contains(ch)) {
                return parseConstant(sign);
            }

            final GenericExpression<T> expression;
            if (test(validOpeningBracketsSymbol)) {
                expression = parseBrackets();
            } else if (validVariableFirstChar.contains(ch)) {
                expression = parseVariable();
            } else {
                expression = unaryOperationBySymbol.get(parseUnaryOperationSymbol()).apply(parseUnaryOperation());
            }
            if (sign) {
                return unaryOperationBySymbol.get(Character.toString(unaryNegateSymbol)).apply(expression);
            } else {
                return expression;
            }

        }

        protected String parseUnaryOperationSymbol() throws ExpectedParsingException {
            final StringBuilder symbol = new StringBuilder();
            final char firstCh = ch;
            do {
                symbol.append(ch);
                nextChar();
            } while (!unaryOperationBySymbol.containsKey(symbol.toString()) && !eof() && !isWhitespace());
            if (!unaryOperationBySymbol.containsKey(symbol.toString())) {
                throw new ExpectedUnaryOperationException(symbol.toString(), source.getPosition(symbol.length()));
            } else if ('a' <= firstCh && firstCh <= 'z') {
                expectedSeparatorAfterOperationSymbol();
            }
            return symbol.toString();
        }

        private GenericExpression<T> parseBrackets() throws ParsingException {
            final GenericExpression<T> expression = parseOperation(OperationPriority.BASE);
            skipWhitespace();
            if (nextBinaryOperation != null) {
                throw new ExpectedParsingException(Character.toString(validClosingBracketsSymbol), nextBinaryOperation,
                        source.getPosition(nextBinaryOperation.length()));
            }
            expected(Character.toString(validClosingBracketsSymbol));
            return expression;
        }

        private String getNextBinaryOperationSymbol() throws ParsingException {
            if (nextBinaryOperation == null) {
                final StringBuilder symbol = new StringBuilder();
                OperationPriority operationPriority = null;
                char firstCh = ch;
                while (!eof() && !isWhitespace() && operationPriority == null) {
                    symbol.append(ch);
                    operationPriority = binaryOperationPriorityBySymbol.get(symbol.toString());
                    firstCh = ch;
                    nextChar();
                }
                if (operationPriority == null) {
                    throw new InvalidBinaryOperationException(symbol.toString(), source.getPosition(symbol.length()));
                } else if ('a' <= firstCh && firstCh <= 'z') {
                    expectedSeparatorAfterOperationSymbol();
                }
                nextBinaryOperation = symbol.toString();
            }
            return nextBinaryOperation;
        }

        private void skipNextBinaryOperation() {
            nextBinaryOperation = null;
        }

        private void expectedSeparatorAfterOperationSymbol() throws ExpectedParsingException {
            if (!isWhitespace() && ch != validOpeningBracketsSymbol && ch != unaryNegateSymbol) {
                throw new ExpectedParsingException("separation after character operation symbol", ch, source.getPosition());
            }
        }
    }

    // parser settings

    public void setBinaryOperationPriorityBySymbol(final Map<String, OperationPriority> binaryOperationPriorityBySymbol) {
        this.binaryOperationPriorityBySymbol = binaryOperationPriorityBySymbol;
    }

    public void setBinaryOperationBySymbol(final Map<String, BiFunction<GenericExpression<T>, GenericExpression<T>, AbstractBinaryOperation<T>>> binaryOperationBySymbol) {
        this.binaryOperationBySymbol = binaryOperationBySymbol;
    }

    public void setUnaryOperationBySymbol(final Map<String, Function<GenericExpression<T>, AbstractUnaryOperation<T>>> unaryOperationBySymbol) {
        this.unaryOperationBySymbol = unaryOperationBySymbol;
    }

    public void setValidVariableName(final Set<String> validVariableName) {
        this.validVariableName = validVariableName;
        this.validVariableFirstChar = new TreeSet<>();
        for (final String variable : validVariableName) {
            this.validVariableFirstChar.add(variable.charAt(0));
        }
    }

    public void setValidSymbolOfConstant(final Set<Character> validSymbolOfConstant) {
        this.validSymbolOfConstant = validSymbolOfConstant;
    }

    public void setValidBrackets(final char openingBrackets, final char closingBrackets) {
        this.validOpeningBracketsSymbol = openingBrackets;
        this.validClosingBracketsSymbol = closingBrackets;
    }

    public void setUnaryNegateSymbol(char unaryNegateSymbol) {
        this.unaryNegateSymbol = unaryNegateSymbol;
    }
}