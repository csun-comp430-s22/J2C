package parser;

public class NotEqualsOp implements Op {

    public boolean equals(final Object other) {
        return other instanceof NotEqualsOp;
    }

    public int hashCode() {
        return 208;
    }

    public String toString() {
        return "NotEqualsOp";
    }
}