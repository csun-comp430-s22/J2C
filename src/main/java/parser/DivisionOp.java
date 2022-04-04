package parser;

public class DivisionOp implements Op {
    public boolean equals(final Object other) {
        return other instanceof DivisionOp;
    }
    public int hashCode() {
        return 207;
    }
    public String toString() {
        return "DivisionOp";
    }
    
}
