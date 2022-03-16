package parser;

public class PlusOp implements Op {
    public int hashCode() {
        return 203;
    }
    public boolean equals(Object o) {
        return o instanceof PlusOp;
    }
    public String toString() {
        return "+";
    }
    
}
