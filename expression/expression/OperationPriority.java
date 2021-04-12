package expression.expression;

public class OperationPriority {
    int ordinal;

    public OperationPriority(int ordinal) {
        this.ordinal = ordinal;
    }

    public OperationPriority nextPriority() {
        return new OperationPriority(ordinal + 1);
    }

    public int compareTo(OperationPriority other) {
        return ordinal - other.ordinal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        return ordinal == ((OperationPriority) o).ordinal;
    }

    public final static OperationPriority UNARY = new OperationPriority(30);
    public final static OperationPriority MULTIPLICATIVE = new OperationPriority(20);
    public final static OperationPriority ADDITIVE = new OperationPriority(10);
    public final static OperationPriority BASE = new OperationPriority(0);
}