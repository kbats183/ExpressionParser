package expression.wrapper;

import expression.exceptions.DivisionByZeroEvaluateException;

import java.math.BigInteger;

public class BigIntegerWrapper extends AbstractWrapper<BigInteger> {
    public BigIntegerWrapper(BigInteger value) {
        super(value);
    }

    public BigIntegerWrapper(long value) {
        super(BigInteger.valueOf(value));
    }

    public BigIntegerWrapper(final String value) {
        super(new BigInteger(value));
    }

    @Override
    protected BigInteger addImplements(BigInteger secondArgument) {
        return value.add(secondArgument);
    }

    @Override
    protected BigInteger subtractImplements(BigInteger secondArgument) {
        return value.subtract(secondArgument);
    }

    @Override
    protected BigInteger multiplyImplements(BigInteger secondArgument) {
        return value.multiply(secondArgument);
    }

    @Override
    protected BigInteger divideImplements(BigInteger secondArgument) {
        if (secondArgument.compareTo(BigInteger.ZERO) == 0) {
            throw new DivisionByZeroEvaluateException();
        }
        return value.divide(secondArgument);
    }

    @Override
    protected BigInteger modImplements(BigInteger secondArgument) {
        return value.mod(secondArgument);
    }

    @Override
    protected BigInteger negateImplements() {
        return value.negate();
    }

    @Override
    protected BigInteger absImplements() {
        return value.abs();
    }

    @Override
    protected BigInteger squareImplements() {
        return value.multiply(value);
    }

    @Override
    public AbstractWrapper<BigInteger> newWrapper(BigInteger value) {
        return new BigIntegerWrapper(value);
    }
}
