package parser;

public class SubtractionOp implements Op {
    public boolean equals(final Object other) {
        return other instanceof SubtractionOp;
    }

    public int hashCode() {
        return 202;
    }

    public String toString() {
        return "SubtractionOp";
    }
}